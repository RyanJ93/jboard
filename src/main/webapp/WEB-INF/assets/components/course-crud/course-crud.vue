<template>
    <div>
        <div :class="$style.controls">
            <a class="button" v-on:click="openCreationForm" title="Add">Add</a>
        </div>
        <ul :class="$style.list">
            <li v-for="course in courses" :data-cid="course.id">
                <div :class="$style.info">
                    <p>{{ course.title }}</p>
                </div>
                <div :class="$style.controls">
                    <a class="button" v-on:click="remove" title="Delete">Delete</a>
                </div>
            </li>
        </ul>
        <form :class="$style.form" ref="form" data-display="false" v-on:submit="handleCreation">
            <h5 class="mb-4">Add a new course</h5>
            <input-field class="mb-2" placeholder="Title" type="text" ref="title"></input-field>
            <input type="submit" value="Create" style="float:right;" />
            <input type="reset" value="Cancel" v-on:click="discardCreation" />
        </form>
        <div :class="$style.overlay" ref="overlay" data-display="false"></div>
    </div>
</template>
<script src="./course-crud.js"></script>
<style lang="sass" src="./course-crud.scss" module></style>