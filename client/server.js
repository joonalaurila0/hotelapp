const express = require('express');
const webpack = require('webpack');
const webpackDevMiddleware = require('webpack-dev-middleware');

const history = require('connect-history-api-fallback');

const app = express();
const config = require('./webpack.config.js');
const compiler = webpack(config({ production: false }));

const host = process.env.host || `127.0.0.1`;
const port = process.env.port || 8081;

console.group('WEBPACK OPTIONS: ', compiler.options);

app.use(history({ verbose: true }));

// Tell express to use the webpack-dev-middleware and use the webpack.config.js
// configuration file as a base.
app.use(
  webpackDevMiddleware(compiler, {
    publicPath: compiler.options.output.publicPath,
  })
);

app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, PUT, PATCH, DELETE');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
  next();
});

// Serve the files on port 8081.
app.listen(port, () => {
  console.log(`app listening on ${host}:${port} \n mode: ${process.env.NODE_ENV}\n`);
});
