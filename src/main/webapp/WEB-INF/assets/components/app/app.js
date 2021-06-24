'use strict';

import Request from '../../utils/Request';
import User from '../../models/User';

export default {
    data: function(){
        return {
            disabled: false,
            views: this.$root.views,
            activeView: null
        }
    },

    methods: {
        getAuthenticatedUser: async function(){
            let response = await Request.get('/api/user/get'), user = null;
            if ( response?.code === 200 ){
                user = new User(response.data.user);
            }else{
                this.handleResponseError(response);
            }
            return this.$root.authenticatedUser = user;
        },

        refreshAvailableViews: function(){
            const authenticatedUserRole = this.$root.authenticatedUser?.getRole();
            this.views = this.$root.views.filter((view) => view.targetRoles.indexOf(authenticatedUserRole) >= 0);
            return this;
        },

        getLoginForm: function(){
            return this.$refs.loginForm;
        },

        getAlertModal: function(){
            return this.$refs.alertModal;
        },

        getLateralMenu: function(){
            return this.$refs.lateralMenu;
        },

        setDisabled: function(disabled){
            this.disabled = disabled === true;
            return this;
        },

        init: async function(){
            this.setDisabled(true);
            const authenticatedUser = await this.getAuthenticatedUser();
            if ( authenticatedUser !== null ){
                this.refreshAvailableViews().setDisabled(false);
                this.$refs.lateralMenu.refreshAvailableViews();
                await new Promise((resolve, reject) => {
                    this.$nextTick(() => {
                        const processes = [];
                        this.$refs.views.querySelectorAll('div[data-vid]').forEach((view) => {
                            const ref = 'view-' + view.getAttribute('data-vid');
                            if ( this.$refs.hasOwnProperty(ref) && typeof this.$refs[ref][0].init === 'function' ){
                                processes.push(this.$refs[ref][0].init());
                            }
                        });
                        Promise.all(processes).then(() => {
                            this.setActiveView('available-lesson-list');
                            resolve();
                        }).catch((ex) => reject(ex));
                    });
                });
            }
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
            this.activeView = activeView;
            this.$refs.lateralMenu.refreshMenuStatus();
            return this;
        },

        getActiveView: function(){
            return this.activeView;
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

    mounted: async function(){
        await this.init();
    }
};
