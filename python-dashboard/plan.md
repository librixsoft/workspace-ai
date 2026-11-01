# Plan — Dashboard TUI (Python/rich)

## Objetivo
Convertir el dashboard monobloque actual en una interfaz modular con **pestañas (tabs)**, reorganizar la jerarquía visual y mejorar la calidad general. Todo sigue siendo un TUI de terminal (sin dependencias nuevas).

## Alcance
- [ ] Extraer cada sección del dashboard en su propio componente/panel independiente.
- [ ] Añadir una barra de navegación lateral con las pestañas y cambio de vista con teclado (1-6 / flechas).
- [ ] Reorganizar el layout: Header fijo arriba, sidebar de pestañas a la izquierda, contenido principal a la derecha, footer fijo abajo.
- [ ] Mejorar paleta, tipografía, paneles, sparklines, barras y donut.
- [ ] Añadir refresco automático configurable y refresco manual (R).
- [ ] Controles de teclado claros: Q salir, R refrescar, 1-6 cambiar pestaña, flechas.
- [ ] Mantener `run.sh` y `requirements.txt` sin cambios (solo `rich`).

## Pestañas (tabs) previstas
1. **Resumen / KPIs** — tarjetas de ventas totales, promo diario, categorías, clientes con sparkline y delta.
2. **Gráficos** — barras de ventas por categoría + donut de distribución.
3. **Transacciones** — tabla de transacciones recientes con zebra striping.
4. **Progreso** — barra de avance vs. meta mensual + tendencia.
5. **Actividad / Alertas** — feed de última operación y alertas.
6. **Configuración** — intervalo de refresco, modo simulado, notas del sistema.

## Mejoras visuales
- Header con título, hora en vivo, estado de conexión del feed y indicador de pestaña activa.
- Paleta consistente (GitHub dark) con acentos por sección.
- Paneles con bordes redondeados, títulos centrados/alineados y padding uniforme.
- KPIs con icono, valor grande, label, delta con color (verde/rojo) y sparkline.
- Tabla transacciones con cabezal destacada y filas alternadas.
- Barra de progreso con porcentaje, meta/actual y tramo de tendencia.
- Footer con hints de teclado y estado.

## Arquitectura técnica
- `tui_dashboard.py`: única entrada, console `rich`, loop principal con `select` no bloqueante.
- Cada pestaña = función que devuelve un `Panel`/`Table`/`Columns`.
- `draw()` renderiza header + sidebar + contenido activo + footer.
- Estado: `tab_index` (0-based), `refresh_interval` (s), `last_refresh`.
- Datos: simulación con jitter suave, persistente entre ciclos.

## Testeo
- Ejecutar `./run.sh` y verificar que se renderice sin errores.
- Cambiar de pestaña con teclas 1-6 y confirmar.
- Verificar refresco con R y salida con Q.
- Confirmar que no hay excepciones en loop prolongado (ej. 30 s).
