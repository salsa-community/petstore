import type { Tarea } from '@/shared/model/tarea.model';
import { defineStore } from 'pinia';

export interface TareaStateStorable {
  tareas: Tarea[] | null;
}

export const defaultTareaState: TareaStateStorable = {
  tareas: [],
};

export const useTareaStore = defineStore('tarea', {
  state: (): TareaStateStorable => ({ ...defaultTareaState }),
  getters: {
    listaDeTareas: state => state.tareas,
  },
  actions: {
    updateTareas(updatedTareas: Tarea[]) {
      this.tareas = updatedTareas;
    },
  },
});
