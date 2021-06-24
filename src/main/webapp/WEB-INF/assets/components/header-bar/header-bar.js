'use strict';

module.exports = {
    methods: {
        openLateralMenu: function(){
            this.$root.$refs.app.getLateralMenu().openMenu();
            return this;
        }
    }
};
