# About

IN MIDDLE OF DEVELOPMENT !

In case you dont have the jar file in the target/ directory, use `mvn clean package` to have maven environment setup, and then you can build the Dockerfile by using `mvn package dockerfile:build`. You can also build the Dockerfile by just providing the jar file as an argument. More info at <a href="https://github.com/spotify/dockerfile-maven">Dockerfile-Maven plugin</a>.

Be aware of running in production, it has has vulnerabilities, <a href="https://mvnrepository.com/artifact/com.spotify/dockerfile-maven-plugin/1.4.13">Vulnerabilities</a>.

# About the urls

The configurations for the other services are in `src/main/resources/config`, you can find these by http requests by starting up the server and using `localhost:8888/<service>/<label>` for example `localhost:8888/consumer/default`, the services go by config filenames. The Configuration Server's own properties can be found at `localhost:8888/config/default`. Make sure vault is running and configured properly, otherwise configuration server will fail to serve these configurations at the urls.

# Tokens

Vault tokens are served from token.yml and tokenClient.yml files. token.yml file is for the Configuration server itself since it maps it to a different place from the clients. tokenClient.yml is the token for the clients.