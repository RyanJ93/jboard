<template>
  <header :class="$style.header">
    <div :class="$style.hamburger" v-on:click="openLateralMenu"></div>
    <div :class="$style.logoWrapper">
        <div :class="$style.logo"></div>
        <p :class="$style.title">jBoard</p>
    </div>
  </header>
</template>
<script src="./header-bar.js"></script>
<style lang="sass" src="./header-bar.scss" module></style>
