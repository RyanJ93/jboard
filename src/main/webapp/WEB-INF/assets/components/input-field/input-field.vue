<template>
  <div :class="$style.wrapper">
    <div :class="$style.inner">
      <input :class="$style.input" placeholder=" " :type="type" ref="input" />
      <label :class="$style.label">{{ placeholder }}</label>
    </div>
    <span v-if="errorMessage !== null" :class="$style.error">{{ errorMessage }}</span>
  </div>
</template>
<script src="./input-field.js"></script>
<style lang="sass" src="./input-field.scss" module></style>
