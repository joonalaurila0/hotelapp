# About Configuration Servber

Acts as the centralized application configuration management.  

**Permissions error with configuration server? Note about deployment onto other hosts and the image file**: You most likely have outdated vault keys due to image being built on older jar than what HCP Vault was deployed on.

Current deployment model is pretty rough around the edges to say the least, currently HCP Vault is distributing the keys for the applications through program called [yq](https://github.com/mikefarah/yq/), (similar to jq) to import the keys for the java apps. This is used by the `import_tokens` function from the libvault.sh, to be found in `/vault` directory.

### Building the application

To build the application, you can run `$ gradle clean build`.

For Docker, run: `$ make build`

To import onto another host: `$ make import`

### Running the application

If you have build the application, it can be run by: `$ gradle bootRun` or `$ java -jar target/<jarfile>.jar`

To run the application in a container for test: `$ make run`.

# About the urls

The configurations for the other services are in `src/main/resources/config`, you can find these by http requests by starting up the server and using `localhost:8888/<service>/<label>` for example `localhost:8888/consumer/default`, the services go by config filenames. The Configuration Server's own properties can be found at `localhost:8888/config/default`. Make sure vault is running and configured properly, otherwise configuration server will fail to serve these configurations at the urls.
