'use strict';

const readline = require('readline');
const crypto = require('crypto');
const mysql = require('mysql');
const fs = require('fs');

module.exports.getConfig = function(){
    let configPath = null, defaultConfigPath = __dirname + '/../../config/config.json';
    if ( fs.existsSync(defaultConfigPath) ){
        configPath = defaultConfigPath;
    }else{
        let path = process.env.JBOARD_CONFIG_PATH;
        if ( path !== '' && typeof path === 'string' && fs.existsSync(path) ){
            configPath = path;
        }
    }
    if ( configPath === null ){
        throw new Error('No configuration file found.');
    }
    const contents = fs.readFileSync(configPath).toString();
    return JSON.parse(contents);
};

module.exports.createDatabaseConnection = function(){
    const config = module.exports.getConfig();
    if ( config.database === null || typeof config.database !== 'object' ){
        throw new Error('No database configuration defined!');
    }
    const host = typeof config.database.host === 'string' && config.database.host !== '' ? config.database.host : '127.0.0.1';
    const port = config.database.port > 0 && config.database.port <= 25565 ? config.database.port : 3306;
    const username = typeof config.database.username === 'string' && config.database.username !== '' ? config.database.username : null;
    const password = typeof config.database.password === 'string' && config.database.password !== '' ? config.database.password : null;
    if ( config.database.database === '' || typeof config.database.database !== 'string' ){
        throw new Error('No database name defined.');
    }
    const connection = mysql.createConnection({
        host: host,
        port: port,
        user: username,
        password: password,
        database: config.database.database
    });
    connection.connect();
    return connection;
};

module.exports.generateToken = function(length){
    return new Promise((resolve, reject) => {
        crypto.randomBytes(length / 2, (error, bytes) => {
            if ( error !== null ){
                return reject(error);
            }
            resolve(bytes.toString('hex'));
        });
    });
};

module.exports.ask = function(question, stream){
    return new Promise((resolve) => {
        stream.question(question, (answer) => resolve(answer));
    });
};

module.exports.query = function(query, params, connection){
    return new Promise((resolve, reject) => {
        connection.query(query, params, (error, result) => {
            if ( error !== null ){
                return reject(error);
            }
            resolve(result);
        });
    })
};

module.exports.createInputStream = function(){
    return readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });
};

module.exports.generatePasswordCocktail = async function(password){
    const [salt, pepper] = await Promise.all([
        module.exports.generateToken(32),
        module.exports.generateToken(32)
    ]);
    let loop = 3, hash = salt + password + pepper;
    for ( let i = 0 ; i < loop ; i++ ){
        hash = crypto.createHash('sha512').update(hash).digest().toString('hex');
    }
    return {
        salt: salt,
        pepper: pepper,
        hash: hash,
        loop: loop
    };
};
