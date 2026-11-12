import { useEffect, useState } from "react";
import { Activity, Boxes, LayoutDashboard, Truck, ClipboardList, PackageSearch, ChartNoAxesCombined, ShoppingCart, FileSpreadsheet, SlidersHorizontal, Users, ShieldCheck, Menu, X, Wifi, WifiOff, ChevronDown } from "lucide-react";
import api from "./api/client";
import Overview from "./pages/Overview";
import CrudPage from "./components/CrudPage";
import SimulationWorkbench from "./pages/SimulationWorkbench";
import Toast from "./components/Toast";
import AuthPage from "./pages/AuthPage";
import AdminUsers from "./pages/AdminUsers";
import { resources } from "./data/resources";

const navGroups = [
  { title:"WORKSPACE", items:[
    {key:"overview",label:"Overview",icon:LayoutDashboard},
    {key:"simulation",label:"Simulation workbench",icon:ChartNoAxesCombined}
  ]},
  { title:"MASTER DATA", items:[
    {key:"products",label:"Products",icon:Boxes},{key:"suppliers",label:"Suppliers",icon:Truck},
    {key:"supplierProducts",label:"Supplier products",icon:PackageSearch},{key:"adminUsers",label:"Manage users",icon:ShieldCheck}
  ]},
  { title:"OPERATIONS", items:[
    {key:"inventory",label:"Inventory",icon:ClipboardList},{key:"inventoryTransactions",label:"Transactions",icon:Activity},
    {key:"demandRecords",label:"Demand records",icon:FileSpreadsheet},{key:"policies",label:"Inventory policies",icon:SlidersHorizontal},
    {key:"scenarios",label:"Scenarios",icon:ChartNoAxesCombined},{key:"purchaseOrders",label:"Purchase orders",icon:ShoppingCart},
    {key:"purchaseOrderItems",label:"Purchase order items",icon:ShoppingCart},{key:"simulations",label:"Simulation runs",icon:Activity},
    {key:"simulationResults",label:"Simulation results",icon:FileSpreadsheet}
  ]}
];

const allowedPages = {
  ADMIN: null, // Admin can access all pages
  INVENTORY_MANAGER: new Set(["overview", "products", "suppliers", "supplierProducts", "inventory", "inventoryTransactions", "policies", "purchaseOrders", "purchaseOrderItems", "simulation", "scenarios", "demandRecords", "simulations", "simulationResults"]),
  ANALYST: new Set(["overview", "simulation", "products", "suppliers", "inventory", "demandRecords", "policies", "scenarios", "simulations", "simulationResults"])
};
const canOpen = (role, key) => role === "ADMIN" || (allowedPages[role]?.has(key) ?? false);

export default function App() {
  const [user,setUser]=useState(null);
  const [checkingAuth,setCheckingAuth]=useState(true);
  useEffect(() => {
    if (!sessionStorage.getItem("stockpilot_token")) { setCheckingAuth(false); return; }
    api.get("/auth/me").then(r=>setUser(r.data)).catch(()=>{
      sessionStorage.removeItem("stockpilot_token");setUser(null);
    }).finally(()=>setCheckingAuth(false));
  }, []);
  const logout=()=>{sessionStorage.removeItem("stockpilot_token");setUser(null);setPage("overview");};
  const [page,setPage]=useState("overview");
  const [collapsed,setCollapsed]=useState(false);
  const [mobileOpen,setMobileOpen]=useState(false);
  const [toast,setToast]=useState(null);
  const [backend,setBackend]=useState(false);
  const notify=(type,message)=>setToast({type,message});
  useEffect(()=>{
    let active=true;
    const ping=()=>api.get("/health").then(()=>active&&setBackend(true)).catch(()=>active&&setBackend(false));
    ping(); const timer=setInterval(ping,15000);
    return()=>{active=false;clearInterval(timer);};
  },[]);
  const activeLabel=page==="overview"?"Overview":page==="simulation"?"Simulation workbench":(page==="adminUsers"?"Manage users":resources[page]?.label)||"Workspace";
  const navigate=key=>{if (canOpen(user?.role,key)) setPage(key);setMobileOpen(false);};
  if (checkingAuth) return <div style={{padding:40}}>Checking session…</div>;
  if (!user) return <AuthPage onAuthenticated={(data)=>{
    sessionStorage.setItem("stockpilot_token",data.token);
    setUser(data.user);
    setPage("overview");
  }}/>
  return <div className={`app-shell ${collapsed?"sidebar-collapsed":""}`}>
    {mobileOpen&&<button className="mobile-scrim" onClick={()=>setMobileOpen(false)} aria-label="Close menu"/>}
    <aside className={`sidebar ${mobileOpen?"mobile-visible":""}`}>
      <div className="brand"><div className="brand-mark"><Activity size={21}/></div><div className="brand-copy"><b>StockPilot</b><span>INVENTORY INTELLIGENCE</span></div><button className="mobile-close icon-button" onClick={()=>setMobileOpen(false)}><X/></button></div>
      <div className="workspace-label">PROJECT WORKSPACE</div>
      <nav>{navGroups.map(group=><div className="nav-group" key={group.title}><div className="nav-caption">{group.title}</div>{group.items.filter(item => canOpen(user.role, item.key)).map(item=><button key={item.key} className={`nav-item ${page===item.key?"active":""}`} onClick={()=>navigate(item.key)} title={item.label}><item.icon size={18}/><span>{item.label}</span></button>)}</div>)}</nav>
      <div className="sidebar-bottom"><div className={`connection-chip ${backend?"connected":"disconnected"}`}>{backend?<Wifi size={15}/>:<WifiOff size={15}/>}<span>{backend?"Backend connected":"Backend offline"}</span></div><div className="sidebar-foot">Inventory Demand Shock<br/> & Supplier Lead-Time Stress Tester</div></div>
    </aside>
    <main className="main-area">
      <header className="topbar"><div className="topbar-left"><button className="icon-button menu-toggle" onClick={()=>setMobileOpen(true)}><Menu/></button><span className="breadcrumb">Workspace <b>/</b> {activeLabel}</span></div><div className="topbar-right"><span className={`backend-indicator ${backend?"good":"bad"}`}><i/>{backend?"API online":"API offline"}</span><span style={{fontSize:12}}>{user.name} ({user.role})</span><button className="button secondary" onClick={logout}>Logout</button><div className="avatar">{user.name?.slice(0,2).toUpperCase()}</div></div></header>
      {page==="overview"?<Overview role={user.role} onNavigate={navigate}/>:page==="simulation"?<SimulationWorkbench notify={notify}/>:page==="adminUsers" && user.role === "ADMIN"?<AdminUsers currentUser={user} notify={notify}/>:canOpen(user.role,page)?<CrudPage key={page} resourceKey={page} notify={notify} readOnly={user.role === "ANALYST"}/>:<div className="page-content"><h2>Access denied</h2><p>Your role cannot access this page.</p></div>}
      <footer className="app-footer"><span>StockPilot <b>·</b> Inventory Stress Tester</span><span>React frontend <b>·</b> Spring Boot REST API</span></footer>
    </main>
    <Toast toast={toast} onClose={()=>setToast(null)}/>
  </div>;
}
