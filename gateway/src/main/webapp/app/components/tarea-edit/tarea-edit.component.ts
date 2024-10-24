import type { ITarea } from '@/shared/model/tarea.model';
import { computed, defineComponent, type PropType } from 'vue';

export default defineComponent({
  compatConfig: { MODE: 3, COMPONENT_V_MODEL: false },
  name: 'TareaEdit',
  props: {
    modelValue: {
      type: Object as PropType<ITarea>,
      required: true,
    },
    readonly: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const tarea = computed({
      get: () => props.modelValue,
      set: value => emit('update:modelValue', value),
    });
    return {
      tarea,
      emit,
    };
  },
  methods: {
    isNombreValid(): boolean {
      if (this.tarea?.nombre?.length) {
        return this.tarea.nombre.length >= 3 && this.tarea.nombre.length <= 50;
      }
      return false;
    },
    isDescripcionValid(): boolean {
      if (this.tarea?.descripcion?.length) {
        return this.tarea.descripcion.length >= 3 && this.tarea.descripcion.length <= 100;
      }
      return false;
    },
    isDateValid(): boolean {
      if (this.tarea?.fechaLimite) {
        const now = new Date();
        const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const current = new Date(this.tarea.fechaLimite.getFullYear(), this.tarea.fechaLimite.getMonth(), this.tarea.fechaLimite.getDate());
        return current >= today;
      }
      return false;
    },
    isFormValid(): boolean {
      return this.isNombreValid() && this.isDescripcionValid() && this.isDateValid();
    },
    cancelHandler(): void {
      this.emit('cancel');
    },
    saveHandler(): void {
      this.emit('confirmed');
    },
  },
});
