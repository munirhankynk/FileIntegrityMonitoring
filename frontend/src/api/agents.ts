import api from "./client";

export type AgentDto = {
  id: string;                     // UUID
  name: string;
  status: string;                 // "active" | "inactive"
  lastHeartbeat: string;          // LocalDateTime -> ISO string
};

export const listAgents = () => api.get<AgentDto[]>("/api/agent/list");
export const getAgent   = (id: string) => api.get<AgentDto>(`/api/agent/${id}`);
