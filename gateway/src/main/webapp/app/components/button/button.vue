<template>
  <div>
    <b-button :variant="variant" @click="clickHandler">
      <b-icon  v-if="icon" :icon="icon"></b-icon>
      {{ name }}
      </b-button>
  </div>
</template>

<script lang="ts" src="./button.component.ts"></script>
