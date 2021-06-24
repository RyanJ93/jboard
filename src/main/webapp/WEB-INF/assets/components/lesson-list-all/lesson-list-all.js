'use strict';

export default {
    methods: {
        init: function(){
            return this.$refs.list.init();
        },

        reset: function(){
            this.$refs.list.reset();
            return this;
        },

        reload: function(){
            return this.$refs.list.reload();
        }
    }
};
