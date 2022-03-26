<template>
    <div>
        <nav :class="$style.wrapper" ref="menu">
            <p :class="$style.logo">jBoard</p>
            <ul :class="$style.menu">
                <li v-for="view in views" :data-vid="view.id" v-on:click="showView" :data-selected="activeView === view.id">
                    <div :class="$style.icon">
                        <i :class="'fas fa-' + view.icon"></i>
                    </div>
                    <span>{{ view.label }}</span>
                </li>
                <li v-on:click="logout">
                    <div :class="$style.icon">
                        <i class="fas fa-sign-out-alt"></i>
                    </div>
                    <span>Logout</span>
                </li>
            </ul>
            <p :class="$style.credits">Developed with ❤️ by <a target="_blank" href="https://www.enricosola.dev">Enrico Sola</a></p>
        </nav>
        <div :class="$style.overlay" ref="overlay" v-on:click="closeMenu"></div>
    </div>
</template>
<script src="./lateral-menu.js"></script>
<style lang="sass" src="./lateral-menu.scss" module></style>
