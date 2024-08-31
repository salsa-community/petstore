<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.mascotamsMascota.home.createOrEditLabel"
          data-cy="MascotaCreateUpdateHeading"
          v-text="t$('gatewayApp.mascotamsMascota.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="mascota.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="mascota.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.mascotamsMascota.nombre')" for="mascota-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="mascota-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.mascotamsMascota.edad')" for="mascota-edad"></label>
            <input
              type="number"
              class="form-control"
              name="edad"
              id="mascota-edad"
              data-cy="edad"
              :class="{ valid: !v$.edad.$invalid, invalid: v$.edad.$invalid }"
              v-model.number="v$.edad.$model"
              required
            />
            <div v-if="v$.edad.$anyDirty && v$.edad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.edad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.mascotamsMascota.precio')" for="mascota-precio"></label>
            <input
              type="number"
              class="form-control"
              name="precio"
              id="mascota-precio"
              data-cy="precio"
              :class="{ valid: !v$.precio.$invalid, invalid: v$.precio.$invalid }"
              v-model.number="v$.precio.$model"
              required
            />
            <div v-if="v$.precio.$anyDirty && v$.precio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.precio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.mascotamsMascota.fechaNacimiento')"
              for="mascota-fechaNacimiento"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="mascota-fechaNacimiento"
                  v-model="v$.fechaNacimiento.$model"
                  name="fechaNacimiento"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="mascota-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !v$.fechaNacimiento.$invalid, invalid: v$.fechaNacimiento.$invalid }"
                v-model="v$.fechaNacimiento.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.mascotamsMascota.foto')" for="mascota-foto"></label>
            <div>
              <img
                :src="'data:' + mascota.fotoContentType + ';base64,' + mascota.foto"
                style="max-height: 100px"
                v-if="mascota.foto"
                alt="mascota"
              />
              <div v-if="mascota.foto" class="form-text text-danger clearfix">
                <span class="pull-left">{{ mascota.fotoContentType }}, {{ byteSize(mascota.foto) }}</span>
                <button
                  type="button"
                  @click="clearInputImage('foto', 'fotoContentType', 'file_foto')"
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <label for="file_foto" v-text="t$('entity.action.addimage')" class="btn btn-primary pull-right"></label>
              <input
                type="file"
                ref="file_foto"
                id="file_foto"
                style="display: none"
                data-cy="foto"
                @change="setFileData($event, mascota, 'foto', true)"
                accept="image/*"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="foto"
              id="mascota-foto"
              data-cy="foto"
              :class="{ valid: !v$.foto.$invalid, invalid: v$.foto.$invalid }"
              v-model="v$.foto.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="fotoContentType"
              id="mascota-fotoContentType"
              v-model="mascota.fotoContentType"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./mascota-update.component.ts"></script>
