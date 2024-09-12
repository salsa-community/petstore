import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Mascota = () => import('@/entities/mascotams/mascota/mascota.vue');
const MascotaUpdate = () => import('@/entities/mascotams/mascota/mascota-update.vue');
const MascotaDetails = () => import('@/entities/mascotams/mascota/mascota-details.vue');

const Alimento = () => import('@/entities/alimentoms/alimento/alimento.vue');
const AlimentoUpdate = () => import('@/entities/alimentoms/alimento/alimento-update.vue');
const AlimentoDetails = () => import('@/entities/alimentoms/alimento/alimento-details.vue');

const Tareas = () => import ('@/pages/tareas/tareas.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'tareas',
      name: 'Tareas',
      component: Tareas,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mascota',
      name: 'Mascota',
      component: Mascota,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mascota/new',
      name: 'MascotaCreate',
      component: MascotaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mascota/:mascotaId/edit',
      name: 'MascotaEdit',
      component: MascotaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'mascota/:mascotaId/view',
      name: 'MascotaView',
      component: MascotaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alimento',
      name: 'Alimento',
      component: Alimento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alimento/new',
      name: 'AlimentoCreate',
      component: AlimentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alimento/:alimentoId/edit',
      name: 'AlimentoEdit',
      component: AlimentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alimento/:alimentoId/view',
      name: 'AlimentoView',
      component: AlimentoDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
