# Inventory Demand Shock & Supplier Lead-Time Stress Tester — React Frontend

A React + Vite frontend for the Spring Boot backend. It includes a live overview, CRUD screens for the entities in the supplied backend ZIP, client-side form validation, API error handling, and a simulation workbench.

## Requirements

- Node.js 18.18+ (Node 20 LTS recommended)
- npm
- Spring Boot backend running on `http://localhost:8080`
- MySQL database configured by the backend

## Run in VS Code

1. Extract this ZIP.
2. Open the extracted `inventory-stress-tester-react` folder in VS Code.
3. Open Terminal → New Terminal.
4. Install packages:

   ```bash
   npm install
   ```

5. Copy `.env.example` to `.env` if you want to configure the API URL. The default is `http://localhost:8080/api`.
6. Start Spring Boot in Eclipse.
7. Start React:

   ```bash
   npm run dev
   ```

8. Open **http://localhost:3000**.

The Vite development server intentionally uses port **3000** because the supplied Spring Boot controllers currently allow CORS from `http://localhost:3000`.

## Included screens

- Overview: backend health check and live counts for products, suppliers, inventory, and scenarios.
- Master data: roles, users, products, suppliers, supplier products.
- Operations: inventory, transactions, demand records, inventory policies, scenarios, purchase orders, purchase order items.
- Simulation workbench: inventory calculation, run simulation, compare policies, Monte Carlo analysis, supplier risk analysis.
- Simulation runs and results: read-only list/detail views through the existing CRUD endpoints.

## API endpoints used

### Health
- `GET /api/health`

### CRUD resources
Each resource supports `GET /api/{resource}`, `GET /api/{resource}/{id}`, `POST /api/{resource}`, `PUT /api/{resource}/{id}`, and `DELETE /api/{resource}/{id}` where supported by the backend.

| Frontend section | API base |
|---|---|
| Roles | `/api/roles` |
| Users | `/api/users` |
| Products | `/api/products` |
| Suppliers | `/api/suppliers` |
| Supplier products | `/api/supplier-products` |
| Inventory | `/api/inventory` |
| Inventory transactions | `/api/inventory-transactions` |
| Demand records | `/api/demand-records` |
| Policies | `/api/policies` |
| Scenarios | `/api/scenarios` |
| Simulation runs | `/api/simulations` |
| Simulation results | `/api/simulation-results` |
| Purchase orders | `/api/purchase-orders` |
| Purchase order items | `/api/purchase-order-items` |

### Advanced simulation endpoints expected by the workbench

The workbench calls these endpoints, which were part of the planned calculation/simulation layer:

- `POST /api/calculations/inventory`
- `POST /api/simulation-engine/run`
- `POST /api/simulation-engine/compare`
- `POST /api/simulation-engine/monte-carlo`
- `GET /api/analysis/suppliers/{supplierId}/risk`

**Important backend compatibility note:** The backend ZIP supplied for this frontend contains the CRUD controllers and `/api/health`. It does not contain the five advanced controllers above. The corresponding workbench controls are included, but they will return a 404 until those controllers are added to your Spring Boot project. This frontend does not fabricate successful simulation responses.

## Validation and error handling

- Required fields are checked before POST/PUT.
- Email format is checked in the browser.
- Numeric minimum/maximum rules are checked before submission.
- String maximum/minimum lengths are checked.
- Relationship selectors load existing roles, products, suppliers, users, and purchase orders from the backend.
- HTTP errors display the backend message when available.
- Connection failures display a message asking you to check Spring Boot and the API URL.
- Delete actions require confirmation.
- The UI refreshes the resource list after successful create, update, and delete actions.

The frontend validations improve usability. Spring Boot/Jakarta Bean Validation and database constraints remain the authoritative validation layer.

## Important notes

1. The supplied backend CORS configuration permits `http://localhost:3000`; do not change the frontend port unless you also update backend CORS.
2. `passwordHash` is write-only in the backend. The UI never displays it in the table or JSON detail response. The create/edit form sends the value to the existing users API. For a real deployment, do not treat this as a production authentication flow; implement password hashing and authentication securely in Spring Boot.
3. Relationship payloads are sent as objects containing an `id`, for example `{ "product": { "id": 1 } }`, which matches the JPA relationship shape.
4. Simulation runs and simulation results are displayed as read-only because these records should normally be created by the simulation engine.
5. The generic CRUD endpoints return entity JSON. If a JPA relationship causes a serialization error or recursive response, fix the backend JSON serialization/DTO layer rather than hiding the error in React.

## Production build

```bash
npm run build
```

The compiled frontend is generated in `dist/`.

## Project structure

```text
src/
  api/client.js
  components/
    CrudPage.jsx
    FieldInput.jsx
    Modal.jsx
    Toast.jsx
  data/resources.js
  pages/
    Overview.jsx
    SimulationWorkbench.jsx
  App.jsx
  main.jsx
  styles.css
```
