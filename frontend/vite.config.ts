import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import fs from "fs";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const certPath = path.resolve(__dirname, "localhost.pem");
const keyPath  = path.resolve(__dirname, "localhost-key.pem");

let httpsOption: import("https").ServerOptions | undefined = undefined;
if (fs.existsSync(certPath) && fs.existsSync(keyPath)) {
  httpsOption = {
    cert: fs.readFileSync(certPath),
    key:  fs.readFileSync(keyPath),
  };
}

export default defineConfig({
  plugins: [react()],
  server: {
    https: httpsOption, 
    port: 5173,
  },
});
