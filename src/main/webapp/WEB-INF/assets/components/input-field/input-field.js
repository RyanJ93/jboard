'use strict';

export default {
    props: ['placeholder', 'type'],

    data: () => {
        return {
            errorMessage: null
        };
    },

    methods: {
        setErrorMessage: function(errorMessage){
            this.errorMessage = errorMessage === '' || typeof errorMessage !== 'string' ? null : errorMessage;
            return this;
        },

        setValue: function(value){
            this.$refs.input.value = value;
            return this;
        },

        getValue: function(){
            return this.$refs.input.value;
        }
    }
};
