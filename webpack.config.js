'use strict';

const path = require('path');
const VueLoaderPlugin = require('vue-loader/lib/plugin')
const WebpackNotifierPlugin = require('webpack-notifier');

module.exports = {
    entry: {
        index: ['babel-polyfill', './src/main/webapp/WEB-INF/assets/index.js']
    },
    output: {
        filename: 'main.js',
        path: path.resolve(__dirname, 'src/main/webapp/static')
    },
    resolve: {
        extensions: ['*', '.js'],
        alias: {
            vue: 'vue/dist/vue.js'
        }
    },
    module: {
        rules: [{
            test: /\.js$/,
            exclude: /node_modules/,
            use: {
                loader: "babel-loader"
            }
        }, {
            test: /\.scss$/,
            use: [{
                loader: "style-loader"
            }, {
                loader: "css-loader",
                options: {
                    modules: true
                }
            }, {
                loader: "sass-loader"
            }]
        }, {
            test: /\.vue$/,
            loader: 'vue-loader'
        }]
    },
    plugins: [
        new VueLoaderPlugin(),
        new WebpackNotifierPlugin()
    ]
};
