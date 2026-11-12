import { useCallback, useEffect, useMemo, useState } from "react";
import { Plus, RefreshCw, Search, Pencil, Trash2, Eye, Database, AlertTriangle } from "lucide-react";
import api, { getApiError } from "../api/client";
import { resources, resourceOrder, displayValue, getRecordTitle } from "../data/resources";
import Modal from "./Modal";
import FieldInput from "./FieldInput";

function validate(fields, values) {
  const errors = {};
  for (const field of fields) {
    const value = values[field.name];
    if (field.required && (value === "" || value === null || value === undefined)) {
      errors[field.name] = `${field.label} is required.`; continue;
    }
    if (value === "" || value === null || value === undefined) continue;
    if (field.type === "email" && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) errors[field.name] = "Enter a valid email address.";
    if (field.type === "number") {
      const number = Number(value);
      if (!Number.isFinite(number)) errors[field.name] = "Enter a valid number.";
      else if (field.min !== undefined && number < field.min) errors[field.name] = `Minimum value is ${field.min}.`;
      else if (field.max !== undefined && number > field.max) errors[field.name] = `Maximum value is ${field.max}.`;
    }
    if (field.maxLength && String(value).length > field.maxLength) errors[field.name] = `Maximum ${field.maxLength} characters.`;
    if (field.minLength && String(value).length < field.minLength) errors[field.name] = `Minimum ${field.minLength} characters.`;
  }
  return errors;
}

function toPayload(fields, values) {
  const body = {};
  fields.forEach(field => {
    const value = values[field.name];
    if (value === "" || value === undefined) { body[field.name] = null; return; }
    if (field.type === "relation") body[field.name] = value ? { id: Number(value) } : null;
    else if (field.type === "number") body[field.name] = value === null ? null : Number(value);
    else body[field.name] = value;
  });
  return body;
}

export default function CrudPage({ resourceKey, notify, readOnly = false }) {
  const config = resources[resourceKey];
  const isReadOnly = readOnly || Boolean(config.readOnly);
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const [modal, setModal] = useState(null);
  const [values, setValues] = useState({});
  const [fieldErrors, setFieldErrors] = useState({});
  const [saving, setSaving] = useState(false);
  const [detail, setDetail] = useState(null);

  const load = useCallback(async () => {
    setLoading(true); setError("");
    try {
      const response = await api.get(config.endpoint);
      setRows(Array.isArray(response.data) ? response.data : []);
    } catch (e) { setError(getApiError(e)); }
    finally { setLoading(false); }
  }, [config.endpoint]);

  useEffect(() => { load(); }, [load]);

  const filtered = useMemo(() => rows.filter(row => JSON.stringify(row).toLowerCase().includes(search.toLowerCase())), [rows, search]);
  const columns = useMemo(() => {
    if (!rows.length) return ["id", ...config.fields.slice(0, 4).map(f => f.name)];
    return ["id", ...Object.keys(rows[0]).filter(k => !["createdAt", "updatedAt", "passwordHash"].includes(k)).slice(0, 6)];
  }, [rows, config.fields]);

  const openCreate = () => {
    const initial = {};
    config.fields.forEach(f => { if (f.default !== undefined) initial[f.name] = f.default; });
    setValues(initial); setFieldErrors({}); setModal({ mode: "create" });
  };
  const openEdit = row => {
    const initial = {};
    config.fields.forEach(f => {
      const value = row[f.name];
      initial[f.name] = f.type === "relation" ? (value?.id ?? "") :
        (f.type === "datetime" && value ? String(value).slice(0, 16) : value ?? "");
    });
    setValues(initial); setFieldErrors({}); setModal({ mode: "edit", row });
  };
  const save = async e => {
    e.preventDefault();
    if (isReadOnly) return;
    const errors = validate(config.fields, values);
    setFieldErrors(errors);
    if (Object.keys(errors).length) { notify("error", "Please correct the highlighted fields."); return; }
    setSaving(true);
    try {
      const payload = toPayload(config.fields, values);
      if (modal.mode === "edit") await api.put(`${config.endpoint}/${modal.row.id}`, payload);
      else await api.post(config.endpoint, payload);
      notify("success", `${config.singular} ${modal.mode === "edit" ? "updated" : "created"} successfully.`);
      setModal(null); await load();
    } catch (e) { notify("error", getApiError(e)); }
    finally { setSaving(false); }
  };
  const remove = async row => {
    if (isReadOnly) return;
    if (!window.confirm(`Delete ${getRecordTitle(row, resourceKey)}? This action cannot be undone.`)) return;
    try { await api.delete(`${config.endpoint}/${row.id}`); notify("success", "Record deleted."); await load(); }
    catch (e) { notify("error", getApiError(e)); }
  };
  const openDetail = async row => {
    try { const res = await api.get(`${config.endpoint}/${row.id}`); setDetail(res.data); }
    catch { setDetail(row); }
  };

  return <div className="page-content">
    <div className="page-heading">
      <div><div className="eyebrow">DATA MANAGEMENT</div><h1>{config.label}</h1><p>Manage {config.label.toLowerCase()} stored in your Spring Boot backend.</p></div>
      <div className="heading-actions"><button className="button secondary" onClick={load}><RefreshCw size={16}/> Refresh</button>
        {!isReadOnly && <button className="button primary" onClick={openCreate}><Plus size={17}/> Add {config.singular}</button>}</div>
    </div>
    <div className="toolbar"><div className="search-box"><Search size={17}/><input value={search} onChange={e => setSearch(e.target.value)} placeholder={`Search ${config.label.toLowerCase()}…`}/></div><span className="record-count">{filtered.length} records</span></div>
    {error && <div className="error-panel"><AlertTriangle size={19}/><div><strong>Could not load {config.label.toLowerCase()}</strong><p>{error}</p><button className="text-button" onClick={load}>Try again</button></div></div>}
    <div className="table-card">
      {loading ? <div className="empty-state"><span className="loader"/><p>Loading records…</p></div> :
       !filtered.length ? <div className="empty-state"><Database size={30}/><strong>No records found</strong><p>{search ? "Try another search term." : "Create your first record to get started."}</p></div> :
       <div className="table-scroll"><table><thead><tr>{columns.map(col => <th key={col}>{col.replace(/([A-Z])/g, " $1")}</th>)}<th className="actions-col">Actions</th></tr></thead>
         <tbody>{filtered.map(row => <tr key={row.id}>{columns.map(col => <td key={col}>{col === "id" ? <span className="id-pill">#{row[col]}</span> : <span className="cell-value">{displayValue(row[col])}</span>}</td>)}
           <td><div className="row-actions"><button title="View" onClick={() => openDetail(row)}><Eye size={16}/></button>{!isReadOnly && <><button title="Edit" onClick={() => openEdit(row)}><Pencil size={16}/></button><button className="danger-action" title="Delete" onClick={() => remove(row)}><Trash2 size={16}/></button></>}</div></td>
         </tr>)}</tbody></table></div>}
    </div>
    {modal && <Modal title={`${modal.mode === "edit" ? "Edit" : "Add"} ${config.singular}`} onClose={() => setModal(null)}>
      <form className="entity-form" onSubmit={save} noValidate>
        <div className="form-grid">{config.fields.map(field => <FieldInput key={field.name} field={field} value={values[field.name] ?? ""} error={fieldErrors[field.name]} onChange={value => setValues(old => ({ ...old, [field.name]: value }))}/>)}</div>
        <div className="form-footer"><button type="button" className="button secondary" onClick={() => setModal(null)}>Cancel</button><button className="button primary" disabled={saving}>{saving ? "Saving…" : "Save record"}</button></div>
      </form>
    </Modal>}
    {detail && <Modal title={`${config.singular} details`} onClose={() => setDetail(null)} wide>
      <pre className="json-view">{JSON.stringify(detail, null, 2)}</pre>
      <div className="form-footer"><button className="button secondary" onClick={() => setDetail(null)}>Close</button></div>
    </Modal>}
  </div>;
}
