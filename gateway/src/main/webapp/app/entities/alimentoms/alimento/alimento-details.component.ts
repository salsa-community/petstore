import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AlimentoService from './alimento.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IAlimento } from '@/shared/model/alimentoms/alimento.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AlimentoDetails',
  setup() {
    const alimentoService = inject('alimentoService', () => new AlimentoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const alimento: Ref<IAlimento> = ref({});

    const retrieveAlimento = async alimentoId => {
      try {
        const res = await alimentoService().find(alimentoId);
        alimento.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.alimentoId) {
      retrieveAlimento(route.params.alimentoId);
    }

    return {
      alertService,
      alimento,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});
