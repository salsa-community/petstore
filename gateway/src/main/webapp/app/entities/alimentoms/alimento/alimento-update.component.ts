import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AlimentoService from './alimento.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Alimento, type IAlimento } from '@/shared/model/alimentoms/alimento.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AlimentoUpdate',
  setup() {
    const alimentoService = inject('alimentoService', () => new AlimentoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const alimento: Ref<IAlimento> = ref(new Alimento());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      precio: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      descripcion: {},
      foto: {},
    };
    const v$ = useVuelidate(validationRules, alimento as any);
    v$.value.$validate();

    return {
      alimentoService,
      alertService,
      alimento,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.alimento.id) {
        this.alimentoService()
          .update(this.alimento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('alimentomsApp.alimentomsAlimento.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.alimentoService()
          .create(this.alimento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('alimentomsApp.alimentomsAlimento.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    clearInputImage(field, fieldContentType, idInput): void {
      if (this.alimento && field && fieldContentType) {
        if (Object.hasOwn(this.alimento, field)) {
          this.alimento[field] = null;
        }
        if (Object.hasOwn(this.alimento, fieldContentType)) {
          this.alimento[fieldContentType] = null;
        }
        if (idInput) {
          (<any>this).$refs[idInput] = null;
        }
      }
    },
  },
});
