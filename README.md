
# StockPilot

## Inventory Demand Shock & Supplier Lead-Time Stress Tester

StockPilot helps retailers evaluate inventory policies
under demand spikes and supplier delivery delays.

## Technology Stack

- Frontend: React.js, Vite
- Backend: Java 17, Spring Boot, Spring Security, JWT
- Database: MySQL
- Data Engineering: Python, Pandas, NumPy
- Data Warehouse: Snowflake
- Cloud Storage: AWS S3
- CI/CD: GitHub Actions
- Business Intelligence: Power BI (planned)

## Architecture

React → Spring Boot → MySQL → Python ETL

Python ETL → Snowflake

Python ETL → AWS S3

Snowflake → Power BI (planned)

## Project Structure

- backend/ - Spring Boot application
- frontend/ - React application
- data-pipeline/ - Python ETL and integrations

## Features

- Product and supplier management
- Inventory management
- Demand shock simulation
- Supplier lead-time simulation
- Reorder point calculations
- Inventory policy comparison
- Role-based authentication
- Data extraction and transformation
- Snowflake analytics
- AWS S3 CSV storage
