<template>
  <div>
    <h1>Administración de tareas</h1>
    <b-button variant="primary" class="mt-3" @click="openModalHandler">Agregar tarea</b-button>
    <h1>Lista de tareas</h1>

    <div v-for="tarea in listaTareas">
      <p>{{ tarea }}</p>
    </div>
  </div>
  <b-modal ref="confirmationModal" id="modal-1" title="BootstrapVue">
    <p class="my-4">Ingresa la tarea que deseas agregar</p>

    <b-form-input v-model="nombreTarea" placeholder="Ingresa el nombre de la tarea"></b-form-input>

    <template #modal-footer>
      <b-button variant="outline-danger" @click="cancelHandler()">{{ $t('entity.action.cancel') }}</b-button>
      <b-button variant="primary" @click="confirmationHandler()">{{ $t('entity.action.confirm') }}</b-button>
    </template>
  </b-modal>
</template>

<script lang="ts" src="./tareas.component.ts"></script>
