# First stage (BUILD)
FROM quay.io/keycloak/keycloak:17.0.0 as builder

ARG KC_METRICS_ENABLED
ARG KC_FEATURES
ARG KC_DB

ENV KC_METRICS_ENABLED=${KC_METRICS_ENABLED}
ENV KC_FEATURES=${KC_FEATURES}
ENV KC_DB=${KC_DB}

# realm configuration
#COPY realm-export.json /

# preconfiguration script
COPY setup.sh /

RUN /opt/keycloak/bin/kc.sh build

# Second stage (RUNTIME)
FROM quay.io/keycloak/keycloak:17.0.0

ARG KEYCLOAK_ADMIN
ARG KEYCLOAK_ADMIN_PASSWORD

ARG KC_DB_URL
ARG KC_DB_USERNAME
ARG KC_DB_PASSWORD
ARG KC_HOSTNAME

ARG KC_HTTP_ENABLED=true
ARG KC_HTTP_PORT=8443
ARG KC_HTTP_HOST

# strict https
ARG KC_HOSTNAME_STRICT_HTTPS=true

#COPY --from=builder /realm-export.json /opt/keycloak/conf/realm-export.json
COPY --from=builder /setup.sh /opt/keycloak/bin/setup.sh

COPY --from=builder /opt/keycloak/lib/quarkus/ /opt/keycloak/lib/quarkus/
RUN apt-get update && apt-get install -y --no-install-recommends jq
WORKDIR /opt/keycloak

# for demonstration purposes only, please make sure to use proper certificates in production instead
#RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=ilikechocoalte" -alias meow -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

ENV KEYCLOAK_ADMIN=${KEYCLOAK_ADMIN}
ENV KEYCLOAK_ADMIN_PASSWORD=${KEYCLOAK_ADMIN_PASSWORD}

ENV KC_DB_URL=${KC_DB_URL}
ENV KC_DB_USERNAME=${KC_DB_USERNAME}
ENV KC_DB_PASSWORD=${KC_DB_PASSWORD}
ENV KC_HOSTNAME=${KC_HOSTNAME}

ENV KC_HTTP_ENABLED=${KC_HTTP_ENABLED}
ENV KC_HTTP_PORT=${KC_HTTP_PORT}
ENV KC_HOSTNAME_STRICT_HTTPS=${KC_HOSTNAME_STRICT_HTTPS}
ENV KEYCLOAK_FRONTEND_URL=${KC_HOSTNAME}

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start"]
