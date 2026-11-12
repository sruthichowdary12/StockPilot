
import os
from pathlib import Path

import pandas as pd
import snowflake.connector
from dotenv import load_dotenv
from snowflake.connector.pandas_tools import write_pandas

load_dotenv()

BASE_DIR = Path(__file__).resolve().parents[1]
OUTPUT_DIR = BASE_DIR / "output"

DATASETS = [
    "demand_summary",
    "inventory_snapshot",
    "simulation_dataset",
]


def load_analytics():
    conn = snowflake.connector.connect(
        account=os.getenv("SNOWFLAKE_ACCOUNT"),
        user=os.getenv("SNOWFLAKE_USER"),
        password=os.getenv("SNOWFLAKE_PASSWORD"),
        warehouse=os.getenv("SNOWFLAKE_WAREHOUSE"),
        database=os.getenv("SNOWFLAKE_DATABASE"),
        schema="ANALYTICS",
        role=os.getenv("SNOWFLAKE_ROLE") or None,
    )

    try:
        cursor = conn.cursor()
        cursor.execute(
            "CREATE SCHEMA IF NOT EXISTS ANALYTICS"
        )
        cursor.execute("USE SCHEMA ANALYTICS")
        cursor.close()

        for dataset in DATASETS:
            csv_path = OUTPUT_DIR / f"{dataset}.csv"

            if not csv_path.exists():
                raise FileNotFoundError(
                    f"Missing dataset: {csv_path}"
                )

            df = pd.read_csv(csv_path, dtype=str)
            df.columns = [
                str(col).upper() for col in df.columns
            ]

            table_name = dataset.upper()

            # Initial staging implementation:
            # store all CSV fields as text.
            columns_sql = ", ".join(
                f'"{col}" VARCHAR' for col in df.columns
            )

            cursor = conn.cursor()
            cursor.execute(
                f'CREATE OR REPLACE TABLE "{table_name}" '
                f"({columns_sql})"
            )
            cursor.close()

            if not df.empty:
                success, chunks, rows, output = write_pandas(
                    conn,
                    df,
                    table_name,
                    quote_identifiers=True,
                )

                if not success or rows != len(df):
                    raise RuntimeError(
                        f"Load failed for {table_name}"
                    )

            print(
                f"[SNOWFLAKE ANALYTICS] "
                f"{table_name}: {len(df)} rows"
            )

        print("Analytics loading completed successfully.")

    finally:
        conn.close()


if __name__ == "__main__":
    load_analytics()
