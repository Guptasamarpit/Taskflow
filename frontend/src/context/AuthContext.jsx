import { createContext, useContext, useState } from "react";
const C = createContext();
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const u = localStorage.getItem("user");
    return u ? JSON.parse(u) : null;
  });
  const login = (d) => {
    localStorage.setItem("token", d.token);
    localStorage.setItem("user", JSON.stringify(d));
    setUser(d);
  };
  const logout = () => {
    localStorage.clear();
    setUser(null);
  };
  return <C.Provider value={{ user, login, logout }}>{children}</C.Provider>;
}
export const useAuth = () => useContext(C);
