<template>
    <div>
        <div :class="$style.controls">
            <a class="button" v-on:click="openCreationForm" title="Add">Add</a>
        </div>
        <ul :class="$style.list">
            <li v-for="repetition in repetitions" :data-rid="repetition.id">
                <div :class="$style.info">
                    <p>{{ repetition.course.title }}</p>
                    <p>{{ repetition.teacher.name + ' ' + repetition.teacher.surname }}</p>
                </div>
                <div :class="$style.controls">
                    <a class="button" v-on:click="remove" title="Delete">Delete</a>
                </div>
            </li>
        </ul>
        <form :class="$style.form" ref="form" data-display="false" v-on:submit="handleCreation">
            <h5 class="mb-4">Add a new repetition</h5>
            <select name="courseID" ref="courseID">
                <option v-for="course in courses" :value="course.id">{{ course.title }}</option>
            </select>
            <select name="teacherID" ref="teacherID" style="margin:6px 0 24px 0;">
                <option v-for="teacher in teachers" :value="teacher.id">{{ teacher.name + ' ' + teacher.surname }}</option>
            </select>
            <input type="submit" value="Create" style="float:right;" />
            <input type="reset" value="Cancel" v-on:click="discardCreation" />
        </form>
        <div :class="$style.overlay" ref="overlay" data-display="false"></div>
    </div>
</template>
<script src="./repetition-crud.js"></script>
<style lang="sass" src="./repetition-crud.scss" module></style>
