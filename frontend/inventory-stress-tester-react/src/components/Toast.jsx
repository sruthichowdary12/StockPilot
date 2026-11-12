import { useEffect } from "react";
import { CheckCircle2, AlertCircle, X } from "lucide-react";

export default function Toast({ toast, onClose }) {
  useEffect(() => {
    if (!toast) return;
    const timer = setTimeout(onClose, 4500);
    return () => clearTimeout(timer);
  }, [toast, onClose]);

  if (!toast) return null;
  return <div className={`toast ${toast.type || "success"}`}>
    {toast.type === "error" ? <AlertCircle size={18}/> : <CheckCircle2 size={18}/>}
    <span>{toast.message}</span>
    <button className="icon-button" onClick={onClose} aria-label="Close"><X size={16}/></button>
  </div>;
}
