'use strict';

import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import Vue from 'vue';
import './app.scss';
import app from './components/app/app.vue';
import headerBar from './components/header-bar/header-bar.vue';
import lateralMenu from './components/lateral-menu/lateral-menu.vue';
import inputField from './components/input-field/input-field.vue';
import loginForm from './components/login-form/login-form.vue';
import availableLessonList from './components/available-lesson-list/available-lesson-list.vue';
import lessonList from './components/lesson-list/lesson-list.vue';
import lessonListAll from './components/lesson-list-all/lesson-list-all.vue';
import alertModal from './components/alert-modal/alert-modal.vue';
import courseCrud from './components/course-crud/course-crud.vue';
import teacherCrud from './components/teacher-crud/teacher-crud.vue';
import repetitionCrud from './components/repetition-crud/repetition-crud.vue';

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
                    component: availableLessonList
                }, {
                    id: 'lesson-list',
                    label: 'My lessons',
                    component: lessonList
                }, {
                    id: 'lesson-list-all',
                    label: 'All lessons',
                    component: lessonListAll
                }, {
                    id: 'course-crud',
                    label: 'Courses management',
                    component: courseCrud
                }, {
                    id: 'teacher-crud',
                    label: 'Teachers management',
                    component: teacherCrud
                }, {
                    id: 'repetition-crud',
                    label: 'Repetitions management',
                    component: repetitionCrud
                }]
            };
        },
        el: '#app'
    });
});
