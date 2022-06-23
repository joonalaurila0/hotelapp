# Stage 1
FROM openjdk:17-slim-bullseye as build

# optinal arguments for build information
ARG BUILD_ID
ARG BUILD_DATE
ARG VCS_REF

# stage
LABEL stage=builder

# Build information for intermediate image.
# This information is not preserved for the final image.
LABEL org.label-schema.version="${BUILD_ID}" \
      org.label-schema.build-date="${BUILD_DATE}" \
      org.label-schema.vcs-ref="${VCS_REF}" \
      org.label-schema.name="configuration-server" \
      org.label-schema.schema-version="1.0"

# The application's jar file
ARG JAR_FILE

# Add the application's jar to the container
COPY ${JAR_FILE} /app.jar

# Unpackage the jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

# Stage 2
FROM openjdk:17-slim-bullseye as runtime

# Mount volume to /tmp
VOLUME /tmp

# Copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

RUN apt-get update && apt-get install -y --no-install-recommends curl
EXPOSE 8000

# Execute the application
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "io.hotely.customer.CustomerApplication"]

# image for debugging, used with docker build --target flag
# to build this image, run: make build-debug
# copies needed debugging tools into image
FROM build as debug-image
ENTRYPOINT ["sh"]

# ensure runtime is the default image
FROM runtime as default
