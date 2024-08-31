import { defineComponent, provide } from 'vue';

import MascotaService from './mascotams/mascota/mascota.service';
import AlimentoService from './alimentoms/alimento/alimento.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('mascotaService', () => new MascotaService());
    provide('alimentoService', () => new AlimentoService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
