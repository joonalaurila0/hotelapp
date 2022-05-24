//import Keycloak from "keycloak-js";
//
//// Keycloak configuration
//const keycloak = Keycloak({
//  url: 'http://localhost:8080/',
//  realm: 'master',
//  clientId: 'react-hotelapp',
//});
// 
//keycloak.init({
//  onLoad: 'check-sso',
//  flow: 'standard',
//  adapter: 'default',
//  enableLogging: true,
//  useNonce: true,
//}).then((auth) => {
//  if (auth) {
//    console.info("authenticated")
//  } else {
//    window.location.reload();
//  }
//})
//
//export { keycloak };

//export const keycloakInit = () => {
//
//  keycloak.init({
//    onLoad: 'check-sso',
//    silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
//    flow: 'standard',
//    pkceMethod: 'S256',
//    adapter: 'default',
//    enableLogging: true,
//    useNonce: true,
//
//  }).then((authenticated) => {
//    !authenticated ? window.alert('User is not authenticated') : console.log('User is authenticated');
//  }).catch((err) => {
//    throw new Error(err);
//  });
//
//  //keycloak.updateToken(5).then((refreshed) => {
//  //  if (refreshed) {
//  //    alert('Token was succesfully refreshed');
//  //  } else {
//  //    alert ('Token is still valid');
//  //  }
//  //})
//
//  //keycloak.onAuthSuccess = () => { alert('Authenticated successfully') }
//}

