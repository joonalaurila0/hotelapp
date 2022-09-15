# About

The client for the application is a running on TypeScript 4.5.4, React 17.0.2 and the application running on Webpack 5 for module bundling and Babel 7 for polyfilling (+ES6 to >=ES5) and more (jsx etc.). The deployment happens through nginx web server. To change the deployment variables, you can use an .env file, webpack.config.js and conf/nginx.conf.

# How to run

`$ npm run serve`

# How to build

`$ npm run build`

# How to deploy

`$ make build deploy`

# To sign in to the application

You can use an existing user: 
  email: `testuser@meow.com` 
  password: `meow123` 

By pressing at the 'sign in' link, you will be directed to keycloak's login page to perform the authentication.
