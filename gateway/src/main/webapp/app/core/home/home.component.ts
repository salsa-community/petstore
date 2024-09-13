import { type ComputedRef, defineComponent, inject } from 'vue';
import { useI18n } from 'vue-i18n';

import type LoginService from '@/account/login.service';
import { useTareaStore } from '@/store';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const tareasFrom = useTareaStore();
    const loginService = inject<LoginService>('loginService');

    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    const openLogin = () => {
      loginService.openLogin();
    };

    return {
      authenticated,
      username,
      openLogin,
      tareasFrom,
      t$: useI18n().t,
    };
  },
});
