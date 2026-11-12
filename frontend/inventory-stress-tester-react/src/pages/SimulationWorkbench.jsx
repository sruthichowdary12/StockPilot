import { useEffect, useState } from "react";

import {
  Play,
  RefreshCw,
  BarChart3,
  Activity,
  ChartNoAxesCombined,
  GitCompareArrows,
  AlertTriangle
} from "lucide-react";

import api, { getApiError } from "../api/client";
import Modal from "../components/Modal";

const initial = { productId:"", policyId:"", scenarioId:"", startDate:new Date().toISOString().slice(0,10), simulationDays:30, initialInventory:"", randomSeed:42 };
export default function SimulationWorkbench({ notify }) {
  const [products,setProducts]=useState([]), [policies,setPolicies]=useState([]), [scenarios,setScenarios]=useState([]);
  const [form,setForm]=useState(initial), [loading,setLoading]=useState(false), [result,setResult]=useState(null), [error,setError]=useState("");
  const [comparison,setComparison]=useState(null);
  const [advanced,setAdvanced]=useState({ calc:false, compare:false, monte:false, supplier:false });
  const [calcForm,setCalcForm]=useState({productId:"",leadTimeDays:"",serviceLevelZ:1.65,orderingCostPerOrder:100});
  const [compareForm,setCompareForm]=useState({productId:"",scenarioId:"",policyAId:"",policyBId:"",startDate:initial.startDate,simulationDays:30,randomSeed:42});
  const [monteForm,setMonteForm]=useState({productId:"",policyId:"",scenarioId:"",startDate:initial.startDate,simulationDays:30,initialInventory:"",baseSeed:42,repetitions:100});
  const [supplierId,setSupplierId]=useState("");
  const [calcResult,setCalcResult]=useState(null);
  const [modal,setModal]=useState(null);

  const loadOptions=async()=>{
    setError("");
    try {
      const [p,po,s]=await Promise.all([api.get("/products"),api.get("/policies"),api.get("/scenarios")]);
      setProducts(p.data||[]);setPolicies(po.data||[]);setScenarios(s.data||[]);
    } catch(e){setError(getApiError(e));}
  };
  useEffect(()=>{loadOptions();},[]);
  const update=(key,value)=>setForm(old=>({...old,[key]:value}));
  const submit=async e=>{
    e.preventDefault();
    if(!form.productId||!form.policyId||!form.scenarioId){notify("error","Select a product, policy and scenario.");return;}
    setLoading(true);setError("");setResult(null);
    try {
      const payload={productId:Number(form.productId),policyId:Number(form.policyId),scenarioId:Number(form.scenarioId),startDate:form.startDate,simulationDays:Number(form.simulationDays),initialInventory:form.initialInventory===""?null:Number(form.initialInventory),randomSeed:Number(form.randomSeed)};
      const res=await api.post("/simulation-engine/run",payload);setResult(res.data);notify("success","Simulation completed.");
    } catch(e){setError(getApiError(e));notify("error",getApiError(e));}
    finally{setLoading(false);}
  };
  const calculate=async e=>{
    e.preventDefault();
    try {const res=await api.post("/calculations/inventory",{productId:Number(calcForm.productId),leadTimeDays:calcForm.leadTimeDays===""?null:Number(calcForm.leadTimeDays),serviceLevelZ:Number(calcForm.serviceLevelZ),orderingCostPerOrder:Number(calcForm.orderingCostPerOrder)});setCalcResult(res.data);}
    catch(e){notify("error",getApiError(e));}
  };
  const compare=async e=>{
    e.preventDefault();
    try {const res=await api.post("/simulation-engine/compare",{productId:Number(compareForm.productId),scenarioId:Number(compareForm.scenarioId),policyAId:Number(compareForm.policyAId),policyBId:Number(compareForm.policyBId),startDate:compareForm.startDate,simulationDays:Number(compareForm.simulationDays),randomSeed:Number(compareForm.randomSeed)});setComparison(res.data);}
    catch(e){notify("error",getApiError(e));}
  };
  const monte=async e=>{
    e.preventDefault();
    try {const res=await api.post("/simulation-engine/monte-carlo",{productId:Number(monteForm.productId),policyId:Number(monteForm.policyId),scenarioId:Number(monteForm.scenarioId),startDate:monteForm.startDate,simulationDays:Number(monteForm.simulationDays),initialInventory:monteForm.initialInventory===""?null:Number(monteForm.initialInventory),baseSeed:Number(monteForm.baseSeed),repetitions:Number(monteForm.repetitions)});setModal({title:"Monte Carlo results",data:res.data});}
    catch(e){notify("error",getApiError(e));}
  };
  const supplierRisk=async e=>{
    e.preventDefault();
    try {const res=await api.get(`/analysis/suppliers/${Number(supplierId)}/risk`);setModal({title:"Supplier risk analysis",data:res.data});}
    catch(e){notify("error",getApiError(e));}
  };
  const productsOptions=<><option value="">Select product</option>{products.map(x=><option key={x.id} value={x.id}>{x.productName} ({x.skuCode})</option>)}</>;
  const policyOptions=<><option value="">Select policy</option>{policies.map(x=><option key={x.id} value={x.id}>{x.policyName} · #{x.id}</option>)}</>;
  const scenarioOptions=<><option value="">Select scenario</option>{scenarios.map(x=><option key={x.id} value={x.id}>{x.scenarioName}</option>)}</>;
  const SelectField=({label,value,onChange,children})=><label className="form-field"><span>{label} <i>*</i></span><select required value={value} onChange={e=>onChange(e.target.value)}>{children}</select></label>;
  const NumberField=({label,value,onChange,min=0})=><label className="form-field"><span>{label}</span><input type="number" min={min} value={value} onChange={e=>onChange(e.target.value)}/></label>;
  return <div className="page-content">
    <div className="page-heading"><div><div className="eyebrow">ANALYTICS ENGINE</div><h1>Simulation workbench</h1><p>Run demand and supplier lead-time stress scenarios against inventory policies.</p></div><button className="button secondary" onClick={loadOptions}><RefreshCw size={16}/> Reload options</button></div>
    {error&&<div className="error-panel"><AlertTriangle size={19}/><div><strong>Could not load simulation options or run simulation</strong><p>{error}</p><p>Confirm that the advanced simulation controllers exist in Spring Boot and that products, policies and scenarios are present.</p></div></div>}
    <div className="workbench-grid">
      <section className="panel simulation-panel"><div className="panel-heading"><div><h2><Play size={18}/> Run a simulation</h2><p>Evaluate inventory over a configurable time horizon.</p></div><span className="tag">CORE WORKFLOW</span></div>
        <form onSubmit={submit}><div className="form-grid">
          <SelectField label="Product" value={form.productId} onChange={v=>update("productId",v)}>{productsOptions}</SelectField>
          <SelectField label="Inventory policy" value={form.policyId} onChange={v=>update("policyId",v)}>{policyOptions}</SelectField>
          <SelectField label="Stress scenario" value={form.scenarioId} onChange={v=>update("scenarioId",v)}>{scenarioOptions}</SelectField>
          <label className="form-field"><span>Start date</span><input type="date" required value={form.startDate} onChange={e=>update("startDate",e.target.value)}/></label>
          <NumberField label="Simulation days" min={1} value={form.simulationDays} onChange={v=>update("simulationDays",v)}/>
          <NumberField label="Initial inventory (optional)" value={form.initialInventory} onChange={v=>update("initialInventory",v)}/>
          <NumberField label="Random seed" value={form.randomSeed} onChange={v=>update("randomSeed",v)}/>
        </div><button className="button primary run-button" disabled={loading}>{loading?"Running simulation…":<><Play size={16}/> Run simulation</>}</button></form>
      </section>
      <section className="panel side-panel"><div className="panel-heading"><div><h2>Inventory calculation</h2><p>Calculate planning metrics for a product.</p></div><ChartNoAxesCombined size={19}/></div>
        <form onSubmit={calculate} className="stack-form">
          <label className="form-field"><span>Product <i>*</i></span><select required value={calcForm.productId} onChange={e=>setCalcForm({...calcForm,productId:e.target.value})}>{productsOptions}</select></label>
          <NumberField label="Lead time (days, optional)" value={calcForm.leadTimeDays} onChange={v=>setCalcForm({...calcForm,leadTimeDays:v})}/>
          <NumberField label="Service-level Z" value={calcForm.serviceLevelZ} onChange={v=>setCalcForm({...calcForm,serviceLevelZ:v})}/>
          <NumberField label="Ordering cost per order" value={calcForm.orderingCostPerOrder} onChange={v=>setCalcForm({...calcForm,orderingCostPerOrder:v})}/>
          <button className="button secondary">Calculate metrics</button>
        </form>
        {calcResult&&<pre className="result-json">{JSON.stringify(calcResult,null,2)}</pre>}
      </section>
    </div>
    {result&&<section className="panel result-panel"><div className="panel-heading"><div><h2>Simulation result</h2><p>Returned by POST /simulation-engine/run</p></div><span className="tag success">COMPLETED</span></div><pre className="json-view">{JSON.stringify(result,null,2)}</pre></section>}
    <div className="advanced-grid">
      <section className="panel"><div className="panel-heading"><div><h2><GitCompareArrows size={18}/> Policy comparison</h2><p>Compare two policies under the same scenario.</p></div></div>
        <form onSubmit={compare} className="form-grid">
          <SelectField label="Product" value={compareForm.productId} onChange={v=>setCompareForm({...compareForm,productId:v})}>{productsOptions}</SelectField>
          <SelectField label="Scenario" value={compareForm.scenarioId} onChange={v=>setCompareForm({...compareForm,scenarioId:v})}>{scenarioOptions}</SelectField>
          <SelectField label="Policy A" value={compareForm.policyAId} onChange={v=>setCompareForm({...compareForm,policyAId:v})}>{policyOptions}</SelectField>
          <SelectField label="Policy B" value={compareForm.policyBId} onChange={v=>setCompareForm({...compareForm,policyBId:v})}>{policyOptions}</SelectField>
          <label className="form-field"><span>Start date</span><input type="date" value={compareForm.startDate} onChange={e=>setCompareForm({...compareForm,startDate:e.target.value})}/></label>
          <NumberField label="Simulation days" value={compareForm.simulationDays} onChange={v=>setCompareForm({...compareForm,simulationDays:v})}/>
          <button className="button secondary form-span">Compare policies</button>
        </form>
        {comparison&&<pre className="result-json">{JSON.stringify(comparison,null,2)}</pre>}
      </section>
      <section className="panel"><div className="panel-heading"><div><h2><Activity size={18}/> Monte Carlo analysis</h2><p>Repeat simulations with different random seeds.</p></div></div>
        <form onSubmit={monte} className="form-grid">
          <SelectField label="Product" value={monteForm.productId} onChange={v=>setMonteForm({...monteForm,productId:v})}>{productsOptions}</SelectField>
          <SelectField label="Policy" value={monteForm.policyId} onChange={v=>setMonteForm({...monteForm,policyId:v})}>{policyOptions}</SelectField>
          <SelectField label="Scenario" value={monteForm.scenarioId} onChange={v=>setMonteForm({...monteForm,scenarioId:v})}>{scenarioOptions}</SelectField>
          <NumberField label="Simulation days" value={monteForm.simulationDays} onChange={v=>setMonteForm({...monteForm,simulationDays:v})}/>
          <NumberField label="Repetitions (2–1000)" min={2} value={monteForm.repetitions} onChange={v=>setMonteForm({...monteForm,repetitions:v})}/>
          <NumberField label="Base seed" value={monteForm.baseSeed} onChange={v=>setMonteForm({...monteForm,baseSeed:v})}/>
          <button className="button secondary form-span">Run Monte Carlo</button>
        </form>
      </section>
      <section className="panel supplier-risk"><div className="panel-heading"><div><h2><AlertTriangle size={18}/> Supplier risk</h2><p>Inspect lead-time variability and supplier rating.</p></div></div>
        <form onSubmit={supplierRisk} className="inline-form"><label className="form-field"><span>Supplier ID</span><input type="number" min="1" required value={supplierId} onChange={e=>setSupplierId(e.target.value)} placeholder="e.g. 1"/></label><button className="button secondary">Analyse risk</button></form>
      </section>
    </div>
    {modal&&<Modal title={modal.title} onClose={()=>setModal(null)} wide><pre className="json-view">{JSON.stringify(modal.data,null,2)}</pre><div className="form-footer"><button className="button secondary" onClick={()=>setModal(null)}>Close</button></div></Modal>}
  </div>;
}
