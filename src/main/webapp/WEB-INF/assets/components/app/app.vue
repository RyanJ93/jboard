<template>
    <div :class="$style.app">
        <main :class="$style.main" :data-disabled="disabled">
            <header-bar></header-bar>
            <lateral-menu ref="lateralMenu"></lateral-menu>
            <div ref="views">
                <div v-for="view in views" :class="$style.container" data-active="false" :data-vid="view.id">
                    <component :is="view.component" :ref="'view-' + view.id"></component>
                </div>
            </div>
        </main>
        <login-form ref="loginForm"></login-form>
        <alert-modal ref="alertModal"></alert-modal>
    </div>
</template>
<script src="./app.js"></script>
<style lang="sass" src="./app.scss" module></style>
