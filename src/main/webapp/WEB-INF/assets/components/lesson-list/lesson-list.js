'use strict';

import Utils from '../../utils/Utils';

export default {
    props: ['mode'],
    data: function(){
        return {
            activeTab: 'active-lessons',
            lessons: {
                active: [],
                completed: [],
                cancelled: []
            }
        };
    },
    methods: {
        init: function(){
            const url = this.mode === 'all' ? '/app/api/lesson/list-all' : '/app/api/lesson/list';
            const request = new XMLHttpRequest();
            request.open('GET', url, true);
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( !this.$root.$refs.app.handleResponseError(request.response) ){
                        this.reset();
                        request.response.data.forEach((lesson) => {
                            if ( lesson.hasOwnProperty('deletedAt') ){
                                this.lessons.cancelled.push(lesson);
                            }else if ( lesson.completed === true ){
                                this.lessons.completed.push(lesson);
                            }else{
                                this.lessons.active.push(lesson);
                            }
                        });
                        this.applyOrdering();
                    }
                }
            };
            request.send();
        },
        reset: function(){
            this.lessons.active = [];
            this.lessons.completed = [];
            this.lessons.cancelled = [];
            return this;
        },
        getDayName: function(day){
            return Utils.getDayName(day);
        },
        pickTab: function(event){
            this.activeTab = event.target.getAttribute('data-ref');
        },
        mark: function(event){
            this.$root.$refs.app.getAlertModal().show('Do you really want to mark this lesson as completed?', 'confirm', null, () => {
                const lessonID = parseInt(event.target.closest('li[data-lid]').getAttribute('data-lid'));
                const request = new XMLHttpRequest();
                request.open('GET', '/app/api/lesson/mark?id=' + lessonID + '&completed=true', true);
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( !this.$root.$refs.app.handleResponseError(request.response) ){
                            this.moveLessonToList(lessonID, 'completed');
                        }
                    }
                };
                request.send();
            });
        },
        remove: function(event){
            this.$root.$refs.app.getAlertModal().show('Do you really want to cancel this lesson?', 'confirm', null, () => {
                const lessonID = parseInt(event.target.closest('li[data-lid]').getAttribute('data-lid'));
                const request = new XMLHttpRequest();
                request.open('GET', '/app/api/lesson/delete?id=' + lessonID, true);
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( !this.$root.$refs.app.handleResponseError(request.response) ){
                            this.moveLessonToList(lessonID, 'cancelled');
                        }
                    }
                };
                request.send();
            });
        },
        applyOrdering: function(){
            for ( const currentListName in this.lessons ){
                if ( this.lessons.hasOwnProperty(currentListName) ){
                    this.lessons[currentListName].sort((a, b) => {
                        const dateA = new Date(a.createdAt);
                        const dateB = new Date(b.createdAt);
                        return dateA < dateB ? 1 : ( dateA === dateB ? 0 : -1 );
                    });
                }
            }
            return this;
        },
        moveLessonToList: function(lessonID, listName){
            for ( const currentListName in this.lessons ){
                if ( this.lessons.hasOwnProperty(currentListName) ){
                    let found = false;
                    for ( let i = 0 ; i < this.lessons[currentListName].length ; i++ ){
                        if ( this.lessons[currentListName][i].id === lessonID ){
                            this.lessons[listName].push(this.lessons[currentListName][i]);
                            this.lessons[currentListName] = this.lessons[currentListName].filter((element) => {
                                return element.id !== lessonID;
                            });
                            this.applyOrdering();
                            found = true;
                            break;
                        }
                    }
                    if ( found ){
                        break;
                    }
                }
            }
            return this;
        },
        addLesson: function(lesson, listName){
            if ( this.lessons.hasOwnProperty(listName) ){
                this.lessons[listName].push(lesson);
                this.applyOrdering();
            }
            return this;
        }
    }
};
