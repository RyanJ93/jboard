'use strict';

export default {
    data: function(){
        return {
            disabled: false,
            views: this.$root.views
        }
    },
    methods: {
        refreshAvailableViews: function(){
            this.views = this.$root.views;
            return this;
        },
        getLoginForm: function(){
            return this.$refs.loginForm;
        },
        getAlertModal: function(){
            return this.$refs.alertModal;
        },
        setDisabled: function(disabled){
            this.disabled = disabled === true;
            return this;
        },
        init: function(){
            this.refreshAvailableViews();
            this.$refs.lateralMenu.refreshAvailableViews();
            this.$refs.views.querySelectorAll('div[data-vid]').forEach((view) => {
                const ref = 'view-' + view.getAttribute('data-vid');
                if ( this.$refs.hasOwnProperty(ref) && typeof this.$refs[ref][0].init === 'function' ){
                    this.$refs[ref][0].init();
                }
            });
            this.setActiveView('available-lesson-list');
        },
        reset: function(){
            this.$refs.views.querySelectorAll('div[data-vid]').forEach((view) => {
                const ref = 'view-' + view.getAttribute('data-vid');
                if ( this.$refs.hasOwnProperty(ref) && typeof this.$refs[ref][0].reset === 'function' ){
                    this.$refs[ref][0].reset();
                }
            });
            this.getLoginForm().show();
        },
        setActiveView: function(activeView){
            this.$refs.views.querySelectorAll('div[data-vid]').forEach((view) => {
                const isActive = view.getAttribute('data-vid') === activeView;
                const ref = 'view-' + activeView;
                if ( this.$refs.hasOwnProperty(ref) ){
                    const callback = isActive ? 'show' : 'hide';
                    if ( typeof this.$refs[ref][0][callback] === 'function' ){
                        this.$refs[ref][0][callback]();
                    }
                }
                view.setAttribute('data-active', ( isActive  ? 'true' : 'false' ));
            });
            return this;
        },
        getView: function(viewID){
            let view = null, i = 0;
            const views = this.$refs.views.querySelectorAll('div[data-vid]');
            while ( view === null && i < views.length ){
                if ( views.item(i).getAttribute('data-vid') === viewID ){
                    const ref = 'view-' + viewID;
                    if ( this.$refs.hasOwnProperty(ref) ){
                        view = this.$refs[ref][0];
                    }
                }
                i++;
            }
            return view;
        },
        handleResponseError: function(response, ignoredCodes = []){
            let handledError = false;
            if ( response === null ){
                this.getAlertModal().show('Unexpected error occurred, please retry later.');
                handledError = true;
            }else if ( response.code === 403 ){
                this.getLoginForm().show();
                handledError = true;
            }else if ( response.status !== 'success' && ignoredCodes.indexOf(response.code) === -1 ){
                this.getAlertModal().show('Unexpected error occurred, please retry later.');
                handledError = true;
            }
            return handledError;
        }
    },
    mounted: function(){
        this.init();
    }
};
