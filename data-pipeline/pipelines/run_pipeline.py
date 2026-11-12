"""Run with: python -m pipelines.run_pipeline [--snowflake]"""
import argparse
from pathlib import Path
from config.database import mysql_engine
from extract.mysql_extractor import extract_tables
from transform.data_cleaner import clean_tables
from transform.demand_analytics import build_demand_summary
from transform.inventory_analytics import build_inventory_snapshot
from transform.simulation_analytics import build_simulation_dataset


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--snowflake', action='store_true', help='Also load extracted RAW tables to Snowflake')
    args = parser.parse_args()
    output = Path('output')
    output.mkdir(exist_ok=True)
    engine = mysql_engine()
    try:
        raw = extract_tables(engine)
    finally:
        engine.dispose()
    tables, quality = clean_tables(raw)
    analytics = {
        'demand_summary': build_demand_summary(tables),
        'inventory_snapshot': build_inventory_snapshot(tables),
        'simulation_dataset': build_simulation_dataset(tables),
    }
    quality.to_csv(output / 'data_quality_report.csv', index=False)
    for name, frame in {**tables, **analytics}.items():
        frame.to_csv(output / f'{name}.csv', index=False)
        print(f'[CSV] {name}: {len(frame)} rows')
    if args.snowflake:
        from load.snowflake_loader import load_frames
        load_frames(tables)
    print('Pipeline completed. Review output/ and data_quality_report.csv.')

if __name__ == '__main__':
    main()
