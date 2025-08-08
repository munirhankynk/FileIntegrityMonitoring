import os
import json
import time
import requests
import schedule
import threading
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler


def load_config():
    try:
        with open("config.json", "r") as f:
            return json.load(f)
    except FileNotFoundError:
        return {}

config = load_config()
BACKEND_URL = config.get("backend_url")
AGENT_ID = config.get("agent_id")
ACTIVATION_TOKEN = config.get("activation_token")
USERNAME = config.get("username")
PASSWORD = config.get("password")
WATCH_DIRS = config.get("watch_dirs")
HEARTBEAT_INTERVAL = config.get("heartbeat_interval")

SESSION = requests.Session()

# Kimlik doğrulama 
def authenticate():
    login_url = f"{BACKEND_URL}/api/v1/public/authentication/login"
    credentials = {"username": USERNAME, "password": PASSWORD} 
    
    try:
        print(login_url)
        response = SESSION.post(login_url, json=credentials, timeout=20)
        if response.status_code == 200:
            print("[AUTH] Giriş başarılı!")
            return True
        else:
            print(f"[AUTH] Giriş başarısız: {response.text}")
            return False
    except requests.RequestException as e:
        print(f"[AUTH] Giriş hatası: {e}")
        return False

# Agent'ı kayıt et
def register_agent():
    global AGENT_ID
    url = f"{BACKEND_URL}/api/agent/activate"
    data = {"activationToken": ACTIVATION_TOKEN}

    try:
        response = SESSION.post(url, json=data, timeout=5)
        if response.status_code == 200:
            agent_info = response.json()
            AGENT_ID = agent_info.get("agentId")
            if AGENT_ID:
                config["agent_id"] = AGENT_ID
                with open("config.json", "w") as f:
                    json.dump(config, f, indent=4)
                print(f"[REGISTER] Agent kayıt başarılı: {AGENT_ID}")
            else:
                print(f"[REGISTER] Kayıt başarılı ama agentId gelmedi! Response: {agent_info}")
        elif response.status_code == 409:
            print("[REGISTER] Agent zaten aktif edilmiş.")
        else:
            print(f"[REGISTER] Kayıt başarısız: {response.text}")
    except requests.RequestException as e:
        print(f"[REGISTER] Kayıt hatası: {e}")


def send_directories_to_backend():
    url = f"{BACKEND_URL}/api/agent/directories"
    data = {
        "agentId": AGENT_ID,
        "paths": WATCH_DIRS
    }

    print(f"[DEBUG] Sending directories: {data}")

    try:
        response = SESSION.post(url, json=data, timeout=5)
        if response.status_code == 200:
            print(f"[DIR-REGISTER] İzlenen dizinler backend'e işlendi.")
        elif response.status_code == 400:
            print(f"[DIR-REGISTER] UYARI: Dizinlerin bazıları zaten kayıtlı olabilir.")
        else:
            print(f"[DIR-REGISTER] Hata: {response.status_code} - {response.text}")
    except requests.RequestException as e:
        print(f"[DIR-REGISTER] Bağlantı hatası: {e}")


def get_watched_directories():
    url = f"{BACKEND_URL}/api/agent/heartbeat"
    data = {"agentId": AGENT_ID}

    try:
        response = SESSION.post(url, json=data, timeout=5)
        if response.status_code == 200:
            body = response.json()
            paths = body.get("directories", [])
            if paths:
                print(f"[DIR] Backend dizinleri döndü: {paths}")
                config["watch_dirs"] = paths
                with open("config.json", "w") as f:
                    json.dump(config, f, indent=4)
                return paths
            else:
                print("[DIR] Backend boş liste döndü, değiştirme yapılmadı.")
                return config.get("watch_dirs", [])  
        else:
            print("[DIR] Backend dizin listesi sağlayamadı, varsayılan kullanılacak.")
            return config.get("watch_dirs", [])
    except Exception as e:
        print(f"[DIR] Dizin hatası: {e}")
        return config.get("watch_dirs", [])



# Heartbeat gönder
def send_heartbeat():
    url = f"{BACKEND_URL}/api/agent/heartbeat"
    data = {"agentId": AGENT_ID, "status": "online"}
    
    try:
        response = SESSION.post(url, json=data, timeout=5)
        print(f"[HEARTBEAT] Gönderildi: {response.status_code}")
    except requests.RequestException as e:
        print(f"[HEARTBEAT] Hata: {e}")

# Dosya değişikliklerini raporla
def report_file_change(file_path, event_type):
    url = f"{BACKEND_URL}/api/v1/agent/event"
    data = {
        "agentId": AGENT_ID,
        "filePath": file_path,
        "eventType": event_type
    }
    
    try:
        response = SESSION.post(url, json=data, timeout=5)
        print(f"[EVENT] {event_type} olayı bildirildi: {file_path}")
    except requests.RequestException as e:
        print(f"[EVENT] Raporlama hatası: {e}")

# Dosya olayları izleyici
class FileChangeHandler(FileSystemEventHandler):
    def on_modified(self, event):
        if not event.is_directory:
            report_file_change(event.src_path, "modified")

    def on_created(self, event):
        if not event.is_directory:
            report_file_change(event.src_path, "created")

    def on_deleted(self, event):
        if not event.is_directory:
            report_file_change(event.src_path, "deleted")

# İzlemeyi başlat
def start_watching():
    print(f"[WATCHING] İzleme başlatıldı: {WATCH_DIRS}")
    event_handler = FileChangeHandler()
    observer = Observer()
    for directory in WATCH_DIRS:
        observer.schedule(event_handler, path=directory, recursive=True)
    observer.start()
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()

# Heartbeat thread'i
def run_heartbeat():
    schedule.every(HEARTBEAT_INTERVAL).seconds.do(send_heartbeat)
    while True:
        schedule.run_pending()
        time.sleep(1)


if __name__ == "__main__":
    print(f"[AGENT] Başlatılıyor. Backend: {BACKEND_URL}")

    if authenticate():
        if AGENT_ID == "default-agent":
            register_agent()

        WATCH_DIRS = get_watched_directories()

        send_directories_to_backend()

        heartbeat_thread = threading.Thread(target=run_heartbeat, daemon=True)
        heartbeat_thread.start()

        start_watching()
    else:
        print("[AGENT] Başlatılamadı. Giriş başarısız.")
