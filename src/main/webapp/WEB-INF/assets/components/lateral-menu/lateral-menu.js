'use strict';

import Request from '../../utils/Request';

export default {
    data: function(){
        return {
            views: this.$root.views,
            activeView: null
        };
    },

    methods: {
        refreshAvailableViews: function(){
            const authenticatedUserRole = this.$root.authenticatedUser?.getRole();
            this.views = this.$root.views.filter((view) => view.targetRoles.indexOf(authenticatedUserRole) >= 0);
            return this;
        },

        refreshMenuStatus: function(){
            this.activeView = this.$root.$refs.app.getActiveView();
        },

        showView: function(event){
            const viewID = event.target.closest('li').getAttribute('data-vid');
            if ( viewID !== null && viewID !== '' ){
                this.$root.$refs.app.setActiveView(viewID);
            }
            this.closeMenu();
        },

        logout: async function(){
            this.closeMenu();
            const response = await Request.get('/api/user/logout');
            if ( !this.$root.$refs.app.handleResponseError(response) ){
                this.$parent.reset();
            }
        },

        openMenu: function(){
            this.$refs.menu.setAttribute('data-visible', 'true');
            this.$refs.overlay.setAttribute('data-visible', 'true');
            return this;
        },

        closeMenu: function(){
            this.$refs.menu.setAttribute('data-visible', 'false');
            this.$refs.overlay.setAttribute('data-visible', 'false');
            return this;
        }
    }
};
