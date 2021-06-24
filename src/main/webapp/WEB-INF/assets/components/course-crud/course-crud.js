'use strict';

import Request from '../../utils/Request';
import User from '../../models/User';

export default {
    data: function(){
        return {
            courses: []
        };
    },

    methods: {
        init: function(){
            return this.reload();
        },

        reset: function(){
            this.courses = [];
            return this;
        },

        reload: async function(){
            const response = await Request.get('/api/course/list');
            if ( !this.$parent.handleResponseError(response) ){
                this.courses = response.data;
                this.$root.$refs.app.getView('repetition-crud').setCourses(this.courses);
            }
        },

        openCreationForm: function(){
            this.$refs.creationDialog.setAttribute('data-display', 'true');
            return this;
        },

        discardCreation: function(){
            this.$refs.creationDialog.setAttribute('data-display', 'false');
            this.$refs.title.setValue('');
            this.$refs.title.setErrorMessage('');
            this.$forceUpdate();
            return this;
        },

        validate: function(){
            const title = this.$refs.title.getValue();
            const titleMessage = title === '' ? 'You must provide a valid title.' : null;
            this.$refs.title.setErrorMessage(titleMessage);
            return titleMessage === null;
        },

        handleCreation: async function(event){
            event.preventDefault();
            event.stopPropagation();
            await this.create();
        },

        showFormErrorMessages: function(response){
            if ( response.messages !== null && typeof response.messages === 'object' ){
                if ( response.messages.title !== '' && typeof response.messages.title === 'string' ){
                    this.$refs.title.setErrorMessage(response.messages.title);
                }
            }
            this.$forceUpdate();
        },

        create: async function(){
            if ( this.validate() ){
                const response = await Request.post('/api/course/create', {
                    title: this.$refs.title.getValue()
                });
                if ( response.code === 400 ){
                    this.showFormErrorMessages(response);
                }else{
                    this.$root.$refs.app.getView('repetition-crud').setCourses(this.courses);
                    this.courses.push(response.data);
                    await new Promise((resolve) => {
                        this.$nextTick(() => {
                            this.discardCreation();
                            resolve();
                        });
                    });
                }
            }
        },

        remove: function(event){
            return new Promise((resolve, reject) => {
                this.$root.$refs.app.getAlertModal().show('Do you really want to delete this course?', 'confirm', null, () => {
                    const courseID = parseInt(event.target.closest('li[data-cid]').getAttribute('data-cid'));
                    Request.get('/api/course/delete?id=' + courseID).then((response) => {
                        if ( !this.$root.$refs.app.handleResponseError(response) ){
                            for ( let i = 0 ; i < this.courses.length ; i++ ){
                                if ( this.courses[i].id === courseID ){
                                    this.courses.splice(i, 1);
                                    break;
                                }
                            }
                            this.$root.$refs.app.getView('repetition-crud').setCourses(this.courses);
                            return this.refreshInvolvedViews().then(() => resolve()).catch((ex) => reject(ex));
                        }
                        resolve();
                    }).catch((ex) => reject(ex));
                });
            });
        },

        refreshInvolvedViews: async function(){
            const authenticatedUserRole = this.$root.authenticatedUser?.getRole();
            const viewName = authenticatedUserRole === User.ROLE_ADMIN ? 'lesson-list-all' : 'lesson-list';
            await Promise.all([
                this.$root.$refs.app.getView('available-lesson-list').reload(),
                this.$root.$refs.app.getView(viewName).reload()
            ]);
        }
    }
};
