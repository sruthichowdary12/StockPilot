"""Optional Snowflake loader: create a RAW table and replace its contents.

Only use for non-sensitive operational/analytics tables, not users or JWT data.
"""
import os
import re
import snowflake.connector
from snowflake.connector.pandas_tools import write_pandas


def load_frames(frames):
    required = ['SNOWFLAKE_ACCOUNT','SNOWFLAKE_USER','SNOWFLAKE_PASSWORD','SNOWFLAKE_WAREHOUSE']
    missing = [key for key in required if not os.getenv(key)]
    if missing:
        raise ValueError('Missing Snowflake settings: ' + ', '.join(missing))
    database = os.getenv('SNOWFLAKE_DATABASE', 'STOCKPILOT_DWH')
    schema = os.getenv('SNOWFLAKE_SCHEMA', 'RAW')
    for name in (database, schema):
        if not re.fullmatch(r'[A-Za-z_][A-Za-z0-9_]*', name):
            raise ValueError(f'Invalid Snowflake identifier: {name}')
    params = dict(account=os.environ['SNOWFLAKE_ACCOUNT'],user=os.environ['SNOWFLAKE_USER'],
                  password=os.environ['SNOWFLAKE_PASSWORD'],warehouse=os.environ['SNOWFLAKE_WAREHOUSE'],
                  database=database,schema=schema)
    if os.getenv('SNOWFLAKE_ROLE'):
        params['role'] = os.environ['SNOWFLAKE_ROLE']
    with snowflake.connector.connect(**params) as conn:
        for name, frame in frames.items():
            if frame.empty or len(frame.columns) == 0:
                print(f'[SKIP] Snowflake empty dataset: {name}')
                continue
            table = 'RAW_' + name.upper()
            if not re.fullmatch(r'[A-Z_][A-Z0-9_]*', table):
                raise ValueError(f'Invalid table name: {table}')
            # Prototype loader stores values as strings to accommodate varying schemas.
            # A production warehouse should define typed tables and incremental MERGE logic.
            data = frame.copy()
            data.columns = [str(c).upper() for c in data.columns]
            if len(set(data.columns)) != len(data.columns) or any(not re.fullmatch(r'[A-Z_][A-Z0-9_]*', c) for c in data.columns):
                raise ValueError(f'Invalid/duplicate column names in {name}')
            data = data.astype('string').where(data.notna(), None).astype(object)
            definitions = ', '.join(f'"{c}" VARCHAR' for c in data.columns)
            with conn.cursor() as cursor:
                cursor.execute(f'CREATE TABLE IF NOT EXISTS {table} ({definitions})')
                cursor.execute(f'TRUNCATE TABLE {table}')
            success, chunks, rows, _ = write_pandas(conn, data, table_name=table, auto_create_table=False, quote_identifiers=True)
            if not success:
                raise RuntimeError(f'Snowflake load failed for {table}')
            print(f'[SNOWFLAKE] {table}: {rows} rows')
