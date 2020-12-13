<template>
  <header :class="$style.header">
    <div :class="$style.hamburger" v-on:click="openLateralMenu"></div>
    <p :class="$style.logo">jBoard</p>
  </header>
</template>
<script src="./header-bar.js"></script>
<style lang="sass" src="./header-bar.scss" module></style>
