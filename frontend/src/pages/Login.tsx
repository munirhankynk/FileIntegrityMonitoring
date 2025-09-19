import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/auth";
import { useAuth } from "../store/auth";

export default function Login() {
  const nav = useNavigate();
  const { setAuthed, setUser } = useAuth();
  const [username, setU] = useState("");
  const [password, setP] = useState("");
  const [err, setErr] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setErr(null);
    setLoading(true);
    try {
      await login({ username, password });
      setAuthed(true);
      setUser(username);
      nav("/", { replace: true });
    } catch (ex: any) {
      setErr(ex?.response?.data?.message ?? "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto max-w-sm pt-24">
      <h1 className="text-xl font-semibold mb-4">Sign in</h1>
      <form onSubmit={onSubmit} className="space-y-3">
        <input className="w-full border px-3 py-2 rounded" placeholder="Username"
               value={username} onChange={(e)=>setU(e.target.value)} />
        <input className="w-full border px-3 py-2 rounded" type="password" placeholder="Password"
               value={password} onChange={(e)=>setP(e.target.value)} />
        {err && <div className="text-red-600 text-sm">{err}</div>}
        <button disabled={loading} className="px-4 py-2 rounded bg-black text-white">
          {loading ? "Signing in…" : "Login"}
        </button>
      </form>
    </div>
  );
}
