<template>
  <div>
    <h2 id="page-heading" data-cy="AlimentoHeading">
      <span v-text="t$('gatewayApp.alimentomsAlimento.home.title')" id="alimento-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.alimentomsAlimento.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AlimentoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-alimento"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.alimentomsAlimento.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && alimentos && alimentos.length === 0">
      <span v-text="t$('gatewayApp.alimentomsAlimento.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="alimentos && alimentos.length > 0">
      <table class="table table-striped" aria-describedby="alimentos">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span v-text="t$('gatewayApp.alimentomsAlimento.nombre')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('precio')">
              <span v-text="t$('gatewayApp.alimentomsAlimento.precio')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'precio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('descripcion')">
              <span v-text="t$('gatewayApp.alimentomsAlimento.descripcion')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('foto')">
              <span v-text="t$('gatewayApp.alimentomsAlimento.foto')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'foto'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="alimento in alimentos" :key="alimento.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AlimentoView', params: { alimentoId: alimento.id } }">{{ alimento.id }}</router-link>
            </td>
            <td>{{ alimento.nombre }}</td>
            <td>{{ alimento.precio }}</td>
            <td>{{ alimento.descripcion }}</td>
            <td>
              <a v-if="alimento.foto" @click="openFile(alimento.fotoContentType, alimento.foto)">
                <img :src="'data:' + alimento.fotoContentType + ';base64,' + alimento.foto" style="max-height: 30px" alt="alimento" />
              </a>
              <span v-if="alimento.foto">{{ alimento.fotoContentType }}, {{ byteSize(alimento.foto) }}</span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AlimentoView', params: { alimentoId: alimento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AlimentoEdit', params: { alimentoId: alimento.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(alimento)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="gatewayApp.alimentomsAlimento.delete.question"
          data-cy="alimentoDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-alimento-heading" v-text="t$('gatewayApp.alimentomsAlimento.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-alimento"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeAlimento()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="alimentos && alimentos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alimento.component.ts"></script>
