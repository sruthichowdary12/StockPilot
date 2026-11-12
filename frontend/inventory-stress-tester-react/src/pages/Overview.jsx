import { useEffect, useState } from "react";
import { Activity, Boxes, Truck, ClipboardList, AlertTriangle, ArrowUpRight, RefreshCw, Users, ShoppingCart, BarChart3, ShieldCheck } from "lucide-react";
import api, { getApiError } from "../api/client";

const configs = {
  ADMIN: {
    eyebrow: "SYSTEM ADMINISTRATION", title: "Administrator dashboard", subtitle: "Monitor the platform and manage access to StockPilot.",
    stats: [
      { label: "Registered users", url: "/admin/users", icon: Users, page: "adminUsers" },
      { label: "Products", url: "/products", icon: Boxes, page: "products" },
      { label: "Suppliers", url: "/suppliers", icon: Truck, page: "suppliers" },
      { label: "Simulation runs", url: "/simulation-runs", icon: Activity, page: "simulations" }
    ],
    actions: [ ["Manage users", "adminUsers", "Assign roles and review accounts"], ["Products", "products", "Manage the product catalog"], ["Suppliers", "suppliers", "Review supplier records"], ["Simulation workbench", "simulation", "Review stress-testing workflows"] ]
  },
  INVENTORY_MANAGER: {
    eyebrow: "INVENTORY OPERATIONS", title: "Inventory manager dashboard", subtitle: "Focus on stock, suppliers, replenishment and purchase orders.",
    stats: [
      { label: "Inventory records", url: "/inventory", icon: ClipboardList, page: "inventory" },
      { label: "Products", url: "/products", icon: Boxes, page: "products" },
      { label: "Suppliers", url: "/suppliers", icon: Truck, page: "suppliers" },
      { label: "Purchase orders", url: "/purchase-orders", icon: ShoppingCart, page: "purchaseOrders" }
    ],
    actions: [ ["Inventory", "inventory", "Review and update stock records"], ["Purchase orders", "purchaseOrders", "Manage replenishment"], ["Inventory policies", "policies", "Review reorder settings"], ["Suppliers", "suppliers", "Maintain supplier information"] ]
  },
  ANALYST: {
    eyebrow: "RISK & SIMULATION ANALYTICS", title: "Analyst dashboard", subtitle: "Study demand shocks, supplier delays and inventory performance.",
    stats: [
      { label: "Simulation runs", url: "/simulation-runs", icon: Activity, page: "simulations" },
      { label: "Simulation results", url: "/simulation-results", icon: BarChart3, page: "simulationResults" },
      { label: "Scenarios", url: "/scenarios", icon: AlertTriangle, page: "scenarios" },
      { label: "Demand records", url: "/demand-records", icon: ClipboardList, page: "demandRecords" }
    ],
    actions: [ ["Simulation workbench", "simulation", "Run stress tests and Monte Carlo analysis"], ["Simulation results", "simulationResults", "Review simulation outcomes"], ["Scenarios", "scenarios", "Explore demand and lead-time assumptions"], ["Demand records", "demandRecords", "Inspect historical demand"] ]
  }
};

export default function Overview({ onNavigate, role = "ANALYST" }) {
  const config = configs[role] || configs.ANALYST;
  const [counts, setCounts] = useState({});
  const [busy, setBusy] = useState(true);
  const [error, setError] = useState("");
  const [roleCounts, setRoleCounts] = useState({});

  async function refresh() {
    setBusy(true); setError("");
    const results = await Promise.allSettled(config.stats.map(s => api.get(s.url)));
    const next = {};
    results.forEach((r, i) => {
      const item = config.stats[i];
      const data = r.status === "fulfilled" ? r.value.data : null;
      next[item.label] = Array.isArray(data) ? data.length : Array.isArray(data?.content) ? data.content.length : "—";
      if (role === "ADMIN" && item.label === "Registered users" && Array.isArray(data)) {
        const groups = {};
        data.forEach(u => { const key = u.role || "Unassigned"; groups[key] = (groups[key] || 0) + 1; });
        setRoleCounts(groups);
      }
    });
    setCounts(next);
    if (results.every(r => r.status === "rejected")) setError(getApiError(results[0].reason));
    setBusy(false);
  }

  useEffect(() => { refresh(); }, [role]);

  return <div className="page-content">
    <div className="page-heading"><div><div className="eyebrow">{config.eyebrow}</div><h1>{config.title}</h1><p>{config.subtitle}</p></div><button className="button secondary" onClick={refresh} disabled={busy}><RefreshCw size={16}/> Refresh data</button></div>
    {error && <div className="error-panel"><AlertTriangle size={19}/><div><strong>Unable to load dashboard data</strong><p>{error}</p></div></div>}
    <div className="stat-grid">{config.stats.map(({label, icon: Icon, page}) => <button className="stat-card" key={label} onClick={() => onNavigate(page)}><div className="stat-top"><span>{label}</span><Icon size={19}/></div><strong>{busy ? "…" : counts[label] ?? "—"}</strong><small>View records <ArrowUpRight size={13}/></small></button>)}</div>
    <div className="overview-grid">
      <section className="panel quick-panel"><div className="panel-heading"><div><h2>Quick actions</h2><p>Common tasks for your role</p></div></div>{config.actions.map(([label,key,desc]) => <button className="quick-link" key={key} onClick={() => onNavigate(key)}><span><b>{label}</b><small>{desc}</small></span><ArrowUpRight size={17}/></button>)}</section>
      <section className="panel quick-panel"><div className="panel-heading"><div><h2>{role === "ADMIN" ? "User role distribution" : role === "INVENTORY_MANAGER" ? "Operational focus" : "Analysis focus"}</h2><p>{role === "ADMIN" ? "Accounts by assigned role" : "Your primary responsibilities"}</p></div>{role === "ADMIN" ? <ShieldCheck size={19}/> : <Activity size={19}/>}</div>
        {role === "ADMIN" ? (Object.keys(roleCounts).length ? Object.entries(roleCounts).map(([name,count]) => <div className="quick-link" key={name}><span><b>{name}</b><small>Registered accounts</small></span><strong>{count}</strong></div>) : <p style={{padding:16}}>No role information available.</p>) : (role === "INVENTORY_MANAGER" ? ["Monitor inventory levels", "Manage suppliers and purchase orders", "Maintain reorder policies"] : ["Run demand shock simulations", "Compare inventory policies", "Review service levels and stockout risk"]).map(item => <div className="quick-link" key={item}><span><b>{item}</b></span></div>)}
      </section>
    </div>
    <p className="footnote">Record counts are loaded from the backend. A dash indicates an endpoint did not return a list; it is not a zero count.</p>
  </div>;
}
