# Práctica 3 - Aplicación de Notas con RecyclerView

## 📱 Descripción
Aplicación Android sencilla que muestra una lista de notas utilizando un **RecyclerView**.
La app permite agregar y eliminar notas de manera dinámica.

## ✅ Requisitos mínimos implementados

La aplicación cumple con lo siguiente:

1. ✔️ **RecyclerView** para mostrar la lista de notas
2. ✔️ **Adapter** personalizado para conectar los datos con la vista
3. ✔️ **Layout XML** para cada ítem de la lista (`item_note.xml`)
4. ✔️ Muestra **título** y **descripción** de cada nota
5. ✔️ Botón para **agregar** una nueva nota

## 🎯 Funcionamiento

- Al iniciar la app, se muestra una lista con **3 notas de ejemplo**
- Al presionar el botón **"Agregar Nota"**, se añade una nueva nota a la lista
- Al presionar **"Eliminar"** en un ítem, la nota desaparece de la lista
- La lista se actualiza **dinámicamente** sin reiniciar la aplicación

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Componentes:** RecyclerView, CardView
- **Patrón:** ViewHolder Pattern

## 📂 Estructura del proyecto

```
app/src/main/java/com/example/practica3/
├── MainActivity.kt      # Actividad principal
├── Note.kt             # Modelo de datos
└── NoteAdapter.kt      # Adaptador del RecyclerView

app/src/main/res/layout/
├── activity_main.xml   # Layout principal
└── item_note.xml       # Layout de cada nota
```

## 👨‍💻 Autor

Luis Fernando Angulo Heredia
