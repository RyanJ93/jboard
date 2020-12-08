'use strict';

export default {
    data: function(){
        return {
            repetitions: [],
            teachers: [],
            courses: []
        };
    },
    methods: {
        init: function(){
            const request = new XMLHttpRequest();
            request.open('GET', '/app/api/repetition/list', true);
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( !this.$parent.handleResponseError(request.response) ){
                        this.repetitions = request.response.data;
                    }
                }
            };
            request.send();
        },
        reset: function(){
            this.repetitions = [];
            return this;
        },
        openCreationForm: function(){
            this.$refs.form.setAttribute('data-display', 'true');
            this.$refs.overlay.setAttribute('data-display', 'true');
            return this;
        },
        discardCreation: function(){
            this.$refs.form.setAttribute('data-display', 'false');
            this.$refs.overlay.setAttribute('data-display', 'false');
            this.$refs.title.setValue('');
            return this;
        },
        handleCreation: function(event){
            event.preventDefault();
            event.stopPropagation();
            this.create();
        },
        setCourses: function(courses){
            this.courses = courses;
            return this;
        },
        setTeachers: function(teachers){
            this.teachers = teachers;
            return this;
        },
        create: function(){
            const request = new XMLHttpRequest();
            request.open('POST', '/app/api/repetition/create', true);
            request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( request.response === null || request.response.code >= 500 ){
                        this.$refs.title.setErrorMessage('An error occurred, please retry later.');
                    }else{
                        this.repetitions.push(request.response.data);
                        this.discardCreation();
                    }
                }
            };
            const courseID = this.$refs.courseID.value, teacherID = this.$refs.teacherID.value;
            request.send('courseID=' + encodeURIComponent(courseID) + '&teacherID=' + encodeURIComponent(teacherID));
        },
        remove: function(event){
            this.$parent.getAlertModal().show('Do you really want to delete this repetition?', 'confirm', null, () => {
                const repetitionID = parseInt(event.target.closest('li[data-rid]').getAttribute('data-rid'));
                const request = new XMLHttpRequest();
                request.open('GET', '/app/api/repetition/delete?id=' + repetitionID, true);
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( !this.$parent.handleResponseError(request.response) ){
                            for ( let i = 0 ; i < this.repetitions.length ; i++ ){
                                if ( this.repetitions[i].id === repetitionID ){
                                    this.repetitions.splice(i, 1);
                                    break;
                                }
                            }
                        }
                    }
                };
                request.send();
            });
        }
    }
};
