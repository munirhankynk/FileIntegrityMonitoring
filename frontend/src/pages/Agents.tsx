import { useEffect, useState } from "react";
import { AgentDto, listAgents } from "../api/agents";
import { Link } from "react-router-dom";

export default function Agents() {
  const [items, setItems] = useState<AgentDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    (async () => {
      try {
        const { data } = await listAgents();
        setItems(data);
      } catch (err: any) {
        setError(err?.response?.data || "Failed to load agents");
      } finally { setLoading(false); }
    })();
  }, []);

  if (loading) return <div>Loading agents…</div>;
  if (error) return <div className="text-red-600">{error}</div>;

  return (
    <div className="space-y-4">
      <h1 className="text-xl font-semibold">Agents</h1>
      <div className="bg-white rounded-2xl border">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-left border-b">
              <th className="px-3 py-2">ID</th>
              <th className="px-3 py-2">Name</th>
              <th className="px-3 py-2">Status</th>
              <th className="px-3 py-2">Last Heartbeat</th>
            </tr>
          </thead>
          <tbody>
            {items.map(a => (
              <tr key={a.id} className="border-t hover:bg-gray-50">
                <td className="px-3 py-2 font-mono text-xs">
                  <Link className="underline" to={`/agents/${a.id}`}>{a.id}</Link>
                </td>
                <td className="px-3 py-2">{a.name}</td>
                <td className="px-3 py-2">{a.status}</td>
                <td className="px-3 py-2">{new Date(a.lastHeartbeat).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
