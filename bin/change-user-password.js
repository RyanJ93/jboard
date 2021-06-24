'use strict';

const lib = require('./lib/lib');

(async () => {
    const stream = lib.createInputStream(), connection = lib.createDatabaseConnection();
    const account = await lib.ask('What is the account name you want to change password for? ', stream);
    const password = await lib.ask('What is the password you want to set? ', stream);
    if ( account === '' ){
        console.log('You must provide a valid account name!');
        process.exit();
    }
    if ( password === '' ){
        console.log('You must provide a valid account password!');
        process.exit();
    }
    const cocktail = await lib.generatePasswordCocktail(password);
    const query = 'UPDATE users SET password_salt = ?, password_pepper = ?, password_hash = ?, password_loop = ? WHERE account = ?;';
    await lib.query(query, [cocktail.salt, cocktail.pepper, cocktail.hash, cocktail.loop, account], connection);
    console.log('Password changed!');
    process.exit();
})();
