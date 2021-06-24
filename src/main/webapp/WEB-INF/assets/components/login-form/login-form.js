'use strict';

import Request from '../../utils/Request';
import User from '../../models/User';

export default {
    data: function(){
        return {
            display: false
        };
    },

    methods: {
        handleSubmit: function(event){
            event.preventDefault();
            event.stopPropagation();
            this.submit();
        },

        showFormErrorMessages: function(response){
            if ( response.messages !== null && typeof response.messages === 'object' ){
                if ( response.messages.account !== '' && typeof response.messages.account === 'string' ){
                    this.$refs.account.setErrorMessage(response.messages.account);
                }
                if ( response.messages.password !== '' && typeof response.messages.password === 'string' ){
                    this.$refs.password.setErrorMessage(response.messages.password);
                }
            }
        },

        submit: async function(){
            if ( this.validate() ){
                const account = this.$refs.account.getValue(), password = this.$refs.password.getValue();
                const response = await Request.post('/api/user/login', {
                    account: account,
                    password: password
                });console.log(response);
                if ( response === null || response.code >= 500 ){
                    this.$refs.password.setErrorMessage('An error occurred while logging you in, please retry later.');
                }else if ( response.code === 404 ){
                    this.$refs.password.setErrorMessage('No such user found.');
                }else if ( response.code === 403 ){
                    this.$refs.password.setErrorMessage('Provided credentials are not valid.');
                }else if ( response.code === 400 ){
                    this.showFormErrorMessages(response);
                }else{
                    this.$root.authenticatedUser = new User(response.data.user);
                    this.hide();
                    this.$parent.init();
                }
            }
        },

        validate: function(){
            const account = this.$refs.account.getValue(), password = this.$refs.password.getValue();
            const accountMessage = account === '' ? 'You must provide a valid account.' : null;
            const passwordMessage = password === '' ? 'You must provide a valid password.' : null;
            this.$refs.account.setErrorMessage(accountMessage);
            this.$refs.password.setErrorMessage(passwordMessage);
            return accountMessage === null && passwordMessage === null;
        },

        show: function(){
            this.display = true;
            this.$parent.setDisabled(true);
            return this;
        },

        hide: function(){
            this.display = false;
            this.$parent.setDisabled(false);
            return this;
        }
    }
};
