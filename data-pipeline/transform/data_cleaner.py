"""Non-destructive cleaning: preserve source rows and report quality issues."""
import pandas as pd

def clean_tables(tables):
    cleaned = {}
    quality = []
    for name, original in tables.items():
        df = original.copy()
        for column in df.columns:
            if column.lower().endswith(('_at', '_date')) or column.lower() in ('date', 'start_date', 'end_date'):
                # Only convert if all non-null values parse successfully.
                parsed = pd.to_datetime(df[column], errors='coerce')
                if parsed.notna().sum() == df[column].notna().sum():
                    df[column] = parsed
        quality.append({
            'table_name': name,
            'row_count': len(df),
            'column_count': len(df.columns),
            'missing_cells': int(df.isna().sum().sum()),
            'duplicate_rows': int(df.duplicated().sum()) if len(df) else 0,
        })
        cleaned[name] = df
    return cleaned, pd.DataFrame(quality)
