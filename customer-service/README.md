# Apacha Cassandra Setup

Remember to check `LOCAL_DC` also known as the datacenter, it is normally set to "datacenter1", you can check it from `/opt/cassandra/bin ./node tools`.

**Make sure the keyspaces exist before you try to do queries on them !**

* To create a new object: `curl -X POST "http://localhost:8001/examples" -H 'Content-Type: application/json' -d '{ "id": "d111d906-9f3c-453d-beaf-2b472173211f", "name": "meow", "age": 199 }'`

* To update an existing object: `curl -X PUT "http://localhost:8001/examples/171c5395-7205-47ec-a47a-b90998b60769" -H 'Content-Type: application/json' -d '{ "id": "171c5395-7205-47ec-a47a-b90998b60769", "name": "keisarii", "age": 21 }'`

* To get all objects from the table: `curl htpp://localhost:8001/examples`

* To get specific object from the table: `curl -v -X GET http://localhost:8001/examples/41560cdc-938a-413b-9c71-48a4cbcbe014`

* To remove an object from the table: `curl -v -X DELETE http://localhost:8001/examples/41560cdc-938a-413b-9c71-48a4cbcbe014`

# Spin up container

1. Build the application: `gradle clean build`.
2. Install prerequisite base-image for the application image building: `make install`.
3. Build the image of the application: `make build`.
4. Run the image: `make run`.

**Note: See the Makefile for other running options like `run-detached`.**

# Run the application

`gradle bootRun` or if you've already compiled, then `gradle run` should also do.
