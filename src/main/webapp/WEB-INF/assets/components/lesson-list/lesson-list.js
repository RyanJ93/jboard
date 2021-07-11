'use strict';

import Request from '../../utils/Request';
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
            },
            isUserAdmin: false
        };
    },

    methods: {
        init: function(){
            this.isUserAdmin = this.$root.authenticatedUser?.isAdmin() === true;
            return this.reload();
        },

        reset: function(){
            this.lessons.active = [];
            this.lessons.completed = [];
            this.lessons.cancelled = [];
            return this;
        },

        reload: async function(){
            const response = await Request.get(this.mode === 'all' ? '/api/lesson/list-all' : '/api/lesson/list');
            if ( !this.$root.$refs.app.handleResponseError(response) ){
                this.reset();
                response.data.forEach((lesson) => {
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
        },

        getDayName: function(day){
            return Utils.getDayName(day);
        },

        pickTab: function(event){
            this.activeTab = event.target.getAttribute('data-ref');
        },

        mark: function(event){
            return new Promise((resolve, reject) => {
                this.$root.$refs.app.getAlertModal().show('Do you really want to mark this lesson as completed?', 'confirm', null, () => {
                    const lessonID = parseInt(event.target.closest('li[data-lid]').getAttribute('data-lid'));
                    Request.get('/api/lesson/mark?id=' + lessonID + '&completed=true').then((response) => {
                        if ( !this.$root.$refs.app.handleResponseError(response) ){
                            this.moveLessonToList(lessonID, 'completed');
                        }
                        this.$root.$refs.app.getView('available-lesson-list').reload().then(() => resolve()).catch((ex) => reject(ex));
                    }).catch((ex) => reject(ex));
                });
            });
        },

        remove: function(event){
            return new Promise((resolve) => {
                this.$root.$refs.app.getAlertModal().show('Do you really want to cancel this lesson?', 'confirm', null, () => {
                    const lessonID = parseInt(event.target.closest('li[data-lid]').getAttribute('data-lid'));
                    Request.get('/api/lesson/delete?id=' + lessonID).then((response) => {
                        if ( !this.$root.$refs.app.handleResponseError(response) ){
                            this.moveLessonToList(lessonID, 'cancelled');
                        }
                        this.$root.$refs.app.getView('available-lesson-list').reload().then(() => resolve()).catch((ex) => reject(ex));
                    }).catch((ex) => reject(ex));
                });
            });
        },

        applyOrdering: function(){
            for ( const currentListName in this.lessons ){
                if ( this.lessons.hasOwnProperty(currentListName) ){
                    this.lessons[currentListName].sort((a, b) => {
                        const dateA = new Date(a.updatedAt), dateB = new Date(b.updatedAt);
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
