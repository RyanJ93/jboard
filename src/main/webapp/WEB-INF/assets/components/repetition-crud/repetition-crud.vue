<template>
    <div>
        <div>
            <h3 class="title d-inline-block">Repetetions management</h3>
            <div :class="$style.controls">
                <a class="button" v-on:click="openCreationForm" title="Add a new repetetion.">
                    <i class="fas fa-plus"></i><span class="d-none d-sm-inline-block">&nbsp;Add</span>
                </a>
            </div>
        </div>
        <ul :class="$style.list">
            <li v-for="repetition in repetitions" :data-rid="repetition.id">
                <div :class="$style.info">
                    <p>{{ repetition.course.title }}</p>
                    <p :class="$style.teacher">By {{ repetition.teacher.name + ' ' + repetition.teacher.surname }}</p>
                </div>
                <div :class="$style.controls">
                    <a class="button" v-on:click="remove" title="Delete this repetetion.">
                        <i class="fas fa-trash"></i><span class="d-none d-sm-inline-block">&nbsp;Delete</span>
                    </a>
                </div>
            </li>
        </ul>
        <div :class="$style.creationDialog" data-display="false" ref="creationDialog">
            <form :class="$style.form" ref="form" v-on:submit="handleCreation">
                <h5 class="mb-4">Add a new repetition</h5>
                <div>
                    <label for="repetition-crud-course-id">Course</label>
                    <select name="courseID" ref="courseID" id="repetition-crud-course-id">
                        <option v-for="course in courses" :value="course.id">{{ course.title }}</option>
                    </select>
                    <p :class="$style.error" v-if="typeof formErrorMessages.courseID === 'string'">{{ formErrorMessages.courseID }}</p>
                </div>
                <div class="mt-2">
                    <label for="repetition-crud-teacher-id">Teacher</label>
                    <select name="teacherID" ref="teacherID" id="repetition-crud-teacher-id">
                        <option v-for="teacher in teachers" :value="teacher.id">{{ teacher.name + ' ' + teacher.surname }}</option>
                    </select>
                    <p :class="$style.error" v-if="typeof formErrorMessages.teacherID === 'string'">{{ formErrorMessages.teacherID }}</p>
                </div>
                <div :class="$style.controls">
                    <input type="submit" value="Create" style="float:right;" />
                    <input type="reset" value="Cancel" v-on:click="discardCreation" />
                </div>
            </form>
            <div :class="$style.overlay" ref="overlay"></div>
        </div>
    </div>
</template>
<script src="./repetition-crud.js"></script>
<style lang="sass" src="./repetition-crud.scss" module></style>
