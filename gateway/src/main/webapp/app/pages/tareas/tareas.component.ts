import { Tarea } from '@/shared/model/tarea.model';
import { defineComponent, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTareaStore } from '@/store';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Tareas',
  setup() {
    const tareas = useTareaStore();
    const nombreTarea: Ref<string> = ref('Valor por defecto');
    const edad: Ref<number> = ref(0);
    const listaTareas: Ref<Tarea[] | null> = ref(tareas.listaDeTareas);
    const tareaToEdit: Ref<Tarea> = ref(new Tarea());

    const confirmationModal = ref<any>(null);

    return {
      nombreTarea,
      listaTareas,
      edad,
      confirmationModal,
      tareaToEdit,
      tareas,
      t$: useI18n().t,
    };
  },
  methods: {
    addTarea(): void {
      if (this.listaTareas) {
        this.listaTareas.push(this.tareaToEdit);
        this.tareas.updateTareas(this.listaTareas);
      }
    },
    openModalHandler(): void {
      this.tareaToEdit = new Tarea();
      this.confirmationModal.show();
    },
    cancelHandler(): void {
      this.confirmationModal.hide();
    },
    confirmationHandler(): void {
      if (this.listaTareas) {
        this.listaTareas.push(this.tareaToEdit);
        this.tareaToEdit = new Tarea();
      }
      this.confirmationModal.hide();
    },
  },
});
