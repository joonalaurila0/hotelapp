#!/bin/sh

set -e
set -u
set -o pipefail
#set -x

# kcadm executable path
kcadm="/opt/keycloak/bin/kcadm.sh"

# Master node
swarmhost="192.168.1.107"

login() {
  local url="http://localhost:8080"
  local user="admin"
  local password="admin"
  local kcadm=$kcadm

  $kcadm config credentials --server $url --realm master --user $user --password $password
}

redirectVar="[\"http://${swarmhost}:8080/*\", \"http://${swarmhost}:8081/*\"]"
webOrigins="[\"http://${swarmhost}:8081\", \"http://${swarmhost}:8072\"]"

# Master realm setup with one user for testing, uses email for usernames and public client
login && $kcadm create roles -r master -s name=hotelapp-user -s 'description=Regular user with a limited set of permissions' \
  && $kcadm update realms/master -s registrationAllowed=true -s registrationEmailAsUsername=true -s rememberMe=true -s verifyEmail=false -s resetPasswordAllowed=true -s editUsernameAllowed=true -s accessTokenLifespan=300 \
  && $kcadm create roles -r master -s name=hotelapp-admin -s 'description=Admin role for hotelapp' \
  && $kcadm create clients -r master -s clientId=hotelapp-client -s enabled=true -s protocol=openid-connect -s "redirectUris=$redirectVar" -s "webOrigins=$webOrigins" -s publicClient=true

ClientID=$($kcadm get clients -r master --fields clientId,id --format 'csv' | grep 'hotelapp-client' | grep -o -i -E '[0-9A-Fa-f]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}')

$kcadm update clients/$ClientID -r master -b '{ "attributes": {"access.token.lifespan":"300","saml.force.post.binding":"false","saml.multivalued.roles":"false","oauth2.device.authorization.grant.enabled":"false","backchannel.logout.revoke.offline.tokens":"false","saml.server.signature.keyinfo.ext":"false","use.refresh.tokens":"true","oidc.ciba.grant.enabled":"false","backchannel.logout.session.required":"true","client_credentials.use_refresh_token":"false","require.pushed.authorization.requests":"false","saml.client.signature":"false","pkce.code.challenge.method":"S256","id.token.as.detached.signature":"false","saml.assertion.signature":"false","saml.encrypt":"false","saml.server.signature":"false","exclude.session.state.from.auth.response":"false","saml.artifact.binding":"false","saml_force_name_id_format":"false","acr.loa.map":"{}","tls.client.certificate.bound.access.tokens":"false","saml.authnstatement":"false","display.on.consent.screen":"false","token.response.type.bearer.lower-case":"false","saml.onetimeuse.condition":"false"}}' \
  && $kcadm create users -r master -s email=testuser@meow.com -s enabled=true \
  && $kcadm set-password -r master --username testuser@meow.com --new-password meow \
  && $kcadm add-roles --uusername testuser@meow.com --rolename hotelapp-user -r master
