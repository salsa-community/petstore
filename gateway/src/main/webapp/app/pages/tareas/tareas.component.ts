import { Tarea } from '@/shared/model/tarea.model';
import { defineComponent, ref, type Ref, inject, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTareaStore } from '@/store';
import TareaEdit from '@/components/tarea-edit/tarea-edit.vue';
import TareaService from '@/pages/tareas/tareas.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  emits: ['confirmed'],
  components: {
    'tarea-edit': TareaEdit,
  },
  name: 'Tareas',
  setup() {
    const tareaService = inject('tareaService', () => new TareaService());

    const textLabel: Ref<string> = ref('Hola mundo');
    const tareaStore = useTareaStore();
    const listaTareas: Ref<Tarea[] | null> = ref([]);
    const tareaToEdit: Ref<Tarea> = ref(new Tarea());
    const fields: Ref<string[]> = ref(['id', 'nombre', 'fechaLimite', 'acciones']);

    const createTareaModal = ref<any>(null);
    const deleteTareaModal = ref<any>(null);
    const editTareaModal = ref<any>(null);
    const isFetching: Ref<boolean> = ref(false);

    const listarTareas = () => {
      isFetching.value = true;
      tareaService()
        .listar()
        .then(res => {
          listaTareas.value = res.data;
          if (listaTareas.value) {
            listaTareas.value?.forEach(tarea => {
              if (tarea.fechaLimite) {
                tarea.fechaLimite = new Date(tarea.fechaLimite);
              }
            });
          }
        })
        .catch(err => {
          console.log(err);
        })
        .finally(() => {
          isFetching.value = false;
        });
    };

    onMounted(() => {
      listarTareas();
    });

    return {
      textLabel,
      listaTareas,
      createTareaModal,
      deleteTareaModal,
      editTareaModal,
      tareaToEdit,
      tareaStore,
      fields,
      isFetching,
      t$: useI18n().t,
      tareaService,
      listarTareas,
    };
  },
  methods: {
    openCreateModalHandler(): void {
      this.tareaToEdit = new Tarea();
      this.createTareaModal.show();
    },
    clickHandler(): void {
      console.log('Se ejecuto un click');
    },
    openEditModalHandler(tarea: any): void {
      this.createEditableTarea(tarea);
      this.editTareaModal.show();
    },
    openDeleteModalHandler(tarea: Tarea): void {
      this.createEditableTarea(tarea);
      this.deleteTareaModal.show();
    },
    createEditableTarea(tarea: Tarea): void {
      this.tareaToEdit = JSON.parse(JSON.stringify(tarea));
      this.tareaToEdit.fechaLimite = tarea.fechaLimite;
    },
    createTareaHandler(): void {
      this.isFetching = true;
      this.tareaService()
        .crear(this.tareaToEdit)
        .then(tarea => {
          this.listarTareas();
        })
        .catch(err => {
          console.log(err);
        })
        .finally(() => {
          this.isFetching = false;
        });
      this.createTareaModal.hide();
    },
    deleteTareaHandler(): void {
      if (this.listaTareas) {
        const index = this.listaTareas.findIndex(tarea => tarea.id === this.tareaToEdit.id);
        this.listaTareas.splice(index, 1);
        this.deleteTareaModal.hide();
      }
    },
    updateTareaHandler(): void {
      if (this.listaTareas) {
        const index = this.listaTareas.findIndex(tarea => tarea.id === this.tareaToEdit.id);
        this.listaTareas.splice(index, 1, this.tareaToEdit);
        this.editTareaModal.hide();
      }
    },
    cancelHandler(): void {
      this.createTareaModal.hide();
      this.deleteTareaModal.hide();
      this.editTareaModal.hide();
    },
    keygenerator(): string {
      return new Date().getTime().toString();
    },
  },
});
