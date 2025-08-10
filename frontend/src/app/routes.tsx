import { createBrowserRouter } from "react-router-dom";
import ProtectedRoute from "../components/ProtectedRoute";
import Layout from "../components/Layout";
import Login from "../pages/Login";
import Agents from "../pages/Agents";
import AgentDetail from "../pages/AgentDetail";
import Events from "../pages/Events";

export const router = createBrowserRouter([
  { path: "/login", element: <Login /> },
  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <Layout />,
        children: [
          { index: true, element: <Agents /> },
          { path: "/agents/:id", element: <AgentDetail /> },
          { path: "/events", element: <Events /> },
        ],
      },
    ],
  },
]);