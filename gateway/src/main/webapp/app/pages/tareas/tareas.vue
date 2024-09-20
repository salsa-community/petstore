<template>
  <div class="container">
    <b-row>
      <h1 v-text="t$('task.title')"></h1>
      <b-button variant="primary" class="float-right mb-2" @click="openCreateModalHandler">Agregar tarea</b-button>
    </b-row>
    <b-row>
      <b-table class="text-center" :items="listaTareas" :fields="fields" head-variant="dark" show-empty>
        <template #empty="scope">
          <h4 class="text-center">No hay registros para mostrar</h4>
        </template>
        <template #cell(acciones)="row">
          <b-button variant="primary" class="mr-2" @click="openEditModalHandler(row.item)">{{ $t('entity.action.edit') }}</b-button>
          <b-button variant="danger" @click="openDeleteModalHandler(row.item)">{{ $t('entity.action.delete') }}</b-button>
        </template>
        <template #cell(fechaLimite)="row">{{ row.item.fechaLimite.toLocaleDateString() }} </template>
      </b-table>
    </b-row>
  </div>

  <!-- Crear una nueva tarea-->
  <b-modal ref="createTareaModal" id="createTareaModal" title="Nueva tarea" header-bg-variant="primary" header-text-variant="white">
    <b-card class="border-0">
      <b-form-group
        label="Nombre"
        label-for="nombre-id"
        :state="isNombreValid()"
        invalid-feedback="Debe ingresar mínimo 3 caracteres y máximo 50."
      >
        <b-form-input
          id="nombre-id"
          :state="isNombreValid()"
          v-model="tareaToEdit.nombre"
          placeholder="Ingresa el nombre de la tarea"
        ></b-form-input>
      </b-form-group>

      <b-form-group
        label="Descripción"
        label-for="description-id"
        :state="isDescripcionValid()"
        invalid-feedback="Debe ingresar mínimo 3 caracteres y máximo 100."
      >
        <b-form-textarea
          id="description-id"
          :state="isDescripcionValid()"
          v-model="tareaToEdit.descripcion"
          placeholder="Ingresa la descripción de su tarea"
        ></b-form-textarea>
      </b-form-group>

      <b-form-group
        label="Fecha límite"
        label-for="deadline-id"
        :state="isDateValid()"
        invalid-feedback="La fecha debe ser mayor o igual a la fecha actual"
      >
        <b-form-datepicker
          id="deadline-id"
          :state="isDateValid()"
          v-model="tareaToEdit.fechaLimite"
          value-as-date
          locale="es"
        ></b-form-datepicker>
      </b-form-group>
    </b-card>

    <template #modal-footer>
      <b-button variant="outline-danger" @click="cancelHandler()">{{ $t('entity.action.cancel') }}</b-button>
      <b-button variant="primary" :disabled="!isFormValid()" @click="createTareaHandler()">{{ $t('entity.action.confirm') }}</b-button>
    </template>
  </b-modal>

  <!-- Editar una tarea-->
  <b-modal ref="editTareaModal" id="editTareaModal" title="Edición" header-bg-variant="primary" header-text-variant="white">
    <b-card>
      <b-form-group label="ID" label-for="tarea-id">
        <p id="tarea-id" v-text="tareaToEdit.id"></p>
      </b-form-group>

      <b-form-group
        label="Nombre"
        label-for="nombre-id"
        :state="isNombreValid()"
        invalid-feedback="Debe ingresar mínimo 3 caracteres y máximo 50."
      >
        <b-form-input
          id="nombre-id"
          :state="isNombreValid()"
          v-model="tareaToEdit.nombre"
          placeholder="Ingresa el nombre de la tarea"
        ></b-form-input>
      </b-form-group>

      <b-form-group
        label="Descripción"
        label-for="description-id"
        :state="isDescripcionValid()"
        invalid-feedback="Debe ingresar mínimo 3 caracteres y máximo 100."
      >
        <b-form-textarea
          id="description-id"
          :state="isDescripcionValid()"
          v-model="tareaToEdit.descripcion"
          placeholder="Ingresa la descripción de su tarea"
        ></b-form-textarea>
      </b-form-group>

      <b-form-group
        label="Fecha límite"
        label-for="deadline-id"
        :state="isDateValid()"
        invalid-feedback="La fecha debe ser mayor o igual a la fecha actual"
      >
        <b-form-datepicker
          id="deadline-id"
          :state="isDateValid()"
          v-model="tareaToEdit.fechaLimite"
          value-as-date
          locale="es"
        ></b-form-datepicker>
      </b-form-group>
    </b-card>
    <template #modal-footer>
      <b-button variant="outline-danger" @click="cancelHandler()">{{ $t('entity.action.cancel') }}</b-button>
      <b-button variant="primary" :disabled="!isFormValid()" @click="updateTareaHandler()">{{ $t('entity.action.confirm') }}</b-button>
    </template>
  </b-modal>

  <!-- Eliminar una tarea-->
  <b-modal ref="deleteTareaModal" id="deleteTareaModal" title="Confirmación" header-bg-variant="primary" header-text-variant="white">
    <p class="my-4">Está seguro de borrar la siguiente tarea?</p>

    <template #modal-footer>
      <b-button variant="outline-danger" @click="cancelHandler()">{{ $t('entity.action.cancel') }}</b-button>
      <b-button variant="primary" @click="deleteTareaHandler()">{{ $t('entity.action.confirm') }}</b-button>
    </template>
  </b-modal>
</template>

<script lang="ts" src="./tareas.component.ts"></script>
