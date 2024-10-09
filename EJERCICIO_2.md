# Gestor de tareas (Parte 2 - Componentes)

El objectivo de esta práctica es crear un componente reutilizable que permita registrar, editar y visualizar una tarea.


# Pasos a seguir

## Definición del componente tarea-edit.vue
1. Crear y adecuar el archivo `src/main/webapp/app/components/tarea-edit/tarea-edit.vue`
2. Crear y adecuar el archivo `src/main/webapp/app/components/tarea-edit/tarea-edit.component.vue`
3. Registrar el componente de manera global en el archivo `src/main/webapp/app/shared/config/config-custom-vue.ts`


## Funcionalidad del componente tarea-edit.vue

El componente `tarea-edit.vue` tiene como propósito el registro, actualización y visualización de una tarea `ITarea` y deberá de cumplir con las siguientes especificaciones técnicas:

1. Deberá de tener una propiedad llamada `tarea` de tipo `ITarea` que no podrá ser null o undefined.
2. Deberá de tener una propiedad llamada `readonly` de tipo Boolean y que tendrá un valor por defecto de `false`.
3. Si la variable `readonly` es igual a `true`, todos los campos deberán de mostrarse en sólo lectura, de otro modo, se debe permitir editar.
4. Deberá de emitir el evento `update:tarea` cuando el usuario el usuario quiera guardar el registro que ha capturado.
5. Deberá de emitir el evento `cancel:tarea` cuando el usuario el usuario quiera cancelar la edición, creación o visualización de una tarea.
6. El registro, actualización y visualización de la tarea deberá de cumplir las mismas reglas definidas en el documento [Especificación de una bandeja de tareas](EJERCICIO_1.md)


## Ejemplo de uso:

A continuación se muestra un ejemplo de uso del componente:

```html
<daic-tarea-edit 
    :tarea="tareaToEdit" 
    @update:tarea="updateTareaHandler"
    @cancel:tarea="cancelTareaHandler">
</daic-tarea-edit>
```

> Nota: El evento `update:tarea` deberá de manejar la lógica para identificar si la tarea es nueva o es una actualización


## Refactorización del componente tarea.vue

Por último, se deberá de reemplazar el componente `src/main/webapp/app/pages/tareas/tareas.vue` para que utilize al componente `<daic-tarea-edit></daic-tarea-edit>` y se evite repetir código.