'use strict';

import 'bootstrap';
import Vue from 'vue';
import './app.scss';
import header from './components/header-bar/header-bar.vue';

document.addEventListener('DOMContentLoaded', function(){
    Vue.component('header-bar', header);
    const app = new Vue({
        el: '#app'
    });
});
