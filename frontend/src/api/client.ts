import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "https://localhost:8443/api/v1",
  withCredentials: true, // cookie gönderimi
});

// basit cookie okuyucu
function getCookie(name: string) {
  return document.cookie
    .split("; ")
    .find(r => r.startsWith(name + "="))
    ?.split("=")[1];
}

// Spring Security: XSRF-TOKEN cookie -> X-XSRF-TOKEN header
api.interceptors.request.use((config) => {
  const csrf = getCookie("XSRF-TOKEN");
  if (csrf) (config.headers ||= {})["X-XSRF-TOKEN"] = csrf;
  return config;
});

export default api;
