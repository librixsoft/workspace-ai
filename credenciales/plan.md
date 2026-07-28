# Plan: Rediseño Estilo KDE Linux — Presentación LibrixSoft

## Objetivo
Transformar la presentación HTML al estilo visual de **KDE Plasma / Breeze**: paleta de colores oficial, glassmorphism con opacidad marcada, tipografía clara, gradientes azul-cian, y superposición de capas.

---

## Tareas

### [x] 1. Actualizar paleta de colores KDE/Breeze
Cambiar toda la paleta del CSS a los colores oficiales Breeze:
- Primary blue: `#31CCE5` (cian KDE)
- Deep blue: `#3DAEEA`, `#2B7CD3`
- Accent cyan: `#49D2C5`
- Dark bg base: `#1E1E2E` / `#181825`
- Glass white: `rgba(65, 180, 229, ...)` para elementos translúcidos
- Text primary: `#FFFFFF`, secondary: `rgba(255,255,255,0.75)`
- Reemplazar todos los hex `#3A5F8A`, `#5B85B5`, `#7BA3D0`, `#294172` por equivalentes Breeze

### [x] 2. Rediseñar fondos de slides con gradientes KDE
- Cambiar gradients de cada slide a tonos KDE Plasma: base oscura `#16162A` → medio `#1E2A3A` → acento `#3DAEEA`
- Agregar capas de overlay semi-transparente (opacidad 0.05–0.15) para dar profundidad
- Mantener la estructura pero con colores KDE: cada slide debe tener un gradiente único pero coherente con el ecosistema Breeze

### [x] 3. Actualizar tipografía y jerarquía visual
- Reemplazar Overpass/Inter por **Breeze** fonts (o fallback a system sans-serif que KDE usa)
- Títulos: font-weight 800, tracking ligeramente negativo (-0.5px) para look KDE moderno
- Tags/labels: letter-spacing 3px, color cian #31CCE5, uppercase
- Body text: font-weight 400 (no 300), más legible
- Actualizar gradientes de texto en títulos a `#fff → #31CCE5`

### [x] 4. Rediseñar cards con glassmorphism KDE (opacidad marcada)
- Todos los componentes tipo card/step/chip/why-card/team-card:
  - Background: `rgba(65, 180, 229, 0.06)` base
  - Border: `1px solid rgba(49, 204, 229, 0.2)`
  - Backdrop-filter: `blur(12px)` (aumentar de 8px a 12)
  - Hover: background sube a `rgba(65, 180, 229, 0.12)`, border a `rgba(49, 204, 229, 0.35)`
- Agregar un glow sutil azul-cian en hover: box-shadow con rgba(49, 204, 229, 0.15)

### [x] 5. Rediseñar elementos decorativos (blur orbs, grid, particles)
- Blur orbs (::before): cambiar color a `#31CCE5` y `#3DAEEA`, aumentar opacidad de 0.12 a 0.18
- Grid overlay: líneas a color cian con rgba(49, 204, 229, 0.06) — más visible pero sutil
- Particles: color cambia de azul apagado a `rgba(49, 204, 229, 0.35)` — partículas cian brillantes

### [x] 6. Rediseñar navegación (dots, arrows, fullscreen button, counter)
- Dots: fondo rgba(49, 204, 229, 0.15), activo color `#31CCE5` con glow
- Arrow buttons: background rgba(49, 204, 229, 0.08), border rgba(49, 204, 229, 0.2)
- Fullscreen button: glassmorphism KDE, fondo rgba(65, 180, 229, 0.1), backdrop-blur
- Slide counter: color `rgba(49, 204, 229, 0.5)`

### [x] 7. Rediseñar logo mark y divider elements
- Logo mark (slide 1): gradiente KDE de fondo `#3DAEEA → #2B7CD3`, border cian con glow
- Glow animation: cambiar a color cian `rgba(49, 204, 229, ...)` 
- Dividers/speed lines: gradiente `#49D2C5 → #31CCE5` (cyan → azul KDE)

### [x] 8. Rediseñar tech tags y elementos inline
- Tech tag pills: fondo rgba(49, 204, 229, 0.1), border `rgba(49, 204, 229, 0.25)`
- Color de texto: `rgba(255, 255, 255, 0.8)`
- Step numbers (círculos): gradiente KDE `#2B7CD3 → #3DAEEA`, border cian

### [x] 9. Testing visual en todas las slides
- Abrir presentacion.html en navegador
- Verificar cada slide (9 total) con el nuevo estilo KDE
- Confirmar transiciones entre slides se ven coherentes
- Validar responsive en mobile (768px breakpoint)
- Probar navegación: flechas, dots, teclado, touch swipe

### [x] 10. Refinamiento final y optimización
- Revisar que no queden colores antiguos (3A5F8A, 5B85B5, 7BA3D0, 294172)
- Verificar contraste suficiente para accesibilidad
- Smooth transitions entre modos de hover en todos los elementos interactivos
- Test fullscreen mode

---

## Paleta KDE Breeze Referencia
```
#31CCE5  — Cyan KDE (primary accent)
#3DAEEA  — Blue Breeze
#2B7CD3  — Darker blue
#49D2C5  — Teal/cyan accent
#16162A  — Darkest bg
#1E1E2E  — Dark bg base  
#1E2A3A  — Mid dark (for gradients)
#FFFFFF  — Text white
```

## Notas de estilo KDE
- Glassmorphism con backdrop-filter: blur(12px+) en todos los paneles
- Gradientes azul→cian como signature visual
- Opacidades marcadas: cards semi-transparentes, no opacos
- Glow sutil cian en elementos interactivos (hover)
- Geometría limpia: bordes redondeados 12-16px
- Patrón de grid sutil como textura de fondo
