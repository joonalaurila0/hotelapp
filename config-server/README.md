# About Configuration Servber

**Permissions error with configuration server? Note about deployment onto other hosts and the image file**: You most likely have outdated vault keys due to image being built on older jar than what HCP Vault was deployed on.

Current deployment model is pretty rough around the edges to say the least, currently HCP Vault is distributing the keys for the applications through program called [yq](https://github.com/mikefarah/yq/), (similar to jq) to import the keys for the java apps. This is used by the `import_tokens` function from the libvault.sh, to be found in `/vault` directory.

IN MIDDLE OF DEVELOPMENT !

In case you dont have the jar file in the target/ directory, use `mvn clean verify` to have maven environment setup, and then you can build the Dockerfile by using `make build`. You can also build the Dockerfile by just providing the jar file as an argument.

### Building the application

To build the application, you can run `maven clean verify`.

For Docker, run: `make build`


### Running the application

If you have build the application, it can be run by: `mvn spring-boot:run` or `java -jar target/<jarfile>.jar`

To run the application in a container for test: `make run`.

# About the urls

The configurations for the other services are in `src/main/resources/config`, you can find these by http requests by starting up the server and using `localhost:8888/<service>/<label>` for example `localhost:8888/consumer/default`, the services go by config filenames. The Configuration Server's own properties can be found at `localhost:8888/config/default`. Make sure vault is running and configured properly, otherwise configuration server will fail to serve these configurations at the urls.

# Tokens

Vault tokens are served from token.yml and tokenClient.yml files. token.yml file is for the Configuration server itself since it maps it to a different place from the clients. tokenClient.yml is the token for the clients.

### Optional Maven plugin
You can also build the Dockerfile using Maven plugin, however this is not recommended.
More info at <a href="https://github.com/spotify/dockerfile-maven">Dockerfile-Maven plugin</a>.
Be aware of running in production, it has has vulnerabilities, <a href="https://mvnrepository.com/artifact/com.spotify/dockerfile-maven-plugin/1.4.13">Vulnerabilities</a>.
