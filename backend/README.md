# Inventory Demand Shock & Supplier Lead-Time Stress Tester — Backend

Spring Boot 3.4.5 / Java 17 / Maven / MySQL backend foundation.

## Included layers
- `entity`: 14 JPA entity classes
- `repository`: Spring Data JPA repositories
- `service`: service classes and common CRUD service
- `controller`: REST endpoints
- `exception`: global exception handling
- `dto`: API health response DTO example (resource controllers currently use entity payloads; expand to request/response DTOs before production)

## Entities included
1. Role
2. AppUser
3. Product
4. Supplier
5. SupplierProduct
6. Inventory
7. InventoryTransaction
8. DemandRecord
9. InventoryPolicy
10. Scenario
11. SimulationRun
12. SimulationResult
13. PurchaseOrder
14. PurchaseOrderItem

## Prerequisites
- JDK 17 or newer
- Maven 3.9+
- MySQL 8+
- IntelliJ IDEA / Eclipse / VS Code (optional)
- Postman (recommended for API testing)

## 1. Create the database
Open MySQL Workbench or MySQL command line and run:

```sql
CREATE DATABASE inventory_stress_tester;
```

## 2. Configure database credentials
Open `src/main/resources/application.properties`.

Default values are:
- database: `inventory_stress_tester`
- username: `root`
- password: `root`

Change the password to your local MySQL password, or set environment variables:

Windows PowerShell:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/inventory_stress_tester?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```

macOS/Linux:
```bash
export DB_URL="jdbc:mysql://localhost:3306/inventory_stress_tester?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```

## 3. Run the backend

From the project root, run:

```bash
mvn spring-boot:run
```

This ZIP does not include Maven Wrapper scripts. Install Maven 3.9+ and confirm it is available with `mvn -version` before running the command.

The API will start at:
`http://localhost:8080`

Hibernate is configured with `ddl-auto=update` for local development and will create/update the tables from the entity mappings.

## 4. Verify that the backend is running

Open:
`GET http://localhost:8080/api/health`

Expected response:
```json
{
  "application": "Inventory Demand Shock & Supplier Lead-Time Stress Tester",
  "status": "UP",
  "message": "Spring Boot backend is running"
}
```

## 5. REST API endpoints

All resource endpoints support:
- `GET /api/{resource}` — list all
- `GET /api/{resource}/{id}` — get by ID
- `POST /api/{resource}` — create
- `PUT /api/{resource}/{id}` — update
- `DELETE /api/{resource}/{id}` — delete

| Resource | Base endpoint |
|---|---|
| Roles | `/api/roles` |
| Users | `/api/users` |
| Products | `/api/products` |
| Suppliers | `/api/suppliers` |
| Supplier-product mappings | `/api/supplier-products` |
| Inventory | `/api/inventory` |
| Inventory transactions | `/api/inventory-transactions` |
| Demand records | `/api/demand-records` |
| Inventory policies | `/api/policies` |
| Scenarios | `/api/scenarios` |
| Simulation runs | `/api/simulations` |
| Simulation results | `/api/simulation-results` |
| Purchase orders | `/api/purchase-orders` |
| Purchase order items | `/api/purchase-order-items` |

Additional endpoint:
- `GET /api/health`

## 6. Example: create a product

`POST http://localhost:8080/api/products`

```json
{
  "skuCode": "LAP001",
  "productName": "Laptop",
  "category": "Electronics",
  "unitCost": 40000.00,
  "sellingPrice": 48000.00,
  "holdingCostPerUnitDay": 50.00,
  "stockoutCostPerUnit": 500.00,
  "active": true
}
```

Then test:
`GET http://localhost:8080/api/products`

## 7. Example: create a supplier

`POST http://localhost:8080/api/suppliers`

```json
{
  "supplierName": "ABC Electronics Supplier",
  "contactEmail": "supplier@example.com",
  "contactNumber": "9000000000",
  "defaultLeadTimeDays": 5,
  "leadTimeStdDevDays": 1.5,
  "supplierRating": 4.2,
  "active": true
}
```

## Important implementation notes

This ZIP provides the initial backend foundation and generic CRUD endpoints for all 14 entities. It is not yet the finished business application. The user entity endpoint is provisional: the passwordHash field is write-only in JSON, but secure registration/login and BCrypt password hashing have not yet been implemented. Do not use it for real accounts.

Before production use, the project should be extended with:
- Request/response DTOs for every resource and explicit validation annotations.
- Authentication and authorization (for example Spring Security + JWT), password hashing, and role-based access.
- Business-specific services for stock adjustments, purchase-order receiving, reorder calculations, CSV import, and policy comparison.
- The Python simulation service and a dedicated simulation orchestration workflow.
- Simulation input snapshots, order-arrival schedules, and reproducibility checks.
- Database migrations (Flyway) instead of relying on Hibernate schema updates.
- Pagination, filtering, audit controls, and integration tests.

For safety, do not expose this development configuration publicly. Do not commit database passwords or secrets to Git.
