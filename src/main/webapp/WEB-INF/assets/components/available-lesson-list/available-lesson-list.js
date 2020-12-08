'use strict';

import Utils from '../../utils/Utils';

export default {
    data: function(){
        return {
            data: [],
        };
    },
    methods: {
        init: function(){
            const request = new XMLHttpRequest();
            request.open('GET', '/app/api/course/available', true);
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( !this.$parent.handleResponseError(request.response) ){
                        this.data = request.response.data;
                    }
                }
            };
            request.send();
        },
        reset: function(){
            this.data = [];
            return this;
        },
        getDayName: function(day){
            return Utils.getDayName(day);
        },
        bookLesson: function(event){
            const element = event.target.closest('li');
            if ( element.getAttribute('data-available') === '1' ){
                const hour = element.getAttribute('data-hour');
                const day = element.getAttribute('data-day');
                const teacherID = element.getAttribute('data-tid');
                const courseID = element.getAttribute('data-cid');
                const request = new XMLHttpRequest();
                request.open('POST', '/app/api/lesson/create', true);
                request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( !this.$parent.handleResponseError(request.response) ){
                            element.setAttribute('data-available', '0');
                            element.setAttribute('data-mine', '1');
                            this.$parent.getView('lesson-list').addLesson(request.response.data, 'active');
                        }
                    }
                };
                request.send('hour=' + hour + '&day=' + day + '&teacherID=' + teacherID + '&courseID=' + courseID);
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
