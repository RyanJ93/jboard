'use strict';

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
        submit: function(){
            if ( this.validate() ){
                const request = new XMLHttpRequest();
                request.open('POST', '/app/api/user/login', true);
                request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( request.response === null || request.response.code >= 500 ){
                            this.$refs.password.setErrorMessage('An error occurred while logging you in, please retry later.');
                        }else if ( request.response.code === 404 ){
                            this.$refs.password.setErrorMessage('No such user found.');
                        }else if ( request.response.code === 403 ){
                            this.$refs.password.setErrorMessage('Provided credentials are not valid.');
                        }else{
                            this.hide();
                            this.$parent.init();
                        }
                    }
                };
                const account = this.$refs.account.getValue();
                const password = this.$refs.password.getValue();
                request.send('account=' + encodeURIComponent(account) + '&password=' + encodeURIComponent(password));
            }
        },
        validate: function(){
            const account = this.$refs.account.getValue(), password = this.$refs.password.getValue();
            const accountMessage = account === '' ? 'Invalid account.' : null;
            const passwordMessage = password === '' ? 'Invalid password.' : null;
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
