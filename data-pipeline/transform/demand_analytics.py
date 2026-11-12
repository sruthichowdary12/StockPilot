"""Create a per-product demand summary if recognizable columns exist."""
import pandas as pd

def build_demand_summary(tables):
    df = tables.get('demand_records')
    if df is None or df.empty:
        return pd.DataFrame()
    product_col = next((c for c in ('product_id', 'productId') if c in df.columns), None)
    qty_col = next((c for c in ('quantity', 'demand_quantity', 'demand', 'units_sold', 'units') if c in df.columns), None)
    if not product_col or not qty_col:
        print('[SKIP] Demand summary: inspect demand_records columns and update mappings in transform/demand_analytics.py')
        return pd.DataFrame()
    working = df[[product_col, qty_col]].copy()
    working[qty_col] = pd.to_numeric(working[qty_col], errors='coerce')
    result = working.groupby(product_col, dropna=False)[qty_col].agg(['count','sum','mean','std']).reset_index()
    return result.rename(columns={product_col:'product_id','count':'observation_count','sum':'total_demand','mean':'average_demand','std':'demand_stddev'}).fillna({'demand_stddev':0})
