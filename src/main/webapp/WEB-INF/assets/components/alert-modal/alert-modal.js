'use strict';

export default {
    data: function(){
        return {
            text: '',
            type: 'alert',
            onClose: null,
            onConfirm: null,
            display: false
        };
    },

    methods: {
        show: function(text, type = 'alert', onClose, onConfirm){
            this.text = text;
            this.type = type;
            this.onClose = onClose;
            this.onConfirm = onConfirm;
            this.display = true;
            return this;
        },

        showNetworkErrorMessage: function(callback, allowRetry = true){
            this.text = 'An error occurred while contacting the server, please retry later.';
            this.type = allowRetry !== false ? 'network-alert' : 'alert';
            this.onClose = callback;
            this.onConfirm = null;
            this.display = true;
        },

        hide: function(){
            this.display = false;
            this.text = '';
            this.type = 'alert';
            this.onClose = this.onConfirm = null;
            return this;
        },

        confirm: function(){
            if ( typeof this.onConfirm === 'function' ){
                this.onConfirm();
            }
            return this.hide();
        },

        cancel: function(){
            if ( typeof this.onClose === 'function' ){
                this.onClose();
            }
            return this.hide();
        }
    }
};
