"""Extract existing tables, gracefully reporting absent optional tables."""
import pandas as pd
from sqlalchemy import inspect, text

TABLES = [
    'products', 'suppliers', 'supplier_products', 'inventory',
    'demand_records', 'inventory_policies', 'scenarios',
    'simulation_runs', 'simulation_results', 'purchase_orders',
    'purchase_order_items', 'inventory_transactions',
]

def extract_tables(engine):
    inspector = inspect(engine)
    existing = set(inspector.get_table_names())
    result = {}
    for table in TABLES:
        if table not in existing:
            print(f'[SKIP] Missing table: {table}')
            continue
        # Table names only from the fixed allowlist above, not user input.
        with engine.connect() as connection:
            frame = pd.read_sql(text(f'SELECT * FROM `{table}`'), connection)
        result[table] = frame
        print(f'[EXTRACT] {table}: {len(frame)} rows')
    if not result:
        raise RuntimeError('None of the expected StockPilot tables were found. Check the database name and table names.')
    return result
