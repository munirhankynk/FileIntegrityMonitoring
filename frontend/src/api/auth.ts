import api from "./client";

export type LoginPayload = { username: string; password: string };
export type GenericResponse<T> = { statusCode: number; data?: T; message?: string };

export const login = (data: LoginPayload) =>
  api.post<GenericResponse<string>>("/api/v1/public/authentication/login", data);

export const logout = () =>
  api.post("/api/v1/public/authentication/logout");

export const me = async () => {
  await api.get("/api/agent/list", {
    params: { _t: Date.now() },
    headers: { "Cache-Control": "no-cache" },
  });
  return { data: { username: "User" } }; 
};
