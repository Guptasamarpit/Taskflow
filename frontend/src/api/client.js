import axios from "axios";
export const api = axios.create({ baseURL: "http://localhost:8080/api" });
// export const api = axios.create({ baseURL: "https://taskflow-backend-ipd1.onrender.com/api" });
api.interceptors.request.use((c) => {
  const t = localStorage.getItem("token");
  if (t) c.headers.Authorization = `Bearer ${t}`;
  return c;
});
