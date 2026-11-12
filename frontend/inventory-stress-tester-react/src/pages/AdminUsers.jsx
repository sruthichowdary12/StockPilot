import { useEffect, useState } from "react";
import { RefreshCw, ShieldCheck } from "lucide-react";
import api, { getApiError } from "../api/client";

const ROLES = ["ADMIN", "INVENTORY_MANAGER", "ANALYST"];

export default function AdminUsers({ currentUser, notify }) {
  const [users, setUsers] = useState([]);
  const [drafts, setDrafts] = useState({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(null);
  const [error, setError] = useState("");

  async function load() {
    setLoading(true);
    setError("");
    try {
      const { data } = await api.get("/admin/users");
      setUsers(data);
      setDrafts(Object.fromEntries(data.map(u => [u.id, u.role])));
    } catch (e) {
      setError(getApiError(e));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function save(user) {
    const role = drafts[user.id];
    if (!role || role === user.role || saving !== null) return;
    setSaving(user.id);
    setError("");
    try {
      const { data } = await api.patch(`/admin/users/${user.id}/role`, { role });
      setUsers(previous => previous.map(u => u.id === data.id ? data : u));
      notify?.("success", `Updated ${user.name} to ${data.role}`);
    } catch (e) {
      const message = getApiError(e);
      setError(message);
      notify?.("error", message);
    } finally {
      setSaving(null);
    }
  }

  return (
    <div className="page-content">
      <div className="page-heading">
        <div>
          <div className="eyebrow">ADMINISTRATION</div>
          <h1>User role management</h1>
          <p>Assign access roles to registered users. New registrations default to ANALYST.</p>
        </div>
        <button className="button secondary" onClick={load} disabled={loading}>
          <RefreshCw size={16} /> Reload
        </button>
      </div>
      <section className="panel" style={{ padding: 24 }}>
        <h2 style={{ display: "flex", alignItems: "center", gap: 8 }}>
          <ShieldCheck size={20} /> Registered users
        </h2>
        {error && <p role="alert" style={{ color: "#b91c1c" }}>{error}</p>}
        {loading ? <p>Loading users…</p> : users.length === 0 ? <p>No registered users.</p> : (
          <div style={{ overflowX: "auto" }}>
            <table style={{ width: "100%", borderCollapse: "collapse", marginTop: 18 }}>
              <thead>
                <tr style={{ textAlign: "left" }}>
                  <th style={{ padding: 12 }}>Name</th>
                  <th style={{ padding: 12 }}>Email</th>
                  <th style={{ padding: 12 }}>Current role</th>
                  <th style={{ padding: 12 }}>Assign role</th>
                  <th style={{ padding: 12 }}>Action</th>
                </tr>
              </thead>
              <tbody>
                {users.map(user => {
                  const isSelf = user.id === currentUser?.id;
                  return (
                    <tr key={user.id} style={{ borderTop: "1px solid #e5e7eb" }}>
                      <td style={{ padding: 12 }}>{user.name}{isSelf ? " (you)" : ""}</td>
                      <td style={{ padding: 12 }}>{user.email}</td>
                      <td style={{ padding: 12 }}>{user.role || "Unassigned"}</td>
                      <td style={{ padding: 12 }}>
                        <select
                          aria-label={`Role for ${user.name}`}
                          value={drafts[user.id] || ""}
                          disabled={isSelf || saving !== null}
                          onChange={e => setDrafts(prev => ({ ...prev, [user.id]: e.target.value }))}
                        >
                          {ROLES.map(role => <option key={role} value={role}>{role}</option>)}
                        </select>
                      </td>
                      <td style={{ padding: 12 }}>
                        <button className="button secondary" type="button"
                          disabled={isSelf || saving !== null || drafts[user.id] === user.role}
                          onClick={() => save(user)}>
                          {saving === user.id ? "Saving…" : "Save role"}
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}
