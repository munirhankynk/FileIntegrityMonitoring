import { useEffect, useState } from "react";
import { EventDto, listEvents } from "../api/events";

export default function Events() {
  const [items, setItems] = useState<EventDto[]>([]);
  const [filters, setFilters] = useState({ agentId: "", eventType: "", from: "", to: "" });
  const [loading, setLoading] = useState(false);

  const load = async () => {
    setLoading(true);
    const { data } = await listEvents({
      agentId: filters.agentId || undefined,
      eventType: filters.eventType || undefined,
      from: filters.from || undefined,
      to: filters.to || undefined,
    });
    setItems(data);
    setLoading(false);
  };

  useEffect(() => { load(); }, []);

  return (
    <div className="space-y-4">
      <h1 className="text-xl font-semibold">Events</h1>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-2">
        <input
          className="border rounded-xl px-3 py-2"
          placeholder="Agent ID"
          value={filters.agentId}
          onChange={(e)=>setFilters({...filters, agentId:e.target.value})}
        />
        <select
          className="border rounded-xl px-3 py-2"
          value={filters.eventType}
          onChange={(e)=>setFilters({...filters, eventType:e.target.value})}
        >
          <option value="">All types</option>
          <option value="created">created</option>
          <option value="modified">modified</option>
          <option value="deleted">deleted</option>
        </select>
        <input
          type="datetime-local"
          className="border rounded-xl px-3 py-2"
          value={filters.from}
          onChange={(e)=>setFilters({...filters, from:e.target.value})}
        />
        <input
          type="datetime-local"
          className="border rounded-xl px-3 py-2"
          value={filters.to}
          onChange={(e)=>setFilters({...filters, to:e.target.value})}
        />
      </div>

      <div className="flex gap-2">
        <button onClick={load} className="px-3 py-2 rounded-xl border" disabled={loading}>
          {loading ? "Loading…" : "Apply"}
        </button>
        <button
          onClick={()=>{ setFilters({ agentId:"", eventType:"", from:"", to:"" }); load(); }}
          className="px-3 py-2 rounded-xl border"
        >
          Reset
        </button>
      </div>

      <div className="bg-white rounded-2xl border overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-left border-b">
              <th className="px-3 py-2">Time</th>
              <th className="px-3 py-2">Agent</th>
              <th className="px-3 py-2">Type</th>
              <th className="px-3 py-2">Path</th>
            </tr>
          </thead>
          <tbody>
            {items.map(e => (
              <tr key={e.id} className="border-t">
                <td className="px-3 py-2">{new Date(e.timestamp).toLocaleString()}</td>
                <td className="px-3 py-2 font-mono text-xs">{e.agentId}</td>
                <td className="px-3 py-2">{e.eventType}</td>
                <td className="px-3 py-2 font-mono text-xs">{e.path}</td>
              </tr>
            ))}
            {items.length === 0 && (
              <tr><td className="px-3 py-3 text-gray-500" colSpan={4}>No events.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
