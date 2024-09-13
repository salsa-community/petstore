<template>
  <div>
    <h1 v-text="t$('task.title')"></h1>
    <b-button variant="primary" class="mt-3" @click="openModalHandler">Agregar tarea</b-button>
    <h1>Lista de tareas</h1>

    <b-table :items="listaTareas" head-variant="dark"></b-table>
  </div>
  <b-modal ref="createTareaModal" id="modal-1" title="BootstrapVue">
    <p class="my-4">Ingresa la tarea que deseas agregar</p>

    <b-form-input class="mb-5" v-model="tareaToEdit.nombre" placeholder="Ingresa el nombre de la tarea"></b-form-input>
    <b-form-input v-model="tareaToEdit.descripcion" placeholder="Ingresa la descripción de su tarea"></b-form-input>

    <template #modal-footer>
      <b-button variant="outline-danger" @click="cancelHandler()">{{ $t('entity.action.cancel') }}</b-button>
      <b-button variant="primary" @click="confirmationHandler()">{{ $t('entity.action.confirm') }}</b-button>
    </template>
  </b-modal>
</template>

<script lang="ts" src="./tareas.component.ts"></script>
