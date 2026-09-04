# Dashboard con Python + Textual

## Fase 1: Configuración del proyecto

- [x] **Tarea 1.1** — Crear estructura de directorios (src/, data/, tests/) y archivo `pyproject.toml` o `requirements.txt` con las dependencias (`textual`, `python-dotenv`).
- [x] **Tarea 1.2** — Crear archivo `.env` con variables básicas: `APP_NAME`, `APP_THEME`, `DATA_SOURCE_PATH`.

- [x] **Tarea 1.3** — Crear `run.sh` que instale las dependencias (pip install -r requirements.txt o poetry install) y levante la app con `textual run src/app.py`, leyendo variables desde `.env`.

## Fase 2: Base de la aplicación Textual

- [x] **Tarea 2.1** — Implementar clase principal `DashboardApp(App)` en `src/app.py` con configuración del layout, tema (lee de `.env`) y barra de navegación básica.
- [x] **Tarea 2.2** — Crear widget de panel lateral (sidebar) con menú de secciones y widget de área de contenido principal vacía como placeholder.

## Fase 3: Widgets y visualización de datos

- [x] **Tarea 3.1** — Implementar widgets reutilizables: tarjetas KPI (Key Performance Indicators), tabla de datos y gráfico de barras básico usando Textual `DataClass` / `Static`.
- [x] **Tarea 3.2** — Conectar lectura de datos desde archivo CSV/JSON definido en `.env`, con manejo de errores (archivo no encontrado, formato inválido).

## Fase 4: Integración y pulido final

- [x] **Tarea 4.1** — Integrar todos los widgets en las secciones del dashboard, agregar navegación entre pestañas/secciones y cargar datos dinámicamente al cambiar de sección.
- [x] **Tarea 4.2** — Agregar estilos finales: colores consistentes, responsive básico para diferentes tamaños de terminal, mensaje de bienvenida con `APP_NAME` desde `.env`.
