import { Tarea } from '@/shared/model/tarea.model';
import { defineComponent, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Tareas',
  setup() {
    const nombreTarea: Ref<string> = ref('Valor por defecto');
    const edad: Ref<number> = ref(0);
    const listaTareas: Ref<Tarea[]> = ref([]);
    const tareaToEdit: Ref<Tarea> = ref(new Tarea());

    const confirmationModal = ref<any>(null);

    return {
      nombreTarea,
      listaTareas,
      edad,
      confirmationModal,
      tareaToEdit,
      t$: useI18n().t,
    };
  },
  methods: {
    addTarea(): void {
      this.listaTareas.push(this.tareaToEdit);
    },
    openModalHandler(): void {
      this.tareaToEdit = new Tarea();
      this.confirmationModal.show();
    },
    cancelHandler(): void {
      this.confirmationModal.hide();
    },
    confirmationHandler(): void {
      this.listaTareas.push(this.tareaToEdit);
      this.confirmationModal.hide();
      this.tareaToEdit = new Tarea();
    },
  },
});
