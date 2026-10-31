import os
from flask import Flask, jsonify, request
from uuid import uuid4


STORE_LOCATION = "store"

app = Flask(__name__)

def _create_store_folder():
    if not os.path.exists(STORE_LOCATION):
        os.makedirs(STORE_LOCATION)

def write_content(path, content):
    with open(path, "w") as new_file:
        new_file.write(content)

def read_content(path):
    with open(path, "r") as file:
        return file.read()

def create_response_body(file_id, content):
    body = {
        "file_id": file_id,
        "content": content
    }
    return jsonify(body)

@app.route('/api/v1/files', methods=['POST'])
def store_content():
    file_id = f"{uuid4()}"
    full_path = f"{STORE_LOCATION}{os.sep}{file_id}.txt"
    
    data = request.get_json()
    content = data.get("content", "No data")
    write_content(full_path, content)

    return create_response_body(file_id, content), 201

@app.route('/api/v1/files/<file_id>', methods=['GET'])
def get_content(file_id):
    full_path = f"{STORE_LOCATION}{os.sep}{file_id}.txt"
    if os.path.exists(full_path):
        content = read_content(full_path)
        return create_response_body(file_id, content), 200
    
    return jsonify({"error": "File not found."}), 404



if __name__ == "__main__":
    _create_store_folder()
    app.run(host="0.0.0.0", debug=True)


# Without bind
# docker run -dit --name file-store-server -p 5000:5000 files-api:0.1
    
# With bind
# docker run -dit --name file-store-server --mount type=bind,src="$(pwd)/store",target=/app/store -p 5000:5000 files-api:0.1    

# Read-only bind
# docker run -dit --name file-store-server --mount type=bind,src="$(pwd)/store",target=/app/store,readonly -p 5000:5000 files-api:0.1    