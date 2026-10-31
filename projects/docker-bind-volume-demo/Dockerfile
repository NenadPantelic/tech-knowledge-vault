# Use the official Python image from the Docker Hub
FROM python:3.11-slim

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file to the container
COPY requirements.txt .

# Install Flask and other dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the Flask app code to the container
COPY server.py .

# Expose the port that the app runs on
EXPOSE 5000

# Command to run the Flask application
CMD ["python", "server.py"]