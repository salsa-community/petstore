/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MascotaUpdate from './mascota-update.vue';
import MascotaService from './mascota.service';
import AlertService from '@/shared/alert/alert.service';

type MascotaUpdateComponentType = InstanceType<typeof MascotaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mascotaSample = { id: 'ABC' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MascotaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Mascota Management Update Component', () => {
    let comp: MascotaUpdateComponentType;
    let mascotaServiceStub: SinonStubbedInstance<MascotaService>;

    beforeEach(() => {
      route = {};
      mascotaServiceStub = sinon.createStubInstance<MascotaService>(MascotaService);
      mascotaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          mascotaService: () => mascotaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MascotaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mascota = mascotaSample;
        mascotaServiceStub.update.resolves(mascotaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mascotaServiceStub.update.calledWith(mascotaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        mascotaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MascotaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mascota = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mascotaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        mascotaServiceStub.find.resolves(mascotaSample);
        mascotaServiceStub.retrieve.resolves([mascotaSample]);

        // WHEN
        route = {
          params: {
            mascotaId: `${mascotaSample.id}`,
          },
        };
        const wrapper = shallowMount(MascotaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.mascota).toMatchObject(mascotaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mascotaServiceStub.find.resolves(mascotaSample);
        const wrapper = shallowMount(MascotaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
