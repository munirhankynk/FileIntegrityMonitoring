import api from "./client";

export type DirectoryDto = { id: string; agentId: string; path: string; createdAt?: string };

export const listDirectories = (agentId?: string) =>
  api.get<DirectoryDto[]>("/directories", { params: { agentId } });

export const addDirectory = (agentId: string, path: string) =>
  api.post<DirectoryDto>("/directories", { agentId, path });

export const removeDirectory = (id: string) => api.delete(`/directories/${id}`);
