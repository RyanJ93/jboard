'use strict';

class User {
    #id = 0;
    #account = null;
    #role = null;

    #setProperties(properties){
        this.#id = !isNaN(properties.id) && properties.id > 0 ? properties.id : 0;
        this.#account = typeof properties.account === 'string' ? properties.account : null;
        this.#role = typeof properties.role === 'string' ? properties.role : null;
    }

    constructor(properties = null){
        if ( properties !== null && typeof properties === 'object' ){
            this.#setProperties(properties);
        }
    }

    getID(){
        return this.#id;
    }

    getAccount(){
        return this.#account;
    }

    getRole(){
        return this.#role;
    }
}

Object.defineProperty(User, 'ROLE_USER', {
    value: 'user',
    writable: false
});

Object.defineProperty(User, 'ROLE_ADMIN', {
    value: 'admin',
    writable: false
});

export default User;
