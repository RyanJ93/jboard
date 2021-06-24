'use strict';

import Request from '../../utils/Request';
import User from "../../models/User";

export default {
    data: function(){
        return {
            teachers: []
        };
    },

    methods: {
        init: function(){
            return this.refresh();
        },

        reset: function(){
            this.teachers = [];
            return this;
        },

        refresh: async function(){
            const response = await Request.get('/api/teacher/list');
            if ( !this.$parent.handleResponseError(response) ){
                this.teachers = response.data;
                this.$root.$refs.app.getView('repetition-crud').setTeachers(this.teachers);
            }
        },

        openCreationForm: function(){
            this.$refs.creationDialog.setAttribute('data-display', 'true');
            return this;
        },

        discardCreation: function(){
            this.$refs.creationDialog.setAttribute('data-display', 'false');
            this.$refs.name.setValue('');
            this.$refs.surname.setValue('');
            this.$refs.name.setErrorMessage('');
            this.$refs.surname.setErrorMessage('');
            this.$forceUpdate();
            return this;
        },

        validate: function(){
            const name = this.$refs.name.getValue(), surname = this.$refs.surname.getValue();
            const nameMessage = name === '' ? 'You must provide a valid name.' : null;
            const surnameMessage = surname === '' ? 'You must provide a valid surname.' : null;
            this.$refs.name.setErrorMessage(nameMessage);
            this.$refs.surname.setErrorMessage(surnameMessage);
            return nameMessage === null && surnameMessage === null;
        },

        handleCreation: async function(event){
            event.preventDefault();
            event.stopPropagation();
            await this.create();
        },

        showFormErrorMessages: function(response){
            if ( response.messages !== null && typeof response.messages === 'object' ){
                if ( response.messages.name !== '' && typeof response.messages.name === 'string' ){
                    this.$refs.name.setErrorMessage(response.messages.name);
                }
                if ( response.messages.surname !== '' && typeof response.messages.surname === 'string' ){
                    this.$refs.surname.setErrorMessage(response.messages.surname);
                }
            }
            this.$forceUpdate();
        },

        create: async function(){
            if ( this.validate() ){
                const response = await Request.post('/api/teacher/create', {
                    name: this.$refs.name.getValue(),
                    surname: this.$refs.surname.getValue()
                });
                if ( response.code === 400 ){
                    this.showFormErrorMessages(response);
                }else{
                    this.teachers.push(response.data);
                    this.$root.$refs.app.getView('repetition-crud').setTeachers(this.teachers);
                    this.discardCreation();
                }
            }
        },

        remove: function(event){
            return new Promise((resolve, reject) => {
                this.$parent.getAlertModal().show('Do you really want to delete this teacher?', 'confirm', null, () => {
                    const teacherID = parseInt(event.target.closest('li[data-tid]').getAttribute('data-tid'));
                    Request.get('/api/teacher/delete?id=' + teacherID).then((response) => {
                        if ( !this.$parent.handleResponseError(response) ){
                            for ( let i = 0 ; i < this.teachers.length ; i++ ){
                                if ( this.teachers[i].id === teacherID ){
                                    this.teachers.splice(i, 1);
                                    break;
                                }
                            }
                            this.$root.$refs.app.getView('repetition-crud').setTeachers(this.teachers);
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
