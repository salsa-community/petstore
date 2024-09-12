import { defineComponent, ref, type Ref } from 'vue';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Tareas',
  setup() {
    const nombreTarea: Ref<string> = ref('Valor por defecto');
    const edad: Ref<number> = ref(0);
    const listaTareas: Ref<string[]> = ref([]);

    const confirmationModal = ref<any>(null);

    return {
      nombreTarea,
      listaTareas,
      edad,
      confirmationModal,
    };
  },
  methods: {
    addTarea(): void {
      this.listaTareas.push(this.nombreTarea);
    },
    openModalHandler(): void {
      this.nombreTarea = '';
      this.confirmationModal.show();
    },
    cancelHandler(): void {
      this.confirmationModal.hide();
    },
    confirmationHandler(): void {
      this.listaTareas.push(this.nombreTarea);
      this.confirmationModal.hide();
      this.nombreTarea = '';
    },
  },
});
