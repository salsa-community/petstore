<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="alimento">
        <h2 class="jh-entity-heading" data-cy="alimentoDetailsHeading">
          <span v-text="t$('gatewayApp.alimentomsAlimento.detail.title')"></span> {{ alimento.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.alimentomsAlimento.nombre')"></span>
          </dt>
          <dd>
            <span>{{ alimento.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.alimentomsAlimento.precio')"></span>
          </dt>
          <dd>
            <span>{{ alimento.precio }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.alimentomsAlimento.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ alimento.descripcion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.alimentomsAlimento.foto')"></span>
          </dt>
          <dd>
            <div v-if="alimento.foto">
              <a @click="openFile(alimento.fotoContentType, alimento.foto)">
                <img :src="'data:' + alimento.fotoContentType + ';base64,' + alimento.foto" style="max-width: 100%" alt="alimento" />
              </a>
              {{ alimento.fotoContentType }}, {{ byteSize(alimento.foto) }}
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="alimento.id" :to="{ name: 'AlimentoEdit', params: { alimentoId: alimento.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alimento-details.component.ts"></script>
