import { X } from "lucide-react";

export default function Modal({ title, children, onClose, wide = false }) {
  return <div className="modal-backdrop" onMouseDown={e => e.target === e.currentTarget && onClose()}>
    <section className={`modal ${wide ? "modal-wide" : ""}`}>
      <header className="modal-header"><h2>{title}</h2><button className="icon-button" onClick={onClose}><X size={19}/></button></header>
      {children}
    </section>
  </div>;
}
