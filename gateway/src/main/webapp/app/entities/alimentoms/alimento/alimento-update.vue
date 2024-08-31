<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.alimentomsAlimento.home.createOrEditLabel"
          data-cy="AlimentoCreateUpdateHeading"
          v-text="t$('gatewayApp.alimentomsAlimento.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="alimento.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="alimento.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.alimentomsAlimento.nombre')" for="alimento-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="alimento-nombre"
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
            <label class="form-control-label" v-text="t$('gatewayApp.alimentomsAlimento.precio')" for="alimento-precio"></label>
            <input
              type="number"
              class="form-control"
              name="precio"
              id="alimento-precio"
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
            <label class="form-control-label" v-text="t$('gatewayApp.alimentomsAlimento.descripcion')" for="alimento-descripcion"></label>
            <textarea
              class="form-control"
              name="descripcion"
              id="alimento-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.alimentomsAlimento.foto')" for="alimento-foto"></label>
            <div>
              <img
                :src="'data:' + alimento.fotoContentType + ';base64,' + alimento.foto"
                style="max-height: 100px"
                v-if="alimento.foto"
                alt="alimento"
              />
              <div v-if="alimento.foto" class="form-text text-danger clearfix">
                <span class="pull-left">{{ alimento.fotoContentType }}, {{ byteSize(alimento.foto) }}</span>
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
                @change="setFileData($event, alimento, 'foto', true)"
                accept="image/*"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="foto"
              id="alimento-foto"
              data-cy="foto"
              :class="{ valid: !v$.foto.$invalid, invalid: v$.foto.$invalid }"
              v-model="v$.foto.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="fotoContentType"
              id="alimento-fotoContentType"
              v-model="alimento.fotoContentType"
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
<script lang="ts" src="./alimento-update.component.ts"></script>
