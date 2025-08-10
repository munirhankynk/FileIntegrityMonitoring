import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { logout } from "../api/auth";
import { useAuth } from "../store/auth";

export default function Layout() {
  const nav = useNavigate();
  const { username, setAuthed, setUser } = useAuth();

  const doLogout = async () => {
    try { await logout(); } finally {
      setAuthed(false);
      setUser(undefined);
      nav("/login", { replace: true });
    }
  };

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    `px-3 py-2 rounded-xl ${isActive ? "bg-gray-200" : "hover:bg-gray-100"}`;

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="flex items-center justify-between px-6 py-3 border-b bg-white">
        <Link to="/" className="font-semibold">File Integrity</Link>
        <nav className="flex gap-2">
          <NavLink to="/" className={linkClass}>Agents</NavLink>
          <NavLink to="/events" className={linkClass}>Events</NavLink>
        </nav>
        <div className="flex items-center gap-3">
          <span className="text-sm text-gray-600">{username ?? "User"}</span>
          <button onClick={doLogout} className="px-3 py-1.5 rounded-xl border">Logout</button>
        </div>
      </header>
      <main className="p-6 max-w-6xl mx-auto">
        <Outlet />
      </main>
    </div>
  );
}
