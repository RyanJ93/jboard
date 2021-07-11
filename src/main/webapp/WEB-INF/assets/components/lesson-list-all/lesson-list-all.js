'use strict';

export default {
    methods: {
        init: function(){
            return this.$refs.list.init();
        },

        reset: function(){
            this.$refs.list.reset();
            return this;
        },

        reload: function(){
            return this.$refs.list.reload();
        },

        moveLessonToList: function(lessonID, listName){
            this.$refs.list.moveLessonToList(lessonID, listName);
            return this;
        },

        addLesson: function(lesson, listName){
            this.$refs.list.addLesson(lesson, listName);
            return this;
        }
    }
};
