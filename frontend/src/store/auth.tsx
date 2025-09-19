import React, { createContext, useContext, useMemo, useState } from "react";

type AuthContextType = {
  isAuthed: boolean;
  username?: string;
  setAuthed: (v: boolean) => void;
  setUser: (name?: string) => void;
  resetAuth: () => void;
};

const AuthContext = createContext<AuthContextType>({
  isAuthed: false,
  username: undefined,
  setAuthed: () => {},
  setUser: () => {},
  resetAuth: () => {},
});

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [isAuthed, setAuthed] = useState(false);
  const [username, setUsername] = useState<string | undefined>(undefined);

  const setUser = (name?: string) => setUsername(name);
  const resetAuth = () => {
    setAuthed(false);
    setUsername(undefined);
  };

  const value = useMemo(
    () => ({ isAuthed, username, setAuthed, setUser, resetAuth }),
    [isAuthed, username]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);
