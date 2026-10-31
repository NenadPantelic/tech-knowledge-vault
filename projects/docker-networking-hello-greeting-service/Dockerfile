FROM python:3.12-slim
WORKDIR /app

# RUN apt update
# RUN apt install dnsutils curl

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY app.py ./app.py
EXPOSE 5001 5002
ENTRYPOINT ["python", "app.py"]
