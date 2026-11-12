"""Preserve current inventory as a timestamped reporting snapshot."""
import pandas as pd

def build_inventory_snapshot(tables):
    df = tables.get('inventory')
    if df is None:
        return pd.DataFrame()
    snapshot = df.copy()
    snapshot['extracted_at_utc'] = pd.Timestamp.now(tz='UTC').isoformat()
    return snapshot
