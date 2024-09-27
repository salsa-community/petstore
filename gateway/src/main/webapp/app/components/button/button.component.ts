import { defineComponent } from 'vue';

export default defineComponent({
  compatConfig: { MODE: 3, COMPONENT_V_MODEL: false },
  emits:['confirmed', 'click'],
  name: 'ButtonComponent',
  props: {
    name: {
      type: String,
      default: 'Guardar',
    },
    icon: {
      type: String,
      default: null,
    },
    variant: {
      type: String,
      default: 'primary',
    },
  },
  setup() {},
  methods: {
    clickHandler() {
      this.$emit('confirmed');
      this.$emit('click');
    },
  },
});
