# Dockerfile for the client
# docker run --rm -it -p 8080:80 react-nginx
FROM node:16.15.0-buster-slim as build

WORKDIR /app
COPY package*.json ./
# Install dependencies
RUN npm install

# Add all application files from the directory to WORKDIR of the container
COPY . .

# Client port
ENV PORT 8080
EXPOSE $PORT

# Build the project
RUN npm run build

# Execute the application
#ENTRYPOINT ["npm", "run", "dev"]

# Nginx for serving the client
FROM nginx:1.20.0-alpine

# Nginx asset directory as the WORKDIR
WORKDIR /usr/share/nginx/html

# Removes default nginx static assets
RUN rm -rf .*/

# Copy static assets from build stage
COPY --from=build /app/build .

# Run nginx with global directives and daemon off
ENTRYPOINT ["nginx", "-g", "daemon off;"]
