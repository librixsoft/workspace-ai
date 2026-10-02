# LibrixSoft Dossier — Guía de Generación de PDF

Este documento contiene las instrucciones necesarias para regenerar de forma idéntica el archivo PDF vectorial de 1 sola página continua (`brochure_single_page.pdf`) a partir del archivo HTML `librixsoft_brochure_v2 2.html`.

---

## 🚀 Cómo Generar el PDF

### Opción 1: Ejecutar el script automatizado (Recomendado)

En la terminal, ejecuta el siguiente comando:

```bash
node generate_pdf.js
```

Este script compila automáticamente el archivo HTML a `brochure_single_page.pdf` en 1 sola página continua sin desbordes.

---

### Opción 2: Comando directo con Google Chrome CLI

Si prefieres ejecutar el comando directamente desde la terminal de macOS:

```bash
"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" \
  --headless \
  --disable-gpu \
  --no-pdf-header-footer \
  --print-to-pdf="/Users/lastprophet/Documents/workspaces-ai/Librix/brochure_single_page.pdf" \
  "file:///Users/lastprophet/Documents/workspaces-ai/Librix/librixsoft_brochure_v2 2.html"
```

---

### Opción 3: Exportación manual desde el navegador (Cmd + P)

1. Abre `librixsoft_brochure_v2 2.html` en Google Chrome o Microsoft Edge.
2. Presiona <kbd>Cmd</kbd> + <kbd>P</kbd> (Imprimir).
3. Configura los siguientes ajustes:
   - **Destino:** Guardar como PDF.
   - **Márgenes:** Ninguno (*None*).
   - **Gráficos de fondo:** **Activado** *(imprescindible para mantener el fondo negro y colores)*.
   - **Encabezados y pies de página:** **Desactivado**.
4. Guarda el archivo.

---

## 🛠️ Notas sobre la Configuración CSS

El archivo `librixsoft_brochure_v2 2.html` incluye reglas de impresión integradas:

- **Página continua:** La regla `@page` define las dimensiones exactas (`210mm x 1675mm`) para que el PDF termine justo al finalizar el footer en una sola página continua sin cortes ni espacios sobrantes.
- **Visibilidad y Colores:** Las reglas `@media print` desactivan capas flotantes desbordantes (`.bg-blur`), fuerzan la visibilidad de elementos interactivos (`opacity: 1`) y aseguran el renderizado exacto de los colores oscuros (`-webkit-print-color-adjust: exact`).
