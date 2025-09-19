import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { logout } from "../api/auth";
import { useAuth } from "../store/auth";

export default function Layout() {
  const nav = useNavigate();
  const { username, resetAuth } = useAuth();

  const doLogout = async () => {
    try {
      await logout();
    } finally {
      resetAuth();
      nav("/login", { replace: true });
    }
  };

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    "px-3 py-2 rounded-xl " + (isActive ? "bg-gray-200" : "hover:bg-gray-100");

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="border-b bg-white">
        <nav className="mx-auto max-w-5xl px-4 py-3 flex items-center gap-3">
          <Link to="/" className="font-semibold">File Integrity</Link>
          <NavLink to="/" className={linkClass}>Agents</NavLink>
          <NavLink to="/events" className={linkClass}>Events</NavLink>
          <div className="flex-1" />
          <span className="text-sm text-gray-600">{username ?? "User"}</span>
          <button onClick={doLogout} className="px-3 py-1.5 rounded-xl border">Logout</button>
        </nav>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-6">
        <Outlet />
      </main>
    </div>
  );
}
