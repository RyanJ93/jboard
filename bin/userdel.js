'use strict';

const lib = require('./lib/lib');

(async () => {
    const stream = lib.createInputStream(), connection = lib.createDatabaseConnection();
    const account = await lib.ask('What is the account you want to remove? ', stream);
    if ( account === '' ){
        console.log('You must provide a valid account name!');
        process.exit();
    }
    const query = 'DELETE FROM users WHERE account = ?;';
    await lib.query(query, [account], connection);
    console.log('Account deleted!');
    process.exit();
})();
