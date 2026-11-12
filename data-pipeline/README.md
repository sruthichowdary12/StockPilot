# StockPilot Data Pipeline (Python → MySQL → Snowflake)

## Purpose
A starter ETL project for the existing StockPilot Spring Boot / React / MySQL application.
It **does not replace the Java simulation engine**. It extracts operational data,
produces a data quality report and CSV analytics, and optionally loads raw tables
into Snowflake. Power BI connects to Snowflake later.

## Install on Windows
1. Install **Python 3.11 or 3.12 (64-bit)** and select **Add Python to PATH**.
2. Use existing **VS Code** with Microsoft Python extension and existing MySQL Server/Workbench.
3. Open this folder in VS Code and run in PowerShell:
   ```powershell
   py -3.12 -m venv .venv
   .\.venv\Scripts\Activate.ps1
   python -m pip install --upgrade pip
   pip install -r requirements.txt
   ```
   If Python 3.12 is unavailable, use `py -3.11` instead.
   In Windows Command Prompt, activate using `.venv\Scripts\activate.bat`.
4. Copy `.env.example` to `.env` and set your real MySQL credentials.
   Recommended: create the read-only MySQL account using `sql/mysql_readonly_user.sql`
   and update its password in `.env`. Never commit `.env`.
5. Ensure MySQL is running and the existing `inventory_stress_tester` database is populated.
6. Run:
   ```powershell
   python -m pipelines.run_pipeline
   ```
7. Inspect CSVs in `output/` and `output/data_quality_report.csv`.

## Optional Snowflake phase
1. Create a Snowflake account, run `sql/snowflake_setup.sql` in a worksheet,
   and configure a least-privilege role with access to the warehouse, database,
   schema and tables.
2. Add Snowflake connection values to `.env`.
3. Run `python -m pipelines.run_pipeline --snowflake`.
4. Validate loaded tables in Snowflake (e.g. `SELECT COUNT(*) FROM RAW_PRODUCTS;`).
5. Install **Power BI Desktop** on Windows and connect to Snowflake when ready.

## Important limitations / adaptation
- The script discovers tables but **column names can differ** between StockPilot versions.
  Check `demand_records` columns; update `transform/demand_analytics.py` if needed.
- Inventory snapshot captures the stock state at extraction time, not historical stock.
- CSV export includes all columns from the listed operational tables. Review before sharing.
- The optional Snowflake loader is a **prototype**: it creates VARCHAR columns and
  truncates/reloads tables. It is not production-grade and should not be scheduled
  until column typing, transactional loading, schema drift, and incremental merges are added.
- Do not load users, roles, credentials, or JWT data into analytics.
- Verify KPIs against the existing Java calculation/simulation formulas.

## Documentation screenshots to take
- VS Code folder tree and Python interpreter; terminal showing dependencies installed.
- MySQL tables and SELECT row counts.
- Pipeline terminal output showing extracted row counts.
- `output/data_quality_report.csv` and demand/inventory CSVs.
- Snowflake database/schema/tables and row counts (after Snowflake phase).
- Power BI model and dashboards (after Power BI phase).
