
@echo off
setlocal

cd /d "%~dp0"

if not exist "logs" mkdir "logs"

set "PYTHON=%~dp0.venv\Scripts\python.exe"

if not exist "%PYTHON%" (
    echo ERROR: Python virtual environment not found.
    exit /b 1
)

echo ======================================
echo StockPilot ETL Started
echo ======================================

echo [1/3] Running MySQL extraction and Snowflake loading
"%PYTHON%" -m pipelines.run_pipeline --snowflake
if errorlevel 1 goto failed

echo [2/3] Loading Snowflake analytics tables
"%PYTHON%" -m load.analytics_loader
if errorlevel 1 goto failed

echo [3/3] Uploading CSV files to AWS S3
"%PYTHON%" -m load.s3_uploader
if errorlevel 1 goto failed

echo StockPilot ETL completed successfully.
exit /b 0

:failed
echo ERROR: StockPilot ETL failed.
exit /b 1
