'use strict';

import Request from '../../utils/Request';
import User from "../../models/User";

export default {
    data: function(){
        return {
            repetitions: [],
            teachers: [],
            courses: [],
            formErrorMessages: {}
        };
    },

    methods: {
        init: function(){
            return this.refresh();
        },

        reset: function(){
            this.repetitions = [];
            return this;
        },

        refresh: async function(){
            const response = await Request.get('/api/repetition/list');
            if ( !this.$parent.handleResponseError(response) ){
                this.repetitions = response.data;
            }
        },

        openCreationForm: function(){
            this.$refs.creationDialog.setAttribute('data-display', 'true');
            return this;
        },

        discardCreation: function(){
            this.$refs.creationDialog.setAttribute('data-display', 'false');
            this.$refs.teacherID.selectedIndex = 0;
            this.$refs.courseID.selectedIndex = 0;
            this.formErrorMessages = {};
            this.$forceUpdate();
            return this;
        },

        validate: function(){
            const teacherID = parseInt(this.$refs.teacherID.value);
            const courseID = parseInt(this.$refs.courseID.value);
            this.formErrorMessages = {};
            let isValid = true;
            if ( teacherID === 0 || isNaN(teacherID) ){
                this.formErrorMessages.teacherID = 'You must select a teacher.';
                isValid = false;
            }
            if ( courseID === 0 || isNaN(courseID) ){
                this.formErrorMessages.courseID = 'You must select a course.';
                isValid = false;
            }
            return isValid;
        },

        handleCreation: async function(event){
            event.preventDefault();
            event.stopPropagation();
            await this.create();
        },

        setCourses: function(courses){
            this.courses = courses;
            return this;
        },

        setTeachers: function(teachers){
            this.teachers = teachers;
            return this;
        },

        showFormErrorMessages: function(response){
            this.formErrorMessages = {};
            if ( response.messages !== null && typeof response.messages === 'object' ){
                if ( response.messages.courseID !== '' && typeof response.messages.courseID === 'string' ){
                    this.formErrorMessages.courseID = response.messages.courseID;
                }
                if ( response.messages.teacherID !== '' && typeof response.messages.teacherID === 'string' ){
                    this.formErrorMessages.teacherID = response.messages.teacherID;
                }
            }else if ( typeof response.message === 'string' && response.message !== '' ){
                this.formErrorMessages.teacherID = response.message;
            }
            this.$forceUpdate();
        },

        create: async function(){
            if ( this.validate() ){
                const response = await Request.post('/api/repetition/create', {
                    teacherID: this.$refs.teacherID.value,
                    courseID: this.$refs.courseID.value
                });
                if ( !this.$root.$refs.app.handleResponseError(response, [400]) ){
                    if ( response.code === 400 ){
                        this.showFormErrorMessages(response);
                    }else{
                        this.repetitions.push(response.data);
                        await this.$root.$refs.app.getView('available-lesson-list').reload();
                        this.discardCreation();
                    }
                }
            }
        },

        remove: function(event){
            return new Promise((resolve, reject) => {
                this.$parent.getAlertModal().show('Do you really want to delete this repetition?', 'confirm', null, () => {
                    const repetitionID = parseInt(event.target.closest('li[data-rid]').getAttribute('data-rid'));
                    Request.get('/api/repetition/delete?id=' + repetitionID).then((response) => {
                        if ( !this.$parent.handleResponseError(response) ){
                            for ( let i = 0 ; i < this.repetitions.length ; i++ ){
                                if ( this.repetitions[i].id === repetitionID ){
                                    this.repetitions.splice(i, 1);
                                    break;
                                }
                            }
                            return this.refreshInvolvedViews().then(() => resolve()).catch((ex) => reject(ex));
                        }
                        resolve();
                    }).catch((ex) => reject(ex));
                });
            });
        },

        removeRepetitionsFromListByCourse: function(courseID){
            this.repetitions = this.repetitions.filter((repetition) => repetition.course.id !== courseID);
            return this;
        },

        removeRepetitionsFromListByTeacher: function(teacherID){
            this.repetitions = this.repetitions.filter((repetition) => repetition.teacher.id !== teacherID);
            return this;
        },

        refreshInvolvedViews: async function(){
            const viewName = this.$root.authenticatedUser?.isAdmin() === true ? 'lesson-list-all' : 'lesson-list';
            await Promise.all([
                this.$root.$refs.app.getView('available-lesson-list').reload(),
                this.$root.$refs.app.getView(viewName).reload()
            ]);
        }
    }
};
