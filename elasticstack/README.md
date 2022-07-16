# Elasticsearch setup with Docker

* Single-node discovery

Set on single-node discovery: a node will elect itself master and will  not join a cluster with any other node.

* Bootstrap checks

You can force execution of the bootstrap checks by setting the system property es.enforce.bootstrap.checks to true in the JVM options.

By default, Elasticsearch binds to loopback addresses for HTTP and transport (internal) communication. This is fine for downloading and playing with Elasticsearch as well as everyday development, but it’s useless for production systems. To join a cluster, an Elasticsearch node must be reachable via transport communication. To join a cluster via a non-loopback address, a node must bind transport to a non-loopback address and not be using single-node discovery. Thus, we consider an Elasticsearch node to be in development mode if it can not form a cluster with another machine via a non-loopback address, and is otherwise in production mode if it can join a cluster via non-loopback addresses.

Note that HTTP and transport can be configured independently via http.host and transport.host; this can be useful for configuring a single node to be reachable via HTTP for testing purposes without triggering production mode.

# How to run?

`$ docker-compose up`

To test if it works: `$ curl 127.0.0.1:9200`

## Heap size

The environment variable `ES_JAVA_OPTS` is used to specify the minimum and maximum JVM heap size (1GB in this case). If it’s not set, it’s very likely that your Elasticsearch container cannot be started successfully

## xpack security

We need to explicitly disable xpack security so we don’t need authentication to access the Elasticsearch server. It’s OK for local development but should be enabled for production.
