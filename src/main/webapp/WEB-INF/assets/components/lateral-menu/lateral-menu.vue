<template>
  <nav :class="$style.wrapper">
    <ul :class="$style.menu">
      <li v-for="view in views" :data-vid="view.id" v-on:click="showView">{{ view.label }}</li>
      <li v-on:click="logout">Logout</li>
    </ul>
  </nav>
</template>
<script src="./lateral-menu.js"></script>
<style lang="sass" src="./lateral-menu.scss" module></style>
