<template>
    <div>
        <div :class="$style.controls">
            <a class="button" v-on:click="openCreationForm" title="Add">Add</a>
        </div>
        <ul :class="$style.list">
            <li v-for="teacher in teachers" :data-tid="teacher.id">
                <div :class="$style.info">
                    <p>{{ teacher.name + ' ' + teacher.surname }}</p>
                </div>
                <div :class="$style.controls">
                    <a class="button" v-on:click="remove" title="Delete">Delete</a>
                </div>
            </li>
        </ul>
        <form :class="$style.form" ref="form" data-display="false" v-on:submit="handleCreation">
            <h5 class="mb-4">Add a new teacher</h5>
            <input-field class="mb-2" placeholder="Name" type="text" ref="name"></input-field>
            <input-field class="mb-2" placeholder="Surname" type="text" ref="surname"></input-field>
            <input type="submit" value="Create" style="float:right;" />
            <input type="reset" value="Cancel" v-on:click="discardCreation" />
        </form>
        <div :class="$style.overlay" ref="overlay" data-display="false"></div>
    </div>
</template>
<script src="./teacher-crud.js"></script>
<style lang="sass" src="./teacher-crud.scss" module></style>
