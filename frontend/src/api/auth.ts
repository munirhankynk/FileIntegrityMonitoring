import api from "./client";

export type LoginPayload = { username: string; password: string };
export type MeResponse = { username: string };

export const login = (data: LoginPayload) => api.post("/auth/login", data);
export const logout = () => api.post("/auth/logout");

// varsa kullanırız; yoksa ProtectedRoute hata vermez, sadece login'e atar.
export const me = () => api.get<MeResponse>("/auth/me");
