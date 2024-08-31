<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="mascota">
        <h2 class="jh-entity-heading" data-cy="mascotaDetailsHeading">
          <span v-text="t$('gatewayApp.mascotamsMascota.detail.title')"></span> {{ mascota.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.mascotamsMascota.nombre')"></span>
          </dt>
          <dd>
            <span>{{ mascota.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.mascotamsMascota.edad')"></span>
          </dt>
          <dd>
            <span>{{ mascota.edad }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.mascotamsMascota.precio')"></span>
          </dt>
          <dd>
            <span>{{ mascota.precio }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.mascotamsMascota.fechaNacimiento')"></span>
          </dt>
          <dd>
            <span>{{ mascota.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.mascotamsMascota.foto')"></span>
          </dt>
          <dd>
            <div v-if="mascota.foto">
              <a @click="openFile(mascota.fotoContentType, mascota.foto)">
                <img :src="'data:' + mascota.fotoContentType + ';base64,' + mascota.foto" style="max-width: 100%" alt="mascota" />
              </a>
              {{ mascota.fotoContentType }}, {{ byteSize(mascota.foto) }}
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="mascota.id" :to="{ name: 'MascotaEdit', params: { mascotaId: mascota.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./mascota-details.component.ts"></script>
