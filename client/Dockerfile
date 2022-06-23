# Dockerfile for the client
FROM node:16.15.0-buster-slim as build

WORKDIR /app
COPY package*.json ./
# Install dependencies
RUN npm install

# Install express 4.18.1
RUN npm i express@4.18.1

# Add all application files from the directory to WORKDIR of the container
COPY . .

RUN apt-get update
RUN apt-get install -y --no-install-recommends curl

# Client port
ENV PORT 8081
EXPOSE $PORT

# Build the project
RUN npm run build

ENTRYPOINT ["node", "server.js"]
