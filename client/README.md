## Install

Via npm or yarn

``` bash
$ npm i react@17.0.2 react-dom@17.0.2
$ npm i -D typescript@4.4.3 webpack@5.54.0 webpack-cli@4.8.0 express@4.17.1 webpack-dev-middleware@5.2.1 style-loader@3.3.0 css-loader@6.3.0 html-webpack-plugin@5.3.2 @babel/core@7.15.5 @babel/cli@7.15.7 @babel/preset-env@7.15.6 @babel/preset-react@7.14.5 @babel/preset-typescript@7.15.0 @types/react@17.0.24 @types/react-dom@17.0.9 babel-loader@8.2.2 dotenv@10.0.0 express@4.17.1
```
* This installation was done on npm v7.24.0 and node v16.9.1

* This setup uses tsc for typechecking and babel for compiling, webpack runs babel underneath all the abstraction through babel-loader and babel manages to understand typescript through @babel-preset-typescript and the same is true for handling jsx.

* It is not necessary to setup webpack's configs using typescript, as you will need to install @types/webpack-dev-server@4.1.0 @types/webpack@5.28.0, this is however completely redundant

* babel-loader is important as it links babel and webpack, allowing webpack to use babel automatically

* webpack server runs the webpack dev server, [link to reference for webpack server](https://github.com/webpack/webpack-cli/blob/master/SERVE-OPTIONS-v4.md)

* You can run development server using webpack-dev-server by just using `npm run dev` or by using express server setup with webpack-dev-middleware by using `npm run server`

* You can also run this site by first building the site and then running it through server.js with `node server.js`

### About

This is second attempt at putting together somewhat coherent typescript react setup. This version attempts to do away with webpack-merge and webpack-dev-server using simpler webpack.config.js with conditionals and webpack-dev-middleware with express server instead. If env.production is true, then the webpack.config.js returns productionConfig object, otherwise it merges developmentConfig and productionConfig using rest parameters, the developmentConfig keys will replace and add to productionConfig's in this instance so it returns an config object for development.

I also use this functionality for the express server passing production key as false so it returns the devlopment setup and I also had to make it so that webpackDevMiddleware function reads compiler.options.output.publicPath. This is because webpackDevMiddleware's second argument takes in options object, how it's show n in Webpack 5's documentation in the Guides>Development section, webpackDevMiddleware, takes output of webpack function and as options (second argument), it takes `config.output.publicPath`, which is the output of webpack.config.js.

I achieve the same but I just pass it from the compiler instead.

There is a backup config for webpack `webpack.backup.js`, incase this functionality breaks further down the line, which is almost guaranteed.

[webpack-dev-middleware](https://github.com/webpack/webpack-dev-middleware/blob/master/src/index.js)

### Currently using the new JSX Transform from React 17

<p>Babelrc has the react using the new jsx transform feature that allows importing react without explicit import. <a href="https://reactjs.org/blog/2020/09/22/introducing-the-new-jsx-transform.html">More here</a></p>

