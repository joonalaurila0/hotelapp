const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
require('dotenv').config({ path: '../.env' })
const webpack = require('webpack');

const ASSET_PATH = process.env.asset_path || '/'

module.exports = (env) => {
  const developmentConfig = {
    mode: 'development',
    devtool: 'eval-cheap-module-source-map',
    devServer: {
      historyApiFallback: true,
      static: './dist',
      hot: true,
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
  }
  const productionConfig = {
    mode: 'production',
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
            test: /\.(png|svg|jpg|jpeg|gif)$/i,
            type: 'asset/resource',
          },
        ],
      },
      plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, './public/index.html'),
      title: 'Hotely | Find excellent hotels',
      filename: 'index.html',
      favicon: path.resolve(__dirname, './public/static/fierce.jpg'),
      cache: true
    }),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, './public/silent-check-sso.html'),
      filename: 'silent-check-sso.html',
    }),
      ],
      stats: {
        env: true,
      },
  }
  return env.production ? productionConfig : { ...productionConfig, ...developmentConfig }
}
