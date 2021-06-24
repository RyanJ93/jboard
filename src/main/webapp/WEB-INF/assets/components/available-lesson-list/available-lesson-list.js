'use strict';

import Request from '../../utils/Request';
import Utils from '../../utils/Utils';
import User from '../../models/User';

export default {
    data: function(){
        return {
            data: [],
            isUserAdmin: false
        };
    },

    methods: {
        init: function(){
            this.isUserAdmin = this.$root.authenticatedUser?.getRole() === User.ROLE_ADMIN;
            return this.reload();
        },

        reset: function(){
            this.data = [];
            return this;
        },

        reload: async function(){
            const response = await Request.get('/api/course/available');
            if ( !this.$root.$refs.app.handleResponseError(response) ){
                this.data = response.data;
            }
        },

        getDayName: function(day){
            return Utils.getDayName(day);
        },

        handleLessonClick: async function(event){
            const targetElement = event.target.closest('li');
            if ( targetElement.getAttribute('data-available') === '1' ){
                if ( !this.isUserAdmin ){
                    await this.bookLesson(targetElement);
                }
            }else if ( targetElement.getAttribute('data-is-mine') === '1' || this.isUserAdmin ){
                await this.deleteLesson(targetElement);
            }
        },

        bookLesson: async function(targetElement){
            const response = await Request.post('/api/lesson/create', {
                hour: targetElement.getAttribute('data-hour'),
                day: targetElement.getAttribute('data-day'),
                teacherID: targetElement.getAttribute('data-tid'),
                courseID: targetElement.getAttribute('data-cid')
            });
            if ( !this.$root.$refs.app.handleResponseError(response) ){
                targetElement.setAttribute('data-available', '0');
                targetElement.setAttribute('data-is-mine', '1');
                targetElement.setAttribute('data-lid', response.data.id);
                this.$root.$refs.app.getView('lesson-list').addLesson(response.data, 'active');
            }
        },

        deleteLesson: async function(targetElement){
            const lessonID = targetElement.getAttribute('data-lid');
            const response = await Request.get('/api/lesson/delete?id=' + lessonID);
            if ( !this.$root.$refs.app.handleResponseError(response) ){
                targetElement.setAttribute('data-available', '1');
                targetElement.setAttribute('data-is-mine', '0');
                targetElement.removeAttribute('data-lid');
                this.$root.$refs.app.getView('lesson-list').moveLessonToList(lessonID, 'cancelled');
            }
        },

        toggleDetails: function(event){
            const element = event.target.closest('li').querySelector('div[data-open]');
            if ( element !== null ){
                const value = element.getAttribute('data-open') === 'true' ? 'false' : 'true';
                element.setAttribute('data-open', value);
            }
        }
    }
};
