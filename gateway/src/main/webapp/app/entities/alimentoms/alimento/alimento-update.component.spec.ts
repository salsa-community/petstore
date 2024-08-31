/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AlimentoUpdate from './alimento-update.vue';
import AlimentoService from './alimento.service';
import AlertService from '@/shared/alert/alert.service';

type AlimentoUpdateComponentType = InstanceType<typeof AlimentoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const alimentoSample = { id: 'ABC' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AlimentoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Alimento Management Update Component', () => {
    let comp: AlimentoUpdateComponentType;
    let alimentoServiceStub: SinonStubbedInstance<AlimentoService>;

    beforeEach(() => {
      route = {};
      alimentoServiceStub = sinon.createStubInstance<AlimentoService>(AlimentoService);
      alimentoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          alimentoService: () => alimentoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AlimentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alimento = alimentoSample;
        alimentoServiceStub.update.resolves(alimentoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(alimentoServiceStub.update.calledWith(alimentoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        alimentoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AlimentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alimento = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(alimentoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        alimentoServiceStub.find.resolves(alimentoSample);
        alimentoServiceStub.retrieve.resolves([alimentoSample]);

        // WHEN
        route = {
          params: {
            alimentoId: `${alimentoSample.id}`,
          },
        };
        const wrapper = shallowMount(AlimentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.alimento).toMatchObject(alimentoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        alimentoServiceStub.find.resolves(alimentoSample);
        const wrapper = shallowMount(AlimentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
