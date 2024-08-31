import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MascotaService from './mascota.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IMascota, Mascota } from '@/shared/model/mascotams/mascota.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MascotaUpdate',
  setup() {
    const mascotaService = inject('mascotaService', () => new MascotaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mascota: Ref<IMascota> = ref(new Mascota());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      edad: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
        max: validations.maxValue(t$('entity.validation.max', { max: 200 }).toString(), 200),
      },
      precio: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      fechaNacimiento: {},
      foto: {},
    };
    const v$ = useVuelidate(validationRules, mascota as any);
    v$.value.$validate();

    return {
      mascotaService,
      alertService,
      mascota,
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
      if (this.mascota.id) {
        this.mascotaService()
          .update(this.mascota)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('mascotamsApp.mascotamsMascota.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mascotaService()
          .create(this.mascota)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('mascotamsApp.mascotamsMascota.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    clearInputImage(field, fieldContentType, idInput): void {
      if (this.mascota && field && fieldContentType) {
        if (Object.hasOwn(this.mascota, field)) {
          this.mascota[field] = null;
        }
        if (Object.hasOwn(this.mascota, fieldContentType)) {
          this.mascota[fieldContentType] = null;
        }
        if (idInput) {
          (<any>this).$refs[idInput] = null;
        }
      }
    },
  },
});
