import api from "./client";

export type DirectoryDto = { id: string; agentId: string; path: string };

export const listDirectories = (agentId: string) =>
  api.get<DirectoryDto[]>(`/api/agent/directories`, { params: { agentId } });

export const addDirectories = (agentId: string, paths: string[]) =>
  api.post(`/api/agent/directories`, { agentId, paths });

export const deleteDirectory = (id: string) =>
  api.delete(`/api/agent/directories/${id}`);
