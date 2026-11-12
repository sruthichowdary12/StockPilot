import { useEffect, useState } from "react";
import api from "../api/client";
import { resources, getRecordTitle } from "../data/resources";

export default function FieldInput({ field, value, onChange, error }) {
  const [options, setOptions] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (field.type !== "relation") return;
    let active = true;
    setLoading(true);
    api.get(resources[field.resource].endpoint)
      .then(res => { if (active) setOptions(Array.isArray(res.data) ? res.data : []); })
      .catch(() => { if (active) setOptions([]); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [field.type, field.resource]);

  const common = { id: field.name, name: field.name, value: value ?? "", onChange: e => onChange(e.target.value) };
  return <div className="form-field">
    <label htmlFor={field.name}>{field.label}{field.required && <span className="required"> *</span>}</label>
    {field.type === "boolean" ? (
      <label className="switch-row"><input type="checkbox" checked={Boolean(value)} onChange={e => onChange(e.target.checked)}/><span>{value ? "Enabled" : "Disabled"}</span></label>
    ) : field.type === "relation" ? (
      <select {...common}>
        <option value="">{loading ? "Loading options…" : "Select an option"}</option>
        {options.map(item => <option key={item.id} value={item.id}>{getRecordTitle(item, field.resource)} (#{item.id})</option>)}
      </select>
    ) : field.type === "select" ? (
      <select {...common}><option value="">Select {field.label.toLowerCase()}</option>{field.options.map(option => <option key={option} value={option}>{option.replaceAll("_", " ")}</option>)}</select>
    ) : field.type === "textarea" ? (
      <textarea {...common} rows={3} maxLength={field.maxLength}/>
    ) : (
      <input {...common} type={field.type === "datetime" ? "datetime-local" : field.type || "text"}
        min={field.min} max={field.max} step={field.step} maxLength={field.maxLength}
        minLength={field.minLength} placeholder={field.placeholder || ""}/>
    )}
    {error && <small className="field-error">{error}</small>}
  </div>;
}
