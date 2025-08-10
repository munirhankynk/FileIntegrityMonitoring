import { create } from "zustand";

type AuthState = {
  isAuthed: boolean;
  username?: string;
  setAuthed: (v: boolean) => void;
  setUser: (u?: string) => void;
};

export const useAuth = create<AuthState>((set) => ({
  isAuthed: false,
  username: undefined,
  setAuthed: (v) => set({ isAuthed: v }),
  setUser: (u) => set({ username: u }),
}));
