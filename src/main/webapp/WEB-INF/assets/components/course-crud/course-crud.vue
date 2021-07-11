<template>
    <div>
        <div>
            <h3 class="title d-inline-block">Courses management</h3>
            <div :class="$style.controls">
                <a class="button" v-on:click="openCreationForm" title="Add a new course.">
                    <i class="fas fa-plus"></i><span class="d-none d-sm-inline-block">&nbsp;Add</span>
                </a>
            </div>
        </div>
        <ul :class="$style.list" v-if="courses.length > 0">
            <li v-for="course in courses" :data-cid="course.id">
                <div :class="$style.info">
                    <p>{{ course.title }}</p>
                </div>
                <div :class="$style.controls">
                    <a class="button" v-on:click="remove" title="Delete this course.">
                        <i class="fas fa-trash"></i><span class="d-none d-sm-inline-block">&nbsp;Delete</span>
                    </a>
                </div>
            </li>
        </ul>
        <p :class="$style.emptyMessage" v-else>No course found</p>
        <div :class="$style.creationDialog" data-display="false" ref="creationDialog">
            <form :class="$style.form" ref="form" v-on:submit="handleCreation">
                <h5 class="mb-4">Add a new course</h5>
                <input-field class="mb-2" placeholder="Title" type="text" ref="title"></input-field>
                <div :class="$style.controls">
                    <input type="submit" value="Create" style="float:right;" />
                    <input type="reset" value="Cancel" v-on:click="discardCreation" />
                </div>
            </form>
            <div :class="$style.overlay" ref="overlay"></div>
        </div>
    </div>
</template>
<script src="./course-crud.js"></script>
<style lang="sass" src="./course-crud.scss" module></style>
