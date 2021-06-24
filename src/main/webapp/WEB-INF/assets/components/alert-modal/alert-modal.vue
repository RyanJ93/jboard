<template>
    <div :class="$style.wrapper" :data-display="display">
        <div :class="$style.alert">
            <p>{{ text }}</p>
            <a v-if="type !== 'network-alert'" class="button" v-on:click="cancel">{{ type === 'confirm' ? 'Cancel' : 'Close' }}</a>
            <a v-if="type === 'confirm'" class="button-danger" v-on:click="confirm" style="float:left;">Confirm</a>
            <a v-if="type === 'network-alert'" class="button" v-on:click="cancel">Retry</a>
        </div>
        <div :class="$style.overlay"></div>
    </div>
</template>
<script src="./alert-modal.js"></script>
<style lang="sass" src="./alert-modal.scss" module></style>
