<template>
    <div>
        <div>
            <h3 class="title d-inline-block">Teachers management</h3>
            <div :class="$style.controls">
                <a class="button" v-on:click="openCreationForm" title="Add a new teacher.">
                    <i class="fas fa-plus"></i><span class="d-none d-sm-inline-block">&nbsp;Add</span>
                </a>
            </div>
        </div>
        <ul :class="$style.list">
            <li v-for="teacher in teachers" :data-tid="teacher.id">
                <div :class="$style.info">
                    <p>{{ teacher.name + ' ' + teacher.surname }}</p>
                </div>
                <div :class="$style.controls">
                    <a class="button" v-on:click="remove" title="Delete this teacher.">
                        <i class="fas fa-trash"></i><span class="d-none d-sm-inline-block">&nbsp;Delete</span>
                    </a>
                </div>
            </li>
        </ul>
        <div :class="$style.creationDialog" data-display="false" ref="creationDialog">
            <form :class="$style.form" ref="form" v-on:submit="handleCreation">
                <h5 class="mb-4">Add a new teacher</h5>
                <input-field class="mb-2" placeholder="Name" type="text" ref="name"></input-field>
                <input-field class="mb-2" placeholder="Surname" type="text" ref="surname"></input-field>
                <div :class="$style.controls">
                    <input type="submit" value="Create" style="float:right;" />
                    <input type="reset" value="Cancel" v-on:click="discardCreation" />
                </div>
            </form>
            <div :class="$style.overlay" ref="overlay"></div>
        </div>
    </div>
</template>
<script src="./teacher-crud.js"></script>
<style lang="sass" src="./teacher-crud.scss" module></style>
