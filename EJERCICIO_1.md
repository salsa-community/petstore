# Gestor de tareas

El objectivo de esta práctica es crear un gestor de tareas que permita realizar las siguientes funcionalidades:

1. Mostrar las lista de tareas registradas
2. Crear una nueva tarea y agregarla a la lista de tareas
2. Modificar una tarea
4. Eliminar una tarea de la lista de tareas

El digrama general con las pantallas de este módulo, se muestra a continuación:

<img title="a title" alt="Alt text" src="task-manager.png ">

A continuación se describen las > **Historias de usuario** y sus respectivos **criterios de aceptación** para el cumplimiento de esta práctica:

> **Historia de usuario**
> Cómo Usuario quiero ver todas las tareas que tengo registradas hasta el momento, con la finalidad de administrar mis tareas registradas.

#### Criterios de aceptación
1. Se muestra una tabla con los campos de id, nombre de la tarea y las acciones de editar y eliminar
2. Se deben mostrar todos los registros sin paginar la información
3. No importa el orden en que se muestren


> **Historia de usuario**
> Cómo Usuario quiero crear una nueva tarea, con la finalidad de agregar un elemento más a mi lista de tareas.

#### Criterios de aceptación
1. Se le muestra al usuario la pantalla modal para crear una nueva tarea
2. El nombre de una tarea debe tener un tamaño mínimo de 3 caracteres y un tamaño máximo de 50.
3. La descripción de una tarea debe tener un tamaño mínimo de 3 caracteres y un tamaño máximo de 100.
4. La fecha final deberá de ser mayor o igual a la fecha en que se está registrando la tarea.
5. Si todos los campos se han llenado correctamente, entonces se deberá de crear la tarea asociada a la lista de tareas del usuario que la creó y deberá de redireccionar al usuario a la pantalla de tareas mostrando los últimos registros ingresados.

> **Historia de usuario**
> Cómo Usuario, quiero cancelar la creación de una nueva tarea con la finalidad de retractarme y no crear una nueva tarea

#### Criterios de aceptación
1. El usuario es redirigido a la pantalla de tareas.vue y le muestra la lista registrada de tareas

> **Historia de usuario**
> Cómo Usuario quiero Editar una tarea, con la finalidad de modificarla y adecuar su contenido con uno nuevo

#### Criterios de aceptación

1. Se le muestra al usuario un modal con la tarea que el usuario quiere editar.
2. Se muestra el id de la tarea que se va a modificar en modo de lectura
3. Se deben de aplicar las mismas reglas de validación de campos que aplican para la creación de una nueva tarea.
4. Una vez que se edita la tarea, el sistema debe guardar una nueva versión de la tarea en la base de datos y redireccionar a la pantalla tareas.vue

> **Historia de usuario**
> Cómo Usuario, quiero cancelar la edición de una tarea con la finalidad de retractarme y no editar una nueva tarea

#### Criterios de aceptación

1. El usuario es redirigido a la pantalla de tareas.vue y le muestra la lista registrada de tareas sin guardar los cambios realizados en la edición de la tarea

> **Historia de usuario**
> Cómo Usuario quiero eliminar una tarea, con la finalidad de quitarla de mi lista de tareas.

#### Criterios de aceptación

1. Se le muestra al usuario la pantalla para confirmar la eliminación de una tarea
2. Una vez confirmada la acción de eliminación, el sistema deberá de borrar la tarea con el ID especificado y redireccionar al usuario a la pantalla de tareas.vue
