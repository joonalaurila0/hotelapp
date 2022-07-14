import Keycloak from 'keycloak-js';
import Api from './api/api';
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
      silentCheckSsoRedirectUri: window.location.origin + ':443/silent-check-sso.html',
      flow: 'standard',
      adapter: 'default',
      enableLogging: true,
      useNonce: true,
      redirectUri: `https://${process.env.swarmhost}`,
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

      console.debug('KC Profile: ', profile);
      State.storeStateToLocalStorage('profile', profile);
      // Create profile for the customer-service
      if (profile.id) {
        Api.doesCustomerExists(profile.id).then((res) =>
          res.json().then((body) => {
            /* Test what the customer-service returned,
             * creates the customer for the customer-service
             * incase it does not already exists in there. */
            //console.debug('Response from doesCustomerExists :: ', body);
            body == 1
              ? null // console.debug('Customer exists.')
              : body == 0 && profile.id && profile.email
              ? Api.createCustomer(profile.id, profile.email)
              : console.error('Something went wrong', body);
          })
        );
      }
    });
  };
};

export { keycloak, keycloakInit };
