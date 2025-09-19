import { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import { getAgent, AgentDto } from "../api/agents";
import { addDirectories, deleteDirectory, listDirectories, DirectoryDto } from "../api/directories";

export default function AgentDetail() {
  const { id = "" } = useParams();
  const [agent, setAgent] = useState<AgentDto | null>(null);
  const [dirs, setDirs] = useState<DirectoryDto[]>([]);
  const [newPath, setNewPath] = useState("");
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState<string | null>(null);

  const reload = useMemo(() => async () => {
    const [{ data: a }, { data: d }] = await Promise.all([
      getAgent(id), listDirectories(id)
    ]);
    setAgent(a); setDirs(d);
  }, [id]);

  useEffect(() => {
    (async () => {
      try { await reload(); } 
      catch (e: any) { setErr(e?.response?.data ?? "Load failed"); }
      finally { setLoading(false); }
    })();
  }, [reload]);

  const add = async () => {
    if (!newPath.trim()) return;
    await addDirectories(id, [newPath.trim()]);
    setNewPath("");
    await reload();
  };

  const del = async (dirId: string) => {
    await deleteDirectory(dirId);
    await reload();
  };

  if (loading) return <div>Loading…</div>;
  if (err) return <div className="text-red-600">{err}</div>;
  if (!agent) return null;

  return (
    <div className="space-y-4">
      <h1 className="text-lg font-semibold">{agent.name}</h1>
      <div className="text-sm text-gray-600">
        Status: {agent.status} • Last heartbeat: {new Date(agent.lastHeartbeat).toLocaleString()}
      </div>

      <div className="flex gap-2">
        <input className="border px-3 py-2 rounded flex-1"
               placeholder="C:\\path\\to\\watch or /var/log"
               value={newPath} onChange={(e)=>setNewPath(e.target.value)} />
        <button onClick={add} className="px-3 py-2 rounded bg-black text-white">Add directory</button>
      </div>

      <table className="w-full text-sm border">
        <thead className="bg-gray-100">
          <tr><th className="p-2 text-left">Path</th><th className="p-2"></th></tr>
        </thead>
        <tbody>
          {dirs.map(d => (
            <tr key={d.id} className="border-t">
              <td className="p-2 font-mono">{d.path}</td>
              <td className="p-2 text-right">
                <button onClick={()=>del(d.id)} className="px-3 py-1.5 rounded border">Delete</button>
              </td>
            </tr>
          ))}
          {!dirs.length && <tr><td className="p-2 text-gray-500" colSpan={2}>No directories yet.</td></tr>}
        </tbody>
      </table>
    </div>
  );
}
