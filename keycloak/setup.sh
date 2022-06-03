#!/bin/sh

set -e
set -u
set -o pipefail
#set -x

# kcadm executable path
kcadm="/opt/keycloak/bin/kcadm.sh"

login() {
  local url="http://localhost:8080"
  local user="admin"
  local password="admin"
  local kcadm=$kcadm

  $kcadm config credentials --server $url --realm master --user $user --password $password
}

# These commands will be useful:
# /opt/keycloak/bin/kcadm.sh config credentials --server http://localhost:8080/ --realm master --user admin --password admin
# /opt/keycloak/bin/kcadm.sh get realms/master

# Creates the "hotel-realm" realm and imports realm configuration overwriting the existing one. 
# Creates a user for the realm and sets a password and role for it.
# Generates a new secret for the "hotel-client" client.
#login && $kcadm create realms -s realm=hotelapp -s enabled=true \
#  && $kcadm create roles -r hotelapp -s name=user -s 'description=Regular user with a limited set of permissions' \
#  && $kcadm create clients -r hotelapp -s clientId=customerapp -s enabled=true -s 'redirectUris=["http://localhost:8081/customer/*"]' -s 'webOrigins=["*"]' -s baseUrl=http://localhost:8081/customerapp -s adminUrl=http://localhost:8081/customer -s directAccessGrantsEnabled=true -s protocol=openid-connect -s publicClient=false -s clientAuthenticatorType=client-secret -s secret=zjpSKq1HEj2RRqiIpb -s bearerOnly=false -i \
#  && $kcadm create users -r hotelapp -s username=testuser -s enabled=true \
#  && $kcadm set-password -r hotelapp --username testuser --new-password meow \
#  && $kcadm add-roles --uusername testuser --rolename user -r hotelapp

# Creates a Master realm setup with one user for testing, uses usernames and a public client configuration
#login && $kcadm create roles -r master -s name=hotelapp-user -s 'description=Regular user with a limited set of permissions' \
#  && $kcadm update realms/master -s registrationAllowed=true -s rememberMe=true -s verifyEmail=false -s resetPasswordAllowed=true -s editUsernameAllowed=true \
#  && $kcadm create roles -r master -s name=hotelapp-admin -s 'description=Admin role for hotelapp' \
#  && $kcadm create clients -r master -s clientId=hotelapp -s enabled=true -s protocol=openid-connect -s 'redirectUris=["http://localhost:8081/*"]' -s 'webOrigins=["*"]' -s publicClient=true -i \
#  && $kcadm create users -r master -s username=testuser -s enabled=true \
#  && $kcadm set-password -r master --username testuser --new-password meow \
#  && $kcadm add-roles --uusername testuser --rolename hotelapp-user -r master

# Master realm setup with one user for testing, uses email for usernames and public client
login && $kcadm create roles -r master -s name=hotelapp-user -s 'description=Regular user with a limited set of permissions' \
  && $kcadm update realms/master -s registrationAllowed=true -s registrationEmailAsUsername=true -s rememberMe=true -s verifyEmail=false -s resetPasswordAllowed=true -s editUsernameAllowed=true -s accessTokenLifespan=300 \
  && $kcadm create roles -r master -s name=hotelapp-admin -s 'description=Admin role for hotelapp' \
  && $kcadm create clients -r master -s clientId=hotelapp-client -s enabled=true -s protocol=openid-connect -s 'redirectUris=["http://localhost:8081/*"]' -s 'webOrigins=["*"]' -s publicClient=true

ClientID=$($kcadm get clients -r master --fields clientId,id --format 'csv' | grep 'hotelapp-client' | grep -o -i -E '[0-9A-Fa-f]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}')

$kcadm update clients/$ClientID -r master -b '{ "attributes": {"access.token.lifespan":"300","saml.force.post.binding":"false","saml.multivalued.roles":"false","oauth2.device.authorization.grant.enabled":"false","backchannel.logout.revoke.offline.tokens":"false","saml.server.signature.keyinfo.ext":"false","use.refresh.tokens":"true","oidc.ciba.grant.enabled":"false","backchannel.logout.session.required":"true","client_credentials.use_refresh_token":"false","require.pushed.authorization.requests":"false","saml.client.signature":"false","pkce.code.challenge.method":"S256","id.token.as.detached.signature":"false","saml.assertion.signature":"false","saml.encrypt":"false","saml.server.signature":"false","exclude.session.state.from.auth.response":"false","saml.artifact.binding":"false","saml_force_name_id_format":"false","acr.loa.map":"{}","tls.client.certificate.bound.access.tokens":"false","saml.authnstatement":"false","display.on.consent.screen":"false","token.response.type.bearer.lower-case":"false","saml.onetimeuse.condition":"false"}}' \
  && $kcadm create users -r master -s email=testuser@meow.com -s enabled=true \
  && $kcadm set-password -r master --username testuser@meow.com --new-password meow \
  && $kcadm add-roles --uusername testuser@meow.com --rolename hotelapp-user -r master

# Gets the client with key-value pair of "clientId:" "hotelapp-client" and returns the ID.
#clientID=$kcadm get clients -r master | jq -r '.[] | select(.clientId=="hotelapp-client") | .id'

