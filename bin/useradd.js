'use strict';

const lib = require('./lib/lib');

(async () => {
    const stream = lib.createInputStream(), connection = lib.createDatabaseConnection();
    const account = await lib.ask('What is the account name? ', stream);
    const password = await lib.ask('What is the account password? ', stream);
    let role = await lib.ask('What is this user role (user/admin)? ', stream);
    role = role.toLowerCase();
    if ( account === '' ){
        console.log('You must provide a valid account name!');
        process.exit();
    }
    if ( password === '' ){
        console.log('You must provide a valid account password!');
        process.exit();
    }
    if ( role !== 'user' && role !== 'admin' ){
        console.log('You must provide a valid user role!');
        process.exit();
    }
    const cocktail = await lib.generatePasswordCocktail(password);
    const query = 'INSERT INTO users (account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (?, ?, ?, ?, ?, ?);';
    await lib.query(query, [account, cocktail.salt, cocktail.pepper, cocktail.hash, cocktail.loop, role], connection);
    console.log('User created!');
    process.exit();
})();
