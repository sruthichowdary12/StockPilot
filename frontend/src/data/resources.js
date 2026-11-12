// Field definitions mirror the Java entity properties in the supplied Spring Boot project.
// Relationship fields are sent as { id: Number(value) } to match JPA @ManyToOne associations.
export const resources = {
  roles: {
    label: "Roles", endpoint: "/roles", singular: "Role",
    fields: [{ name: "roleName", label: "Role name", required: true, maxLength: 40 }]
  },
  users: {
    label: "Users", endpoint: "/users", singular: "User",
    fields: [
      { name: "name", label: "Full name", required: true, maxLength: 100 },
      { name: "email", label: "Email", type: "email", required: true, maxLength: 150 },
      { name: "passwordHash", label: "Password / hash", type: "password", required: true, minLength: 6 },
      { name: "role", label: "Role", type: "relation", resource: "roles" }
    ]
  },
  products: {
    label: "Products", endpoint: "/products", singular: "Product",
    fields: [
      { name: "skuCode", label: "SKU code", required: true, maxLength: 60 },
      { name: "productName", label: "Product name", required: true, maxLength: 150 },
      { name: "category", label: "Category", maxLength: 100 },
      { name: "unitCost", label: "Unit cost", type: "number", min: 0, step: "0.01" },
      { name: "sellingPrice", label: "Selling price", type: "number", min: 0, step: "0.01" },
      { name: "holdingCostPerUnitDay", label: "Holding cost / unit / day", type: "number", min: 0, step: "0.01" },
      { name: "stockoutCostPerUnit", label: "Stockout cost / unit", type: "number", min: 0, step: "0.01" },
      { name: "active", label: "Active", type: "boolean", default: true }
    ]
  },
  suppliers: {
    label: "Suppliers", endpoint: "/suppliers", singular: "Supplier",
    fields: [
      { name: "supplierName", label: "Supplier name", required: true, maxLength: 150 },
      { name: "contactEmail", label: "Contact email", type: "email", maxLength: 150 },
      { name: "contactNumber", label: "Contact number", maxLength: 40 },
      { name: "defaultLeadTimeDays", label: "Default lead time (days)", type: "number", min: 1 },
      { name: "leadTimeStdDevDays", label: "Lead-time standard deviation", type: "number", min: 0, step: "0.01" },
      { name: "supplierRating", label: "Supplier rating", type: "number", min: 0, max: 5, step: "0.1" },
      { name: "active", label: "Active", type: "boolean", default: true }
    ]
  },
  supplierProducts: {
    label: "Supplier products", endpoint: "/supplier-products", singular: "Supplier product",
    fields: [
      { name: "supplier", label: "Supplier", type: "relation", resource: "suppliers", required: true },
      { name: "product", label: "Product", type: "relation", resource: "products", required: true },
      { name: "minimumOrderQuantity", label: "Minimum order quantity", type: "number", required: true, min: 1 },
      { name: "unitPurchasePrice", label: "Unit purchase price", type: "number", min: 0, step: "0.01" },
      { name: "leadTimeDays", label: "Lead time (days)", type: "number", min: 1 },
      { name: "preferredSupplier", label: "Preferred supplier", type: "boolean", default: false }
    ]
  },
  inventory: {
    label: "Inventory", endpoint: "/inventory", singular: "Inventory record",
    fields: [
      { name: "product", label: "Product", type: "relation", resource: "products", required: true },
      { name: "warehouseCode", label: "Warehouse code", required: true, maxLength: 60 },
      { name: "quantityOnHand", label: "Quantity on hand", type: "number", min: 0, default: 0 },
      { name: "quantityOnOrder", label: "Quantity on order", type: "number", min: 0, default: 0 },
      { name: "backorderQuantity", label: "Backorder quantity", type: "number", min: 0, default: 0 },
      { name: "safetyStock", label: "Safety stock", type: "number", min: 0, default: 0 },
      { name: "reorderPoint", label: "Reorder point", type: "number", min: 0, default: 0 }
    ]
  },
  inventoryTransactions: {
    label: "Inventory transactions", endpoint: "/inventory-transactions", singular: "Inventory transaction",
    fields: [
      { name: "product", label: "Product", type: "relation", resource: "products", required: true },
      { name: "transactionType", label: "Transaction type", type: "select", required: true,
        options: ["PURCHASE_RECEIVED", "CUSTOMER_SALE", "STOCK_ADJUSTMENT", "RETURN", "BACKORDER_FULFILLED"] },
      { name: "quantity", label: "Quantity", type: "number", required: true, min: 1 },
      { name: "transactionDate", label: "Transaction date and time", type: "datetime", required: true },
      { name: "referenceId", label: "Reference ID", maxLength: 100 },
      { name: "remarks", label: "Remarks", maxLength: 500 }
    ]
  },
  demandRecords: {
    label: "Demand records", endpoint: "/demand-records", singular: "Demand record",
    fields: [
      { name: "product", label: "Product", type: "relation", resource: "products", required: true },
      { name: "demandDate", label: "Demand date", type: "date", required: true },
      { name: "demandQuantity", label: "Demand quantity", type: "number", required: true, min: 0 },
      { name: "seasonalityFactor", label: "Seasonality factor", type: "number", min: 0, step: "0.01" }
    ]
  },
  policies: {
    label: "Inventory policies", endpoint: "/policies", singular: "Inventory policy",
    fields: [
      { name: "policyName", label: "Policy name", required: true, maxLength: 120 },
      { name: "policyType", label: "Policy type", type: "select", required: true, options: ["FIXED_REORDER_POINT", "PERIODIC_REVIEW"] },
      { name: "product", label: "Product", type: "relation", resource: "products", required: true },
      { name: "reorderPoint", label: "Reorder point", type: "number", min: 0 },
      { name: "orderQuantity", label: "Order quantity", type: "number", min: 1 },
      { name: "reviewIntervalDays", label: "Review interval (days)", type: "number", min: 1 },
      { name: "targetStockLevel", label: "Target stock level", type: "number", min: 0 },
      { name: "safetyStock", label: "Safety stock", type: "number", min: 0 }
    ]
  },
  scenarios: {
    label: "Scenarios", endpoint: "/scenarios", singular: "Scenario",
    fields: [
      { name: "scenarioName", label: "Scenario name", required: true, maxLength: 150 },
      { name: "description", label: "Description", maxLength: 1000 },
      { name: "demandMultiplier", label: "Demand multiplier", type: "number", required: true, min: 0, step: "0.01", default: 1 },
      { name: "leadTimeMultiplier", label: "Lead-time multiplier", type: "number", required: true, min: 0, step: "0.01", default: 1 },
      { name: "simulationDays", label: "Simulation days", type: "number", required: true, min: 1, default: 30 },
      { name: "randomSeed", label: "Random seed", type: "number" },
      { name: "createdBy", label: "Created by user", type: "relation", resource: "users" }
    ]
  },
  simulations: {
    label: "Simulation runs", endpoint: "/simulations", singular: "Simulation run", readOnly: true,
    fields: []
  },
  simulationResults: {
    label: "Simulation results", endpoint: "/simulation-results", singular: "Simulation result", readOnly: true,
    fields: []
  },
  purchaseOrders: {
    label: "Purchase orders", endpoint: "/purchase-orders", singular: "Purchase order",
    fields: [
      { name: "orderNumber", label: "Order number", required: true, maxLength: 80 },
      { name: "supplier", label: "Supplier", type: "relation", resource: "suppliers", required: true },
      { name: "orderDate", label: "Order date", type: "date" },
      { name: "expectedDeliveryDate", label: "Expected delivery date", type: "date" },
      { name: "actualDeliveryDate", label: "Actual delivery date", type: "date" },
      { name: "status", label: "Status", type: "select", required: true, options: ["DRAFT", "PLACED", "PARTIALLY_RECEIVED", "RECEIVED", "CANCELLED"] }
    ]
  },
  purchaseOrderItems: {
    label: "Purchase order items", endpoint: "/purchase-order-items", singular: "Purchase order item",
    fields: [
      { name: "purchaseOrder", label: "Purchase order", type: "relation", resource: "purchaseOrders", required: true },
      { name: "product", label: "Product", type: "relation", resource: "products", required: true },
      { name: "orderedQuantity", label: "Ordered quantity", type: "number", required: true, min: 1 },
      { name: "receivedQuantity", label: "Received quantity", type: "number", required: true, min: 0, default: 0 },
      { name: "unitPrice", label: "Unit price", type: "number", required: true, min: 0, step: "0.01" }
    ]
  }
};

export const resourceOrder = [
  "products", "suppliers", "inventory", "demandRecords", "policies", "scenarios",
  "supplierProducts", "inventoryTransactions", "purchaseOrders", "purchaseOrderItems",
  "simulations", "simulationResults", "users", "roles"
];

export function displayValue(value) {
  if (value == null) return "—";
  if (typeof value === "object") {
    if (value.productName) return value.productName;
    if (value.supplierName) return value.supplierName;
    if (value.scenarioName) return value.scenarioName;
    if (value.policyName) return value.policyName;
    if (value.orderNumber) return value.orderNumber;
    if (value.name) return value.name;
    if (value.roleName) return value.roleName;
    return value.id ? `#${value.id}` : JSON.stringify(value);
  }
  return String(value);
}

export function getRecordTitle(record, key) {
  const candidates = ["productName", "supplierName", "scenarioName", "policyName", "orderNumber", "name", "roleName", "skuCode"];
  for (const field of candidates) if (record?.[field]) return record[field];
  return `${resources[key]?.singular || "Record"} #${record?.id ?? ""}`;
}
