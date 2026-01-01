# Práctica 3 - Aplicación de Notas con RecyclerView

## 📱 Descripción
Aplicación Android moderna para gestionar notas con **RecyclerView**, Material Design 3 y múltiples funcionalidades avanzadas.

## 📸 Capturas de Pantalla

<p align="center">
  <img src="screenshots/app_screenshot.png" alt="App Screenshot" width="300"/>
</p>

*App de notas con sistema de prioridades, búsqueda y ordenamiento*

## ✨ Características Principales

### Funcionalidades Básicas
- ✅ **RecyclerView** con animaciones fluidas
- ✅ **CRUD completo**: Crear, leer, actualizar y eliminar notas
- ✅ **Material Design 3** con tema morado personalizado
- ✅ **Swipe to Delete** con confirmación
- ✅ **Long press to Edit** para editar notas
- ✅ **Empty State** cuando no hay notas

### Funcionalidades Avanzadas
- 🔍 **Búsqueda en tiempo real** por título o descripción
- 🎨 **Sistema de prioridades** con colores:
  - 🔴 Alta (rojo pastel)
  - 🟡 Media (amarillo pastel)
  - 🟢 Baja (verde pastel)
- 📊 **Ordenamiento múltiple**:
  - 📅 Por fecha (más recientes primero)
  - 🔤 Por título (A-Z)
  - ⭐ Por prioridad (alta → baja)
- 🔗 **Compartir notas** por WhatsApp u otras apps
- ⏰ **Timestamp automático** en cada nota
- 🎭 **Animaciones**: Slide, fade, scale para mejor UX

## 🎯 Cómo usar la aplicación

1. **Agregar nota**: Presiona el botón flotante **+** morado
2. **Editar nota**: Mantén presionado sobre una nota
3. **Eliminar nota**: 
   - Desliza hacia la izquierda
   - O presiona el botón "Eliminar"
4. **Buscar**: Toca el ícono de búsqueda en la barra superior
5. **Ordenar**: Toca el ícono de ordenar y elige criterio
6. **Compartir**: Presiona el botón de compartir en cada nota

## 🛠️ Tecnologías y Componentes

- **Lenguaje:** Kotlin
- **IDE:** Android Studio (AGP 8.11.2, Gradle 8.13)
- **Min SDK:** 33 | **Target SDK:** 36
- **Arquitectura:** MVVM simplificado con ViewHolder Pattern

### Librerías utilizadas:
- Material Components (Material Design 3)
- RecyclerView con ItemTouchHelper
- CardView para las tarjetas de notas
- ConstraintLayout y CoordinatorLayout

## 📂 Estructura del proyecto

```
app/src/main/
├── java/com/example/practica3/
│   ├── MainActivity.kt      # Actividad principal
│   ├── Note.kt             # Modelo con Priority enum
│   └── NoteAdapter.kt      # Adaptador con callbacks
├── res/
│   ├── layout/
│   │   ├── activity_main.xml              # Layout principal
│   │   ├── item_note.xml                  # Tarjeta de nota
│   │   ├── dialog_add_note_with_priority.xml
│   │   └── empty_state.xml                # Estado vacío
│   ├── drawable/
│   │   ├── popup_menu_background.xml      # Fondo del menú
│   │   └── [animaciones y recursos]
│   ├── anim/                              # Animaciones XML
│   ├── menu/
│   │   └── menu_main.xml                  # Menú de búsqueda/ordenar
│   └── values/
│       ├── colors.xml                     # Paleta de colores
│       ├── strings.xml                    # Textos externalizados
│       └── themes.xml                     # Tema Material 3
```

## 🎨 Paleta de Colores

- **Primario:** #6750A4 (Morado)
- **Primario Oscuro:** #4A3780
- **Acento:** #7B61FF
- **Prioridad Alta:** #FFCDD2 (Rojo pastel)
- **Prioridad Media:** #FFF9C4 (Amarillo pastel)
- **Prioridad Baja:** #C8E6C9 (Verde pastel)

## 🚀 Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/luisfernandoAngulo28/Practica3kotlin.git
```

2. Abre el proyecto en Android Studio

3. Sincroniza Gradle

4. Ejecuta la app en un emulador o dispositivo físico

## 📦 APK

Puedes descargar el APK directamente desde:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 👨‍💻 Autor

**Luis Fernando Angulo Heredia**

---

📅 Última actualización: Enero 2026
