#!/bin/bash

# Set variables
IMAGE_NAME="smart-medicine"
CONTAINER_NAME="smart-medicine-container"
IMAGE_FILE="${IMAGE_NAME}.tar"
REMOTE_USER="root"
REMOTE_HOST="47.115.213.70"
REMOTE_PATH="~/workspace/smart-medicine/"

# Echo function for logging
function log_step() {
    echo "===================================================="
    echo "$1"
    echo "===================================================="
}

# Ensure Docker context is set to default
log_step "Ensuring Docker context is set to default"
docker context use default

# Ensure Docker buildx is available
log_step "Ensuring Docker buildx is available"
if ! docker buildx ls | grep -q "default"; then
    echo "Creating and enabling Docker buildx builder"
    docker buildx create --name builder --use
else
    echo "Docker buildx is already available"
    docker buildx use default
fi

# Build the Docker image (specify platform as linux/amd64)
log_step "Building Docker image: ${IMAGE_NAME}:latest (platform: linux/amd64)"
docker buildx build --platform linux/amd64 --load -t ${IMAGE_NAME}:latest . && echo "Build successful" || { echo "Build failed"; exit 1; }

# Save the Docker image to a file
log_step "Saving Docker image to file: ${IMAGE_FILE}"
docker save -o ${IMAGE_FILE} ${IMAGE_NAME}:latest && echo "Image saved successfully" || { echo "Failed to save image"; exit 1; }

# Transfer the file to the remote server
log_step "Transferring ${IMAGE_FILE} to ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}"
rsync -avz --progress ${IMAGE_FILE} ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH} && echo "Transfer successful" || { echo "Transfer failed"; exit 1; }

# Execute commands on the remote server
log_step "Connecting to ${REMOTE_USER}@${REMOTE_HOST} to deploy the container"
ssh ${REMOTE_USER}@${REMOTE_HOST} << EOF
    cd ${REMOTE_PATH}
    echo "Current directory: \$(pwd)"
    
    # Check if container exists and stop it
    echo "Checking for existing container: ${CONTAINER_NAME}"
    if docker ps -a | grep -q ${CONTAINER_NAME}; then
        echo "Stopping container: ${CONTAINER_NAME}"
        docker stop ${CONTAINER_NAME}
        echo "Removing container: ${CONTAINER_NAME}"
        docker rm ${CONTAINER_NAME}
    else
        echo "No existing container found with name: ${CONTAINER_NAME}"
    fi
    
    # Check if image exists and remove it
    echo "Checking for existing image: ${IMAGE_NAME}"
    if docker images | grep -q ${IMAGE_NAME}; then
        echo "Removing image: ${IMAGE_NAME}"
        docker rmi ${IMAGE_NAME}:latest
    else
        echo "No existing image found with name: ${IMAGE_NAME}"
    fi
    
    # Load the Docker image
    echo "Loading Docker image from ${IMAGE_FILE}"
    docker load -i ${IMAGE_FILE}
    
    # Run the new container
    echo "Starting new container: ${CONTAINER_NAME}"
    docker run -d --name ${CONTAINER_NAME} \
        -p 8080:8080 \
        -e SPRING_PROFILES_ACTIVE=prod \
        ${IMAGE_NAME}:latest
    
    echo "Deployment completed successfully"
    echo "Container status:"
    docker ps | grep ${CONTAINER_NAME}
EOF

# Clean up local tar file
log_step "Cleaning up local tar file"
rm ${IMAGE_FILE} && echo "Local cleanup successful" || echo "Failed to remove local tar file"

log_step "Deployment process completed" 