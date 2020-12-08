<template>
    <div>
        <form :class="$style.form" autocomplete="on" v-on:submit="handleSubmit" :data-display="display">
            <h5 class="mb-4">Log in to your account</h5>
            <input-field class="mb-2" placeholder="Account" type="text" ref="account"></input-field>
            <input-field class="mb-4" placeholder="Password" type="password" ref="password"></input-field>
            <input type="submit" value="Login" />
        </form>
        <div :class="$style.overlay" :data-display="display"></div>
    </div>
</template>
<script src="./login-form.js"></script>
<style lang="sass" src="./login-form.scss" module></style>
