import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getAgent } from "../api/agents";
import { addDirectory, listDirectories, removeDirectory, DirectoryDto } from "../api/directories";

export default function AgentDetail() {
  const { id } = useParams();
  const [agentName, setAgentName] = useState<string>("");
  const [dirs, setDirs] = useState<DirectoryDto[]>([]);
  const [path, setPath] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refresh = async () => {
    if (!id) return;
    const [{ data: agent }, { data: directories }] = await Promise.all([
      getAgent(id),
      listDirectories(id),
    ]);
    setAgentName(agent.name);
    setDirs(directories);
  };

  useEffect(() => {
    (async () => {
      try { await refresh(); }
      catch (e: any) { setError(e?.response?.data || "Failed to load agent"); }
      finally { setLoading(false); }
    })();
  }, [id]);

  const onAdd = async () => {
    if (!id || !path) return;
    await addDirectory(id, path);
    setPath("");
    await refresh();
  };

  const onDelete = async (dirId: string) => {
    await removeDirectory(dirId);
    await refresh();
  };

  if (loading) return <div>Loading…</div>;
  if (error) return <div className="text-red-600">{error}</div>;

  return (
    <div className="space-y-4">
      <h1 className="text-xl font-semibold">Agent: {agentName}</h1>

      <div className="flex items-end gap-2">
        <div className="flex-1">
          <label className="block text-sm mb-1">Watch path</label>
          <input
            value={path}
            onChange={(e)=>setPath(e.target.value)}
            placeholder="C:\\logs veya /var/log gibi"
            className="w-full border rounded-xl px-3 py-2"
          />
        </div>
        <button onClick={onAdd} className="px-3 py-2 rounded-xl border">Add</button>
      </div>

      <div className="bg-white rounded-2xl border">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-left border-b">
              <th className="px-3 py-2">Directory</th>
              <th className="px-3 py-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {dirs.map(d => (
              <tr key={d.id} className="border-t">
                <td className="px-3 py-2 font-mono text-xs">{d.path}</td>
                <td className="px-3 py-2">
                  <button onClick={()=>onDelete(d.id)} className="px-2 py-1 rounded-lg border">Delete</button>
                </td>
              </tr>
            ))}
            {dirs.length === 0 && (
              <tr><td className="px-3 py-3 text-gray-500" colSpan={2}>No directories yet.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
