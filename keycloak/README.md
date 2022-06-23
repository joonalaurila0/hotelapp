# Running

Running:
```bash
$ make deploy
$ make init
```

will deploy and initialize the vault with data and premade configuration.

# Initialization

Initialization happens through the startup.sh script that also calls the setup.sh script inside the container to initialize default values.

# Enabling SSL/HTTPS for the Keycloak server

```
curl -X POST "http://localhost:8080/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data-urlencode "grant_type=password" \
  --data-urlencode "username=test@user.com" \
  --data-urlencode "password=123" \
  --data-urlencode "scope=hotelapp" \
  --data-urlencode "client_id=hotelapp"
```

* [Sauce](https://linuxconfig.org/how-to-generate-a-self-signed-ssl-certificate-on-linux)

If you're not using a reverse proxy or load balancer to handle HTTPS traffic for you, you'll need to enable HTTPS for the Keycloak server.

1. Obtain or generate a keystore that contains the private key and certificate for SSL/HTTP traffic.
2. Configure the Keycloak server to use this keypair and certificate.

## Creating the Certificate and Java Keystore

In order to allow HTTPS connections, you need to obtain a self signed or third-party signed certificate and import it into a Java keystore before you can enable HTTPS in the web container where you are deploying the Keycloak Server.

* To generate our certificate, together with a private key, we need to run req with the -newkey.

Run: `$ openssl req -newkey rsa:4096  -x509  -sha512  -days 365 -nodes -out certificate.pem -keyout privatekey.pem`

This creates an RSA key of 4096 bits. If we omit key size, it defaults to 2048. The '-x509' flag specifies a standard format for public key certificates, this enables us to create self-signed certificate instead of a certificate request.

The '-sha512' is the message digest [Message Digest](https://www.techopedia.com/definition/4024/message-digest).

The '-days 365' specifies the validity period in days (30 is the default), so this makes the certificate value for a whole year.

With the '-nodes' option we specified that we don’t want to encrypt the generated private key. Encrypting the private key is without doubts useful: this can be intended as a security measure in case someone stoles it, since to use it, a passphrase should be provided. Just as an example, if we use a private key with Apache, we must provide the passphrase to decrypt it each time we restart the daemon. In this case, since we are generating a self sign certificate, which we will use for testing, therefore we can avoid encrypting the private key.

Finally, we used the -out and -keyout options to specify the filenames to write the certificate and the key to, respectively. In this case the certificate will be saved into the certificate.pem file, and the private key into the privatekey.pem file. Why did we use “.pem” as the file names suffix? This is because both the certificate and key will be created in PEM format. PEM stands for “Privacy Enhanced Mail”: it is basically a container which includes base64 formatted data.

# kcadm / admin-cli for keycloak -- CLI for keycloak

[Docs](https://github.com/keycloak/keycloak-documentation/blob/main/server_admin/topics/admin-cli.adoc)
[Source](https://github.com/keycloak/keycloak/blob/main/integration/client-cli/admin-cli/src/main/bin/kcadm.sh)

Examples:

## Get a field from realm config and update it:

```bash
$ /opt/keycloak/bin/kcadm.sh get realms/master --fields accessTokenLifespan`
$ /opt/keycloak/bin/kcadm.sh update realms/master -s accessTokenLifespan=59`
```

## Update credentials of a realm:

```bash
$ /opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080/ --realm master --user admin --password admin`
$ /opt/keycloak/bin/kcadm.sh get realms/master`
```

## Get all the clients of a realm:

```bash
$ /opt/keycloak/bin/kcadm.sh get clients -r master
```

## Get all the clients of a realm but only the ID and ClientID fields:

```bash
$ /opt/keycloak/bin/kcadm.sh get clients -r master --fields 'id,clientId' | jq
```

## Get specific client information by ID:

```bash
$ /opt/keycloak/bin/kcadm.sh get clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master | jq
```

## Getting and updating a value in a client in the master realm:

```bash
$ /opt/keycloak/bin/kcadm.sh get clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master --fields directAccessGrantsEnabled | jq
$ /opt/keycloak/bin/kcadm.sh update clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master -s directAccessGrantsEnabled=false
```

## Getting an object of elements, here `(*)` is the fields of the object, you can also specify fields there:

```bash
$ /opt/keycloak/bin/kcadm.sh get clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master --fields 'attributes(*)' | jq
```

So to get particular fields from a nested object, you'd write it like so:
```bash
$ /opt/keycloak/bin/kcadm.sh get clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master --fields 'attributes(pkce.code.challenge.method)' | jq
```


## Updating a nested value in an object and to see the result:

```bash
$ /opt/keycloak/bin/kcadm.sh update clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master -b '{ "attributes": { "access.token.lifespan": "310" }}'
$ /opt/keycloak/bin/kcadm.sh get clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master --fields 'attributes(access.token.lifespan)' | jq
```
NOTE:
(these get merged automatically and it wont remove nor mutate any other values except the one you have specified, check source for more):
[Source code is here for the update command](https://github.com/keycloak/keycloak/blob/main/integration/client-cli/admin-cli/src/main/java/org/keycloak/client/admin/cli/commands/UpdateCmd.java)

If you wanted to update entire attributes object:
```bash
$ /opt/keycloak/bin/kcadm.sh update clients/ad30971c-c3a2-4e82-82dc-81d8477708de -r master -b '{ "attributes": {"access.token.lifespan":"300","saml.force.post.binding":"false","saml.multivalued.roles":"false","oauth2.device.authorization.grant.enabled":"false","backchannel.logout.revoke.offline.tokens":"false","saml.server.signature.keyinfo.ext":"false","use.refresh.tokens":"true","oidc.ciba.grant.enabled":"false","backchannel.logout.session.required":"true","client_credentials.use_refresh_token":"false","require.pushed.authorization.requests":"false","saml.client.signature":"false","pkce.code.challenge.method":"S256","id.token.as.detached.signature":"false","saml.assertion.signature":"false","saml.encrypt":"false","saml.server.signature":"false","exclude.session.state.from.auth.response":"false","saml.artifact.binding":"false","saml_force_name_id_format":"false","acr.loa.map":"{}","tls.client.certificate.bound.access.tokens":"false","saml.authnstatement":"false","display.on.consent.screen":"false","token.response.type.bearer.lower-case":"false","saml.onetimeuse.condition":"false"}}'
```


## Creating client with particular specifications and attributes:
```bash
$ /opt/keycloak/bin/kcadm.sh create clients -r master -f - << EOF
{
  "id": "ad30971c-c3a2-4e82-82dc-81d8477708de",
  "clientId": "hotelapp-client",
  "surrogateAuthRequired": false,
  "enabled": true,
  "alwaysDisplayInConsole": false,
  "clientAuthenticatorType": "client-secret",
  "redirectUris": [
    "http://localhost:8081/*"
  ],
  "webOrigins": [
    "*"
  ],
  "notBefore": 0,
  "bearerOnly": false,
  "consentRequired": false,
  "standardFlowEnabled": true,
  "implicitFlowEnabled": false,
  "directAccessGrantsEnabled": false,
  "serviceAccountsEnabled": false,
  "publicClient": true,
  "frontchannelLogout": false,
  "protocol": "openid-connect",
  "attributes": {
    "access.token.lifespan": "300",
    "saml.force.post.binding": "false",
    "saml.multivalued.roles": "false",
    "oauth2.device.authorization.grant.enabled": "false",
    "backchannel.logout.revoke.offline.tokens": "false",
    "saml.server.signature.keyinfo.ext": "false",
    "use.refresh.tokens": "true",
    "oidc.ciba.grant.enabled": "false",
    "backchannel.logout.session.required": "true",
    "client_credentials.use_refresh_token": "false",
    "require.pushed.authorization.requests": "false",
    "saml.client.signature": "false",
    "pkce.code.challenge.method": "S256",
    "id.token.as.detached.signature": "false",
    "saml.assertion.signature": "false",
    "saml.encrypt": "false",
    "saml.server.signature": "false",
    "exclude.session.state.from.auth.response": "false",
    "saml.artifact.binding": "false",
    "saml_force_name_id_format": "false",
    "acr.loa.map": "{}",
    "tls.client.certificate.bound.access.tokens": "false",
    "saml.authnstatement": "false",
    "display.on.consent.screen": "false",
    "token.response.type.bearer.lower-case": "false",
    "saml.onetimeuse.condition": "false"
  },
  "authenticationFlowBindingOverrides": {},
  "fullScopeAllowed": true,
  "nodeReRegistrationTimeout": -1,
  "defaultClientScopes": [
    "web-origins",
    "roles",
    "profile",
    "email"
  ],
  "optionalClientScopes": [
    "address",
    "phone",
    "offline_access",
    "microprofile-jwt"
  ],
  "access": {
    "view": true,
    "configure": true,
    "manage": true
  }
}
EOF
```

## Getting, updating and creating users

To get all the users of a realm:
```bash
$ /opt/keycloak/bin/kcadm.sh get users -r master | jq
```

To set a custom attribute(s) on a user:
```bash
$ /opt/keycloak/bin/kcadm.sh update users/f4c91088-f67f-471a-90b5-40a35693920c -b '{"attributes":{"dob":["1970-05-28"]}}'
```

To create a user and password for the user:
```bash
$ /opt/keycloak/bin/kcadm.sh update users/f4c91088-f67f-471a-90b5-40a35693920c create users -r demorealm -s username=testuser -s enabled=true -o --fields id,username
$ /opt/keycloak/bin/kcadm.sh set-password -r master --username testuser --new-password meow \
```
