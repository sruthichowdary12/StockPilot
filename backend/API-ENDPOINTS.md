# API Endpoint Inventory

The initial scaffold exposes generic CRUD endpoints for each entity. Each resource has:
- GET collection
- GET by ID
- POST create
- PUT update
- DELETE by ID

| Entity | Endpoint |
|---|---|
| Role | `/api/roles` |
| AppUser | `/api/users` |
| Product | `/api/products` |
| Supplier | `/api/suppliers` |
| SupplierProduct | `/api/supplier-products` |
| Inventory | `/api/inventory` |
| InventoryTransaction | `/api/inventory-transactions` |
| DemandRecord | `/api/demand-records` |
| InventoryPolicy | `/api/policies` |
| Scenario | `/api/scenarios` |
| SimulationRun | `/api/simulations` |
| SimulationResult | `/api/simulation-results` |
| PurchaseOrder | `/api/purchase-orders` |
| PurchaseOrderItem | `/api/purchase-order-items` |

Health:
- GET `/api/health`

Total: 70 generic CRUD routes + 1 health route.

Note: The scaffold does not yet implement the previously proposed business-specific 34 endpoint plan as specialized workflows. These CRUD routes are a starting backend surface; business logic and tailored request/response DTOs are added in subsequent development steps.
