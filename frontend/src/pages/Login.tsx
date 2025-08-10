import { useState } from "react";
import { login } from "../api/auth";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../store/auth";

export default function Login() {
  const nav = useNavigate();
  const { setAuthed, setUser } = useAuth();
  const [form, setForm] = useState({ username: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault(); setError(null); setLoading(true);
    try {
      await login(form);
      setAuthed(true);
      setUser(form.username);
      nav("/", { replace: true });
    } catch (err: any) {
      setError(err?.response?.data || "Login failed");
    } finally { setLoading(false); }
  };

  return (
    <div className="min-h-screen grid place-items-center bg-gray-50 p-6">
      <form onSubmit={submit} className="w-full max-w-sm bg-white rounded-2xl p-6 shadow">
        <h1 className="text-xl font-semibold mb-4">Sign in</h1>

        <label className="block text-sm mb-1">Username</label>
        <input
          className="w-full border rounded-xl px-3 py-2 mb-3"
          value={form.username}
          onChange={(e)=>setForm({...form, username:e.target.value})}
        />

        <label className="block text-sm mb-1">Password</label>
        <input
          type="password"
          className="w-full border rounded-xl px-3 py-2 mb-4"
          value={form.password}
          onChange={(e)=>setForm({...form, password:e.target.value})}
        />

        {error && <div className="text-red-600 text-sm mb-3">{String(error)}</div>}

        <button disabled={loading} className="w-full rounded-xl border px-3 py-2">
          {loading ? "Signing in…" : "Sign in"}
        </button>
      </form>
    </div>
  );
}
