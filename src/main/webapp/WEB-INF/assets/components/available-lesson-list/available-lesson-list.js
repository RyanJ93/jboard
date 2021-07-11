'use strict';

import Request from '../../utils/Request';
import Utils from '../../utils/Utils';

export default {
    data: function(){
        return {
            data: [],
            isUserAdmin: false
        };
    },

    methods: {
        _getSlotList: function(courseID, teacherID, day, hour, havingDifferentTeacherID = false){
            const slotList = [];
            for ( let i = 0 ; i < this.data.length ; i++ ){
                if ( this.data[i].course.id === courseID ){
                    for ( let j = 0 ; j < this.data[i].lessons.length ; j++ ){
                        let keepInspecting = this.data[i].lessons[j].teacher.id === teacherID;
                        if ( havingDifferentTeacherID === true ){
                            keepInspecting = !keepInspecting;
                        }
                        if ( keepInspecting ){
                            for ( let k = 0 ; k < this.data[i].lessons[j].slots.length ; k++ ){
                                if ( this.data[i].lessons[j].slots[k].day === day && this.data[i].lessons[j].slots[k].hour === hour ){
                                    slotList.push(this.data[i].lessons[j].slots[k]);
                                }
                            }
                        }
                    }
                }
            }
            return slotList;
        },

        _getSlotPropertiesByTargetElement(targetElement){
            return {
                teacherID: parseInt(targetElement.getAttribute('data-tid')),
                courseID: parseInt(targetElement.getAttribute('data-cid')),
                hour: parseInt(targetElement.getAttribute('data-hour')),
                day: parseInt(targetElement.getAttribute('data-day'))
            };
        },

        init: function(){
            this.isUserAdmin = this.$root.authenticatedUser?.isAdmin() === true;
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
                this.computeEligibility();
            }
        },

        getDayName: function(day){
            return Utils.getDayName(day);
        },

        computeEligibility: function(){
            for ( let i = 0 ; i < this.data.length ; i++ ){
                const index = new Set();
                for ( let j = 0 ; j < this.data[i].lessons.length ; j++ ){
                    for ( let k = 0 ; k < this.data[i].lessons[j].slots.length ; k++ ){
                        if ( !index.has(j + ':' + k) ){
                            this.data[i].lessons[j].slots[k].eligible = 1;
                            if ( this.data[i].lessons[j].slots[k].available === 0 && this.data[i].lessons[j].slots[k].isMine === 1 ){
                                for ( let n = 0 ; n < this.data[i].lessons.length ; n++ ){
                                    if ( n !== j ){
                                        this.data[i].lessons[n].slots[k].eligible = 0;
                                        index.add(n + ':' + k);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.$forceUpdate();
        },

        handleLessonClick: async function(event){
            const targetElement = event.target.closest('li');
            const isAvailable = targetElement.getAttribute('data-available') === '1';
            const isEligible = targetElement.getAttribute('data-eligible') === '1';
            const isMine = targetElement.getAttribute('data-is-mine') === '1';
            if ( isAvailable && isEligible ){
                await this.bookLesson(targetElement);
            }else if ( isMine || this.isUserAdmin ){
                await this.deleteLesson(targetElement);
            }
        },

        bookLesson: async function(targetElement){
            const slotProps = this._getSlotPropertiesByTargetElement(targetElement);
            const response = await Request.post('/api/lesson/create', slotProps);
            if ( !this.$root.$refs.app.handleResponseError(response) ){
                targetElement.setAttribute('data-lid', response.data.id);
                targetElement.setAttribute('data-available', '0');
                targetElement.setAttribute('data-is-mine', '1');
                const viewName = this.isUserAdmin ? 'lesson-list-all' : 'lesson-list';
                this.$root.$refs.app.getView(viewName).addLesson(response.data, 'active');
                const slotList = this._getSlotList(slotProps.courseID, slotProps.teacherID, slotProps.day, slotProps.hour);
                if ( slotList.length === 1 ){
                    slotList[0].available = 0;
                    slotList[0].isMine = 1;
                }
                this.computeEligibility();
            }
        },

        deleteLesson: async function(targetElement){
            const lessonID = parseInt(targetElement.getAttribute('data-lid'));
            const response = await Request.get('/api/lesson/delete?id=' + lessonID);
            if ( !this.$root.$refs.app.handleResponseError(response) ){
                targetElement.setAttribute('data-available', '1');
                targetElement.setAttribute('data-is-mine', '0');
                targetElement.removeAttribute('data-lid');
                const viewName = this.isUserAdmin ? 'lesson-list-all' : 'lesson-list';
                this.$root.$refs.app.getView(viewName).moveLessonToList(lessonID, 'cancelled');
                const slotProps = this._getSlotPropertiesByTargetElement(targetElement);
                const slotList = this._getSlotList(slotProps.courseID, slotProps.teacherID, slotProps.day, slotProps.hour);
                if ( slotList.length === 1 ){
                    slotList[0].available = 1;
                    slotList[0].isMine = 0;
                }
                this.computeEligibility();
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
