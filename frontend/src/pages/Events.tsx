import { useEffect, useState } from "react";
import { EventDto, listEvents } from "../api/events";

export default function Events() {
  const [rows, setRows] = useState<EventDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);

  const [agentId, setAgentId] = useState("");
  const [eventType, setEventType] = useState("");
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");

  const load = async () => {
    setLoading(true);
    try {
      const { data } = await listEvents({
        agentId: agentId || undefined,
        eventType: eventType || undefined,
        from: from || undefined,
        to: to || undefined
      });
      setRows(data);
      setErr(null);
    } catch (e: any) {
      setErr(e?.response?.data ?? "Load failed");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []); // initial

  return (
    <div className="space-y-4">
      <h1 className="text-lg font-semibold">Events</h1>

      <div className="flex flex-wrap gap-2 items-end">
        <input className="border px-3 py-2 rounded" placeholder="AgentId (optional)"
               value={agentId} onChange={(e)=>setAgentId(e.target.value)} />
        <select className="border px-3 py-2 rounded" value={eventType}
                onChange={(e)=>setEventType(e.target.value)}>
          <option value="">All types</option>
          <option value="created">created</option>
          <option value="modified">modified</option>
          <option value="deleted">deleted</option>
        </select>
        <input type="datetime-local" className="border px-3 py-2 rounded" value={from}
               onChange={(e)=>setFrom(e.target.value)} />
        <input type="datetime-local" className="border px-3 py-2 rounded" value={to}
               onChange={(e)=>setTo(e.target.value)} />
        <button onClick={load} className="px-3 py-2 rounded bg-black text-white">Filter</button>
      </div>

      {loading ? <div>Loading…</div> :
       err ? <div className="text-red-600">{err}</div> :
       <table className="w-full text-sm border">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 text-left">Time</th>
            <th className="p-2 text-left">Agent</th>
            <th className="p-2 text-left">Type</th>
            <th className="p-2 text-left">Path</th>
          </tr>
        </thead>
        <tbody>
          {rows.map(e => (
            <tr key={e.id} className="border-t">
              <td className="p-2">{new Date(e.timestamp).toLocaleString()}</td>
              <td className="p-2">{e.agentId}</td>
              <td className="p-2">{e.eventType}</td>
              <td className="p-2 font-mono">{e.filePath}</td>
            </tr>
          ))}
          {!rows.length && <tr><td className="p-2 text-gray-500" colSpan={4}>No events.</td></tr>}
        </tbody>
       </table>}
    </div>
  );
}
