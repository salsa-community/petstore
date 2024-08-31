import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MascotaService from './mascota.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IMascota } from '@/shared/model/mascotams/mascota.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MascotaDetails',
  setup() {
    const mascotaService = inject('mascotaService', () => new MascotaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mascota: Ref<IMascota> = ref({});

    const retrieveMascota = async mascotaId => {
      try {
        const res = await mascotaService().find(mascotaId);
        mascota.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mascotaId) {
      retrieveMascota(route.params.mascotaId);
    }

    return {
      alertService,
      mascota,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});
