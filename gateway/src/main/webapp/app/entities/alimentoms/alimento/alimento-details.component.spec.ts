/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AlimentoDetails from './alimento-details.vue';
import AlimentoService from './alimento.service';
import AlertService from '@/shared/alert/alert.service';

type AlimentoDetailsComponentType = InstanceType<typeof AlimentoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const alimentoSample = { id: 'ABC' };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Alimento Management Detail Component', () => {
    let alimentoServiceStub: SinonStubbedInstance<AlimentoService>;
    let mountOptions: MountingOptions<AlimentoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      alimentoServiceStub = sinon.createStubInstance<AlimentoService>(AlimentoService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          alimentoService: () => alimentoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        alimentoServiceStub.find.resolves(alimentoSample);
        route = {
          params: {
            alimentoId: '' + 'ABC',
          },
        };
        const wrapper = shallowMount(AlimentoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.alimento).toMatchObject(alimentoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        alimentoServiceStub.find.resolves(alimentoSample);
        const wrapper = shallowMount(AlimentoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
