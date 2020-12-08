'use strict';

export default {
    data: function(){
        return {
            teachers: []
        };
    },
    methods: {
        init: function(){
            const request = new XMLHttpRequest();
            request.open('GET', '/app/api/teacher/list', true);
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( !this.$parent.handleResponseError(request.response) ){
                        this.teachers = request.response.data;
                        this.$root.$refs.app.getView('repetition-crud').setTeachers(this.teachers);
                    }
                }
            };
            request.send();
        },
        reset: function(){
            this.teachers = [];
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
            this.$refs.name.setValue('');
            this.$refs.surname.setValue('');
            return this;
        },
        validate: function(){
            const name = this.$refs.name.getValue(), surname = this.$refs.surname.getValue();
            const nameMessage = name === '' ? 'Invalid name.' : null;
            const surnameMessage = surname === '' ? 'Invalid surname.' : null;
            this.$refs.name.setErrorMessage(nameMessage);
            this.$refs.surname.setErrorMessage(surnameMessage);
            return nameMessage === null && surnameMessage === null;
        },
        handleCreation: function(event){
            event.preventDefault();
            event.stopPropagation();
            this.create();
        },
        create: function(){
            if ( this.validate() ){
                const request = new XMLHttpRequest();
                request.open('POST', '/app/api/teacher/create', true);
                request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( request.response === null || request.response.code >= 500 ){
                            this.$refs.title.setErrorMessage('An error occurred, please retry later.');
                        }else{
                            this.teachers.push(request.response.data);
                            this.$root.$refs.app.getView('repetition-crud').setTeachers(this.teachers);
                            this.discardCreation();
                        }
                    }
                };
                const name = this.$refs.name.getValue(), surname = this.$refs.surname.getValue();
                request.send('name=' + encodeURIComponent(name) + '&surname=' + encodeURIComponent(surname));
            }
        },
        remove: function(event){
            this.$parent.getAlertModal().show('Do you really want to delete this teacher?', 'confirm', null, () => {
                const teacherID = parseInt(event.target.closest('li[data-tid]').getAttribute('data-tid'));
                const request = new XMLHttpRequest();
                request.open('GET', '/app/api/teacher/delete?id=' + teacherID, true);
                request.responseType = 'json';
                request.onreadystatechange = () => {
                    if ( request.readyState === XMLHttpRequest.DONE ){
                        if ( !this.$parent.handleResponseError(request.response) ){
                            for ( let i = 0 ; i < this.teachers.length ; i++ ){
                                if ( this.teachers[i].id === teacherID ){
                                    this.teachers.splice(i, 1);
                                    break;
                                }
                            }
                            this.$root.$refs.app.getView('repetition-crud').setTeachers(this.teachers);
                        }
                    }
                };
                request.send();
            });
        }
    }
};
