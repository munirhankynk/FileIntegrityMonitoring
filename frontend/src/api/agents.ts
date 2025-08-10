import api from "./client";

export type AgentDto = {
  id: string;
  name: string;
  status: "active" | "inactive";
  lastHeartbeat: string;
};

export const listAgents = () => api.get<AgentDto[]>("/agents");
export const getAgent = (id: string) => api.get<AgentDto>(`/agents/${id}`);
