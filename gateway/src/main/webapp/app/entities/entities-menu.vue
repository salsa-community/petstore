<template>
  <div>
    <b-dropdown-item to="/mascota">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.mascotamsMascota')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/alimento">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.alimentomsAlimento')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/tareas">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.task')"> </span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
