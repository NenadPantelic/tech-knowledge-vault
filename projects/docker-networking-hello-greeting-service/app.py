import argparse
import requests
from flask import Flask, jsonify, request

app = Flask(__name__)


HEADERS = {"Content-Type": "application/json"}

def create_app(my_name, other_service_address):
    app = Flask(__name__)

    @app.post("/hello")
    def hello():
        data = request.get_json()
        name = data.get("name", "unknown")
        message = data.get("message", "No message")
        return jsonify(message=f"Hello, {name}! My name is {my_name}. I have received your message: {message}.")


    @app.post("/greet")
    def greet():
        payload = {"name": my_name, "message": "Hello"}
        response = requests.post(f"{other_service_address}/hello", json=payload, headers=HEADERS, timeout=30)
        response.raise_for_status()
        response_body = response.json()

        greeting_response = {
            "sender": payload,
            "receiver": response_body
        }
        return greeting_response


    return app

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--my_name", help = "The service name when it talks to the other service")
    parser.add_argument("--port", help = "Port of the service.")
    parser.add_argument("--other_service_address", help="Base URL of other service B, e.g. http://localhost:5002")
    args = parser.parse_args()

    my_name = args.my_name
    port = args.port
    other_service_address = args.other_service_address
    print(f"Input parameters: my_name = {my_name}, port = {port}, other_service_address = {other_service_address}")

    print(f"Starting server on port:{port}")
    create_app(my_name, other_service_address).run(host="0.0.0.0", port=port)


# python .\app.py --my_name "service A" --port 5001 --other_service_address http://localhost:5002
# python .\app.py --my_name "service B" --port 5002 --other_service_address http://localhost:5001


# docker run -d --name s1 -p 5001:5001 hello-greeting-service:latest --network=greeting-network --my_name "s1" --port 5001 --other_service_address http://localhost:5002
# docker run -d --name s2 -p 5002:5002 hello-greeting-service:latest --network=greeting-network --my_name "s2" --port 5002 --other_service_address http://localhost:5001


# docker create network -d bridge greeting-network

# docker run --name s1 -p 5001:5001 --network=greeting-network  hello-greeting-service:latest --my_name "s1" --port 5001 --other_service_address http://s2:5002
#docker run --name s2 -p 5002:5002 --network=greeting-network  hello-greeting-service:latest --my_name "s2" --port 5002 --other_service_address http://s1:5001
