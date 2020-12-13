'use strict';

export default {
    data: function(){
        return {
            courses: []
        };
    },
    methods: {
        init: function(){
            const request = new XMLHttpRequest();
            request.open('GET', '/app/api/course/list', true);
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( !this.$parent.handleResponseError(request.response) ){
                        this.courses = request.response.data;
                        this.$root.$refs.app.getView('repetition-crud').setCourses(this.courses);
                    }
                }
            };
            request.send();
        },
        reset: function(){
            this.courses = [];
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
        validate: function(){
            const title = this.$refs.title.getValue();
            const titleMessage = title === '' ? 'You must provide a valid title.' : null;
            this.$refs.title.setErrorMessage(titleMessage);
            return titleMessage === null;
        },
        handleCreation: function(event){
            event.preventDefault();
            event.stopPropagation();
            this.create();
        },
        showFormErrorMessages: function(response){
            if ( response.messages !== null && typeof response.messages === 'object' ){
                if ( response.messages.title !== '' && typeof response.messages.title === 'string' ){
                    this.$refs.title.setErrorMessage(response.messages.title);
                }
            }
        },
        create: function(){
            if ( this.validate() ){
                const request = new XMLHttpRequest();
                request.open('POST', '/app/api/course/create', true);
                request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( request.response === null || request.response.code >= 500 ){
                            this.$refs.title.setErrorMessage('An error occurred, please retry later.');
                        }else if ( request.response.code === 400 ){
                            this.showFormErrorMessages(request.response);
                        }else{
                            this.courses.push(request.response.data);
                            this.$root.$refs.app.getView('repetition-crud').setCourses(this.courses);
                            this.discardCreation();
                        }
                    }
                };
                const title = this.$refs.title.getValue();
                request.send('title=' + encodeURIComponent(title));
            }
        },
        remove: function(event){
            this.$parent.getAlertModal().show('Do you really want to delete this course?', 'confirm', null, () => {
                const courseID = parseInt(event.target.closest('li[data-cid]').getAttribute('data-cid'));
                const request = new XMLHttpRequest();
                request.open('GET', '/app/api/course/delete?id=' + courseID, true);
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( !this.$parent.handleResponseError(request.response) ){
                            for ( let i = 0 ; i < this.courses.length ; i++ ){
                                if ( this.courses[i].id === courseID ){
                                    this.courses.splice(i, 1);
                                    break;
                                }
                            }
                            this.$root.$refs.app.getView('repetition-crud').setCourses(this.courses);
                        }
                    }
                };
                request.send();
            });
        }
    }
};
