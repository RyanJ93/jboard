'use strict';

export default {
    data: function(){
        return {
            views: this.$root.views
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
                this.$parent.setActiveView(viewID);
            }
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
        }
    }
};
