<template>
  <div class="container">
    <b-row>
      <b-col>
        <daic-label :title="t$('task.title')"></daic-label>
      </b-col>
      <b-col>
        <daic-button
          class="float-right"
          :name="$t('entity.action.create')"
          @confirmed="openCreateModalHandler"
          @click="clickHandler"
        ></daic-button>
      </b-col>
    </b-row>
    <b-row>
      <b-table class="text-center" :items="listaTareas" :fields="fields" head-variant="dark" show-empty>
        <template #empty="scope">
          <h4 class="text-center">{{ $t('task.messages.empty-data') }}</h4>
        </template>
        <template #head(nombre)="row">{{ $t('task.nombre') }} </template>
        <template #head(descripcion)="row">{{ $t('task.descripcion') }} </template>
        <template #head(fechaLimite)="row">{{ $t('task.fechaLimite') }} </template>
        <template #head(acciones)="row">{{ $t('task.acciones') }} </template>

        <template #cell(fechaLimite)="row">{{ row.item.fechaLimite.toLocaleDateString() }} </template>
        <template #cell(acciones)="row">
          <b-button variant="primary" class="mr-2" @click="openEditModalHandler(row.item)">{{ $t('entity.action.edit') }}</b-button>
          <b-button variant="danger" @click="openDeleteModalHandler(row.item)">{{ $t('entity.action.delete') }}</b-button>
        </template>
      </b-table>
    </b-row>
  </div>

  <!-- Crear una nueva tarea-->
  <b-modal
    ref="createTareaModal"
    id="createTareaModal"
    :title="$t('task.messages.new')"
    header-bg-variant="primary"
    header-text-variant="white"
  >
    <b-card class="border-0">
      <b-form-group
        :label="$t('task.nombre')"
        label-for="nombre-id"
        :state="isNombreValid()"
        :invalid-feedback="$t('task.validate.nombre')"
      >
        <b-form-input
          id="nombre-id"
          :state="isNombreValid()"
          v-model="tareaToEdit.nombre"
          :placeholder="$t('task.messages.nombrePlaceholder')"
        ></b-form-input>
      </b-form-group>

      <b-form-group
        :label="$t('task.descripcion')"
        label-for="description-id"
        :state="isDescripcionValid()"
        :invalid-feedback="$t('task.validate.descripcion')"
      >
        <b-form-textarea
          id="description-id"
          :state="isDescripcionValid()"
          v-model="tareaToEdit.descripcion"
          :placeholder="$t('task.messages.descripcionPlaceholder')"
        ></b-form-textarea>
      </b-form-group>

      <b-form-group
        :label="$t('task.fechaLimite')"
        label-for="deadline-id"
        :state="isDateValid()"
        :invalid-feedback="$t('task.validate.fechaLimite')"
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
  <b-modal
    ref="editTareaModal"
    id="editTareaModal"
    :title="$t('entity.messages.edit')"
    header-bg-variant="primary"
    header-text-variant="white"
  >
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
  <b-modal
    ref="deleteTareaModal"
    id="deleteTareaModal"
    :title="$t('task.messages.delete')"
    header-bg-variant="primary"
    header-text-variant="white"
  >
    <p class="my-4 text-center">{{ $t('task.messages.confirmacion') }}</p>

    <template #modal-footer>
      <b-button variant="outline-danger" @click="cancelHandler()">{{ $t('entity.action.cancel') }}</b-button>
      <b-button variant="primary" @click="deleteTareaHandler()">{{ $t('entity.action.confirm') }}</b-button>
    </template>
  </b-modal>
</template>

<script lang="ts" src="./tareas.component.ts"></script>
