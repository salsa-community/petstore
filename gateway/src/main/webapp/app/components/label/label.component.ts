import { defineComponent } from 'vue';

export default defineComponent({
  compatConfig: { MODE: 3, COMPONENT_V_MODEL: false },
  name: 'LabelComponent',
  props: {
    title: {
      type: String,
      default: 'Titulo general no implentado',
    },
  },
  setup() {},
});
