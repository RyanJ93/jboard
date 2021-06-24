'use strict';

import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/js/fontawesome';
import '@fortawesome/fontawesome-free/js/solid';
import '@fortawesome/fontawesome-free/js/regular';
import '@fortawesome/fontawesome-free/js/brands';
import Vue from 'vue';

import './app.scss';
import User from './models/User';
import app from './components/app/app.vue';
import headerBar from './components/header-bar/header-bar.vue';
import loginForm from './components/login-form/login-form.vue';
import inputField from './components/input-field/input-field.vue';
import lessonList from './components/lesson-list/lesson-list.vue';
import alertModal from './components/alert-modal/alert-modal.vue';
import courseCrud from './components/course-crud/course-crud.vue';
import teacherCrud from './components/teacher-crud/teacher-crud.vue';
import lateralMenu from './components/lateral-menu/lateral-menu.vue';
import lessonListAll from './components/lesson-list-all/lesson-list-all.vue';
import repetitionCrud from './components/repetition-crud/repetition-crud.vue';
import availableLessonList from './components/available-lesson-list/available-lesson-list.vue';

function registerComponents(){
    Vue.component('app', app);
    Vue.component('header-bar', headerBar);
    Vue.component('lateral-menu', lateralMenu);
    Vue.component('input-field', inputField);
    Vue.component('login-form', loginForm);
    Vue.component('available-lesson-list', availableLessonList);
    Vue.component('lesson-list', lessonList);
    Vue.component('lesson-list-all', lessonListAll);
    Vue.component('alert-modal', alertModal);
    Vue.component('course-crud', courseCrud);
    Vue.component('teacher-crud', teacherCrud);
    Vue.component('repetition-crud', repetitionCrud);
}

document.addEventListener('DOMContentLoaded', function(){
    registerComponents();
    const app = new Vue({
        data: function(){
            return {
                views: [{
                    id: 'available-lesson-list',
                    label: 'Available lessons',
                    icon: 'calendar-alt',
                    component: availableLessonList,
                    targetRoles: [User.ROLE_USER, User.ROLE_ADMIN]
                }, {
                    id: 'lesson-list',
                    label: 'My lessons',
                    icon: 'chalkboard-teacher',
                    component: lessonList,
                    targetRoles: [User.ROLE_USER]
                }, {
                    id: 'lesson-list-all',
                    label: 'All lessons',
                    icon: 'chalkboard-teacher',
                    component: lessonListAll,
                    targetRoles: [User.ROLE_ADMIN]
                }, {
                    id: 'course-crud',
                    label: 'Courses management',
                    icon: 'book',
                    component: courseCrud,
                    targetRoles: [User.ROLE_ADMIN]
                }, {
                    id: 'teacher-crud',
                    label: 'Teachers management',
                    icon: 'user-graduate',
                    component: teacherCrud,
                    targetRoles: [User.ROLE_ADMIN]
                }, {
                    id: 'repetition-crud',
                    label: 'Repetitions management',
                    icon: 'edit',
                    component: repetitionCrud,
                    targetRoles: [User.ROLE_ADMIN]
                }],
                authenticatedUser: null
            };
        },
        el: '#app'
    });
});
