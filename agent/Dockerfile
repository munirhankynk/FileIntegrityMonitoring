FROM python:3.10-slim

WORKDIR /app

# Gerekli bağımlılıkları yükle
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Uygulama dosyalarını kopyala
COPY . .

CMD ["python", "agent.py"]
