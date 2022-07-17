### Building and running the application

To build the application, you can run `$ gradle clean build`.

For Docker, run: `$ make build`

To import onto another host: `$ make import`

### Running the application

If you have build the application, it can be run by: `$ gradle bootRun` or `$ java -jar target/<jarfile>.jar`

To run the application in a container for test: `$ make run`.


