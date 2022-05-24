# Dockerfile for the client
#

FROM node:16.15.0-buster-slim
WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .
ENV PORT 8080
EXPOSE $PORT
RUN npm run build
CMD ["npm", "run", "server"]
