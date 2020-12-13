<template>
  <div>
    <nav :class="$style.wrapper" ref="menu">
      <p :class="$style.logo">jBoard</p>
      <ul :class="$style.menu">
        <li v-for="view in views" :data-vid="view.id" v-on:click="showView" :data-selected="activeView === view.id">{{ view.label }}</li>
        <li v-on:click="logout">Logout</li>
      </ul>
    </nav>
    <div :class="$style.overlay" ref="overlay" v-on:click="closeMenu"></div>
  </div>
</template>
<script src="./lateral-menu.js"></script>
<style lang="sass" src="./lateral-menu.scss" module></style>
