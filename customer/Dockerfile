# Stage 1
FROM openjdk:17-slim as build

# The application's jar file
ARG JAR_FILE

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Unpackage the jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

# Stage 2
FROM openjdk:17-slim

# Mount volume to /tmp
VOLUME /tmp

# Copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Execute the application
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "io.hotely.customer.CustomerApplication"]
