import Keycloak from 'keycloak-js';
import State from './state';

// Keycloak configuration
// src/homepage/main/main.tsx has functions for keycloak.
const keycloak = new Keycloak({
  url: `http://${process.env.swarmhost}:8080/`,
  realm: 'master',
  clientId: 'hotelapp-client',
});

// Simple initialization function.
const keycloakInit = () => {
  // To keep track of keycloak initialization
  State.storeStateToLocalStorage('KC_INITIALIZED', true);

  keycloak
    .init({
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      flow: 'standard',
      adapter: 'default',
      enableLogging: true,
      useNonce: true,
      redirectUri: `http://${process.env.swarmhost}:8081`,
      pkceMethod: 'S256',
    })
    .then((auth) => {
      auth
        ? console.info('Authenticated with keycloak')
        : console.info('Not authenticated with keycloak');
    })
    .catch((e) => console.error(e));

  // minValidity is seconds
  // https://www.keycloak.org/docs/latest/securing_apps/#updatetoken-minvalidity
  setInterval(() => {
    keycloak
      .updateToken(300)
      .then((refreshed) => {
        if (refreshed) {
          console.info('Token was succesfully refreshed');
        } else {
          console.info('Token is still valid');
        }
      })
      .catch(() => console.info('Failed to refresh the token, or the session had expired'));
  }, 300000); // 5 minutes in milliseconds

  // On succesfull authentication, calls state to store the user information as JSON string to LocalStorage.
  keycloak.onAuthSuccess = () => {
    //console.debug('Authenticated successfully with Keycloak');
    keycloak.loadUserProfile().then((profile) => {
      State.storeStateToLocalStorage('token', keycloak.token ?? null);
      //console.debug('KC Profile: ', profile);
      State.storeStateToLocalStorage('profile', profile);
    });
  };
};

export { keycloak, keycloakInit };
