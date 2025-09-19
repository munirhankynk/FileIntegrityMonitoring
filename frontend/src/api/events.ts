import api from "./client";

export type EventDto = {
  id: string;
  agentId: string;
  filePath: string;
  eventType: "created" | "modified" | "deleted";
  timestamp: string;
};

export const listEvents = (p: {
  agentId?: string;
  eventType?: string;
  from?: string; // ISO 'YYYY-MM-DDTHH:mm'
  to?: string;   // ISO
}) => api.get<EventDto[]>("/api/v1/agent/events", { params: p });
