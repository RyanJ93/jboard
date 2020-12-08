<template>
    <div>
        <div :class="$style.alert" :data-display="display">
            <p>{{ text }}</p>
            <a class="button" v-on:click="cancel">{{ type === 'confirm' ? 'Cancel' : 'Close' }}</a>
            <a v-if="type === 'confirm'" class="button" v-on:click="confirm" style="float:left;">Confirm</a>
        </div>
        <div :class="$style.overlay" :data-display="display"></div>
    </div>
</template>
<script src="./alert-modal.js"></script>
<style lang="sass" src="./alert-modal.scss" module></style>
