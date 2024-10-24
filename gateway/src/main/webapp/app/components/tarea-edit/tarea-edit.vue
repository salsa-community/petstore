<template>
  <div>
    <b-form-group v-if="tarea.id" label="ID" label-for="tarea-id">
      <p id="tarea-id" v-text="tarea.id"></p>
    </b-form-group>
    <b-form-group :label="$t('task.nombre')" label-for="nombre-id" :state="isNombreValid()" :invalid-feedback="$t('task.validate.nombre')">
      <b-form-input
        id="nombre-id"
        :state="isNombreValid()"
        v-model="tarea.nombre"
        :placeholder="$t('task.messages.nombrePlaceholder')"
        :disabled="readonly"
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
        v-model="tarea.descripcion"
        :placeholder="$t('task.messages.descripcionPlaceholder')"
        :disabled="readonly"
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
        v-model="tarea.fechaLimite"
        value-as-date
        locale="es"
        :disabled="readonly"
      ></b-form-datepicker>
    </b-form-group>

    <div class="float-right" v-if="!readonly">
      <b-button class="mr-1" variant="outline-danger" @click="cancelHandler()">{{ $t('entity.action.cancel') }}</b-button>
      <b-button variant="primary" :disabled="!isFormValid()" @click="saveHandler()">{{ $t('entity.action.confirm') }}</b-button>
    </div>
  </div>
</template>

<script lang="ts" src="./tarea-edit.component.ts"></script>
