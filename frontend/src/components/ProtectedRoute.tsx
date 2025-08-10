import { Navigate, Outlet } from "react-router-dom";
import { useEffect, useState } from "react";
import { me } from "../api/auth";
import { useAuth } from "../store/auth";

export default function ProtectedRoute() {
  const { isAuthed, setAuthed, setUser } = useAuth();
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    (async () => {
      try {
        const res = await me(); 
        if (res?.data?.username) setUser(res.data.username);
        setAuthed(true);
      } catch {
        setAuthed(false);
      } finally {
        setChecking(false);
      }
    })();
  }, [setAuthed, setUser]);

  if (checking) return <div className="p-6 text-sm">Checking session…</div>;
  return isAuthed ? <Outlet /> : <Navigate to="/login" replace />;
}
