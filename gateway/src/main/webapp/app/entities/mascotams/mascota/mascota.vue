<template>
  <div>
    <d-label title="Otro titulo para mascotas"></d-label>

    <h2 id="page-heading" data-cy="MascotaHeading">
      <span v-text="t$('gatewayApp.mascotamsMascota.home.title')" id="mascota-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.mascotamsMascota.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MascotaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-mascota"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.mascotamsMascota.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && mascotas && mascotas.length === 0">
      <span v-text="t$('gatewayApp.mascotamsMascota.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="mascotas && mascotas.length > 0">
      <table class="table table-striped" aria-describedby="mascotas">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span v-text="t$('gatewayApp.mascotamsMascota.nombre')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('edad')">
              <span v-text="t$('gatewayApp.mascotamsMascota.edad')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'edad'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('precio')">
              <span v-text="t$('gatewayApp.mascotamsMascota.precio')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'precio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaNacimiento')">
              <span v-text="t$('gatewayApp.mascotamsMascota.fechaNacimiento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaNacimiento'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('foto')">
              <span v-text="t$('gatewayApp.mascotamsMascota.foto')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'foto'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mascota in mascotas" :key="mascota.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MascotaView', params: { mascotaId: mascota.id } }">{{ mascota.id }}</router-link>
            </td>
            <td>{{ mascota.nombre }}</td>
            <td>{{ mascota.edad }}</td>
            <td>{{ mascota.precio }}</td>
            <td>{{ mascota.fechaNacimiento }}</td>
            <td>
              <a v-if="mascota.foto" @click="openFile(mascota.fotoContentType, mascota.foto)">
                <img :src="'data:' + mascota.fotoContentType + ';base64,' + mascota.foto" style="max-height: 30px" alt="mascota" />
              </a>
              <span v-if="mascota.foto">{{ mascota.fotoContentType }}, {{ byteSize(mascota.foto) }}</span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MascotaView', params: { mascotaId: mascota.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MascotaEdit', params: { mascotaId: mascota.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(mascota)"
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
        <span ref="infiniteScrollEl"></span>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="gatewayApp.mascotamsMascota.delete.question"
          data-cy="mascotaDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-mascota-heading" v-text="t$('gatewayApp.mascotamsMascota.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-mascota"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeMascota()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./mascota.component.ts"></script>
