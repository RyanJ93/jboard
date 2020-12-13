'use strict';

export default {
    data: function(){
        return {
            views: this.$root.views,
            activeView: null
        };
    },
    methods: {
        refreshAvailableViews: function(){
            this.views = this.$root.views;
            return this;
        },
        showView: function(event){
            const viewID = event.target.getAttribute('data-vid');
            if ( viewID !== null && viewID !== '' ){
                this.$root.$refs.app.setActiveView(viewID);
                this.activeView = viewID;
            }
            this.closeMenu();
        },
        logout: function(){
            const request = new XMLHttpRequest();
            request.open('GET', '/app/api/user/logout', true);
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( request.response === null || request.response.code !== 200 ){
                        //
                    }else{
                        this.$parent.reset();
                    }
                }
            };
            request.send();
            this.closeMenu();
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
