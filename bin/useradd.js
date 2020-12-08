'use strict';

const readline = require('readline');
const crypto = require('crypto');
const mysql = require('mysql');

function generateToken(length){
    return new Promise((resolve, reject) => {
        crypto.randomBytes(length / 2, (error, bytes) => {
            if ( error !== null ){
                return reject(error);
            }
            resolve(bytes.toString('hex'));
        });
    });
}

const connection = mysql.createConnection({
    host: '127.0.0.1',
    user: 'root',
    password: 'mysql_password',
    database: 'jboard'
});
const stream = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
connection.connect();
stream.question('What is the account name? ', (account) => {
    stream.question('What is the account password? ', (password) => {
        Promise.all([generateToken(32), generateToken(32)]).then((components) => {
            let loop = Math.random() * (256 - 128) + 128;loop=3;
            let hash = components[0] + password + components[1];
            for ( let i = 0 ; i < loop ; i++ ){
                hash = crypto.createHash('sha512').update(hash).digest().toString('hex');
            }
            const query = 'INSERT INTO users (account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (?, ?, ?, ?, ?, ?);';
            connection.query(query, [account, components[0], components[1], hash, loop, 'user'], (error) => {
                if ( error !== null ){
                    console.log(error);
                }
                console.log('User created!');
                process.exit();
            });
        }).catch((ex) => console.log(ex));
    });
});
