# Dockerfile for the client
FROM node:16.15.0-buster-slim as build

WORKDIR /app

COPY package*.json ./

RUN npm install

# Add all application files from the directory to WORKDIR of the container
COPY . .

# Build the project
RUN npm run build

# Nginx for serving the client
FROM nginx:1.22.0 as webserver

# Nginx asset directory as the WORKDIR
WORKDIR /usr/share/nginx/html

# Removes default nginx static assets
RUN rm -rfv *

# Removes default nginx configuration files
RUN rm /etc/nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Copy static assets from build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Copy over configuration files
COPY --from=build /app/conf /etc/nginx

# Copy over SSL/TLS files
COPY ssl /etc/nginx/ssl

EXPOSE 80 443

# Run nginx with global directives and daemon off
ENTRYPOINT ["nginx", "-g", "daemon off;"]

FROM webserver as default
