const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
require('dotenv').config({ path: './.env' });
const webpack = require('webpack');
const dotenv = require('dotenv-webpack');

const ASSET_PATH = process.env.asset_path || '/';
const host = process.env.host || '127.0.0.1';

module.exports = (env) => {
  const developmentConfig = {
    mode: 'development',
    target: 'web',
    devtool: 'eval-cheap-module-source-map',
    devServer: {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
        'Access-Control-Allow-Headers': 'X-Requested-With, content-type, Authorization',
      },
      historyApiFallback: true,
      static: './dist',
      hot: true,
      host: host,
      port: process.env.port,
      open: true,
    },
    optimization: {
      moduleIds: 'deterministic',
      runtimeChunk: 'single',
      splitChunks: {
        cacheGroups: {
          vendor: {
            test: /[\\/]node_modules[\\/]/,
            name: 'vendors',
            chunks: 'all',
          },
        },
      },
    },
  };
  const productionConfig = {
    mode: 'production',
    target: 'web',
    entry: {
      index: path.resolve(__dirname, './src/index.tsx'),
    },
    output: {
      filename: '[name].[contenthash].bundle.js',
      path: path.resolve(__dirname, './dist'),
      clean: true,
      publicPath: ASSET_PATH,
    },
    resolve: {
      extensions: ['.tsx', '.ts', '.js'],
    },
    devtool: 'source-map',
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          loader: 'babel-loader',
          exclude: /node_modules/,
        },
        {
          test: /\.css$/i,
          use: ['style-loader', 'css-loader'],
        },
        {
          test: /\.(png|svg|jpg|jpeg|gif|webp)$/i,
          type: 'asset/resource',
        },
      ],
    },
    plugins: [
      new dotenv(),
      new HtmlWebpackPlugin({
        template: path.resolve(__dirname, './public/index.html'),
        title: 'Hotely | Find excellent hotels',
        filename: 'index.html',
        favicon: path.resolve(__dirname, './public/static/favicon-32x32.png'),
        cache: true,
      }),
      new HtmlWebpackPlugin({
        template: path.resolve(__dirname, './public/silent-check-sso.html'),
        filename: 'silent-check-sso.html',
      }),
    ],
    stats: {
      env: true,
    },
  };
  return env.production ? productionConfig : { ...productionConfig, ...developmentConfig };
};
