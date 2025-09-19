import { useEffect, useState } from "react";
import { listAgents, AgentDto } from "../api/agents";
import { Link } from "react-router-dom";

export default function Agents() {
  const [rows, setRows] = useState<AgentDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);

  useEffect(() => {
    (async () => {
      try {
        const { data } = await listAgents();
        setRows(data);
      } catch (e: any) {
        setErr(e?.response?.data ?? "Load failed");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <div>Loading agents…</div>;
  if (err) return <div className="text-red-600">{err}</div>;

  return (
    <div className="space-y-3">
      <h1 className="text-lg font-semibold">Agents</h1>
      <table className="w-full text-sm border">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 text-left">Name</th>
            <th className="p-2 text-left">Status</th>
            <th className="p-2 text-left">Last Heartbeat</th>
          </tr>
        </thead>
        <tbody>
          {rows.map(a => (
            <tr key={a.id} className="border-t hover:bg-gray-50">
              <td className="p-2">
                <Link to={`/agents/${a.id}`} className="text-blue-600 underline">{a.name}</Link>
              </td>
              <td className="p-2">{a.status}</td>
              <td className="p-2">{new Date(a.lastHeartbeat).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
