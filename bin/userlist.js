'use strict';

const lib = require('./lib/lib');

(async () => {
    const connection = lib.createDatabaseConnection();
    const resultSet = await lib.query('SELECT id, account, role FROM users;', null, connection);
    console.table(resultSet);
    console.log('Account deleted!');
    process.exit();
})();
