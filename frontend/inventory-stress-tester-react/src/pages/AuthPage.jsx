import { useState } from "react";
import api, { getApiError } from "../api/client";

export default function AuthPage({ onAuthenticated }) {
  const [register, setRegister] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  async function submit(e) {
    e.preventDefault();setError("");setLoading(true);
    try {
      const body = register ? { name, email, password } : { email, password };
      const { data } = await api.post(register ? "/auth/register" : "/auth/login", body);
      onAuthenticated(data);
    } catch (err) { setError(getApiError(err)); }
    finally { setLoading(false); }
  }
  return <div style={{minHeight:"100vh",display:"grid",placeItems:"center",padding:24,background:"#0b1120",color:"#f8fafc"}}>
    <div style={{width:"100%",maxWidth:420,padding:32,background:"#172136",border:"1px solid #334155",borderRadius:16}}>
      <h1 style={{marginTop:0}}>StockPilot</h1>
      <p style={{color:"#94a3b8"}}>{register ? "Create an analyst account" : "Sign in to Inventory Intelligence"}</p>
      <form onSubmit={submit} style={{display:"grid",gap:16}}>
        {register && <label style={{display:"grid",gap:6}}>Full name<input required maxLength={100} value={name} onChange={e=>setName(e.target.value)} style={{padding:12,borderRadius:8}} /></label>}
        <label style={{display:"grid",gap:6}}>Email<input type="email" required value={email} onChange={e=>setEmail(e.target.value)} style={{padding:12,borderRadius:8}} /></label>
        <label style={{display:"grid",gap:6}}>Password<input type="password" required minLength={register?8:1} value={password} onChange={e=>setPassword(e.target.value)} style={{padding:12,borderRadius:8}} /></label>
        {error && <p role="alert" style={{color:"#fca5a5",margin:0}}>{error}</p>}
        <button type="submit" disabled={loading} style={{padding:12,border:0,borderRadius:8,background:"#38bdf8",color:"#082f49",fontWeight:700,cursor:"pointer"}}>{loading ? "Please wait…" : register ? "Create account" : "Login"}</button>
      </form>
      <button type="button" onClick={()=>{setRegister(!register);setError("");}} style={{marginTop:16,background:"none",border:0,color:"#7dd3fc",cursor:"pointer"}}>{register ? "Already registered? Login" : "New here? Register"}</button>
    </div>
  </div>;
}