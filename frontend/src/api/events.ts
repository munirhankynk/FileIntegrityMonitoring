import api from "./client";

export type EventDto = {
  id: string;
  agentId: string;
  path: string;
  eventType: "created" | "modified" | "deleted";
  timestamp: string;
};

export type EventFilters = {
  agentId?: string;
  eventType?: string;
  from?: string; 
  to?: string;   
};

export const listEvents = (filters: EventFilters) =>
  api.get<EventDto[]>("/events", { params: filters });
