import { Link, NavLink, Outlet } from "react-router-dom";

const nav = [
  { to: "/agents", label: "Agents" },
  { to: "/events", label: "Events" },
];

export default function Layout() {
  return (
    <div className="min-h-dvh bg-background text-foreground">
      <header className="border-b">
        <div className="mx-auto max-w-6xl px-4 h-14 flex items-center justify-between">
          <Link to="/agents" className="font-semibold">File Integrity</Link>
          <nav className="flex items-center gap-1">
            {nav.map(i => (
              <NavLink
                key={i.to}
                to={i.to}
                className={({ isActive }) =>
                  "px-3 py-1.5 rounded-lg text-sm " +
                  (isActive ? "bg-primary text-primary-foreground" : "hover:bg-muted")
                }
              >
                {i.label}
              </NavLink>
            ))}
          </nav>
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-4 py-6">
        <Outlet />
      </main>

      <footer className="border-t">
        <div className="mx-auto max-w-6xl px-4 h-12 flex items-center text-xs text-muted-foreground">
          © {new Date().getFullYear()} File Integrity Monitoring
        </div>
      </footer>
    </div>
  );
}
