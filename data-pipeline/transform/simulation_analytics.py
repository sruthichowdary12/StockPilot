"""Export simulation results without assuming an undocumented metric schema."""
import pandas as pd

def build_simulation_dataset(tables):
    results = tables.get('simulation_results')
    if results is None:
        return pd.DataFrame()
    # Keep all source columns; metric definitions should be validated against Java.
    return results.copy()
