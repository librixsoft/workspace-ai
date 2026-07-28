# Plan: Rediseño KDE/GNOME — Cards Blancas Glassmorphism sobre Fondo Degradado Azul

## Objetivo
Transformar la presentación HTML al estilo visual de **KDE Plasma / GNOME** con **cards blancas semitransparentes**, fondo degradado azul corporativo, diseño minimalista elegante y profesional. Paneles con opacidad blanca sobre gradiente azul profundo, tipografía limpia, sombras suaves.

---

## Tareas

### [x] 1. Nueva paleta azul corporativo + cards blancas glassmorphism

Reemplazar toda la paleta verde anterior por azules corporativos elegantes:

- **Azul profundo (fondo base)**: `#0F172A` → slate oscuro casi negro
- **Azul principal**: `#3B82F6` — azul vibrante corporativo
- **Azul medio**: `#2563EB` — para gradientes y acentos
- **Azul claro**: `#60A5FA` — hover y elementos secundarios
- **Azul cielo**: `#93C5FD`, `#BFDBFE` — acentos sutiles
- **Índigo**: `#4F46E5` — transición en gradiente
- **Cards blancas glassmorphism**: `rgba(255, 255, 255, 0.75)` base — blanco visible con transparencia
- **Cards hover**: `rgba(255, 255, 255, 0.88)` — más opaco al pasar el mouse
- **Cards border**: `1px solid rgba(255, 255, 255, 0.9)` — borde blanco casi sólido
- **Cards hover border**: `1px solid #FFFFFF` — borde blanco puro en hover
- **Texto primary**: `#FFFFFF`
- **Texto secondary**: `rgba(255, 255, 255, 0.7)`
- **Texto muted**: `rgba(255, 255, 255, 0.45)`

### [x] 2. Fondo de slide — degradado azul profundo corporativo

- Base universal: gradiente lineal diagonal desde arriba-izquierda
- Gradiente principal: `#0F172A → #1E3A5F → #3B82F6` (de oscuro a medio)
- Overlay sutil con patrón geométrico GNOME-like
- Esquema por slide con variaciones sutiles de posición/intensidad:

```
slide-1:  #0F172A → #1E40AF (intro, gradiente más intenso)
slide-2:  #0F172A → #1E3A5F (sutil, casi sólido)
slide-3:  #1E3A5F → #3B82F6 (medio, transición visible)
slide-4:  #0F172A → #1D4ED8 (variación índigo)
slide-5:  #0F172A → #1E3A5F (mínimo — header servicios)
slide-6:  #1E3A5F → #60A5FA (acentuado, azul más claro)
slide-7:  #0F172A → #1E40AF (medio-intenso)
slide-8:  #1E3A5F → #3B82F6 (vibrante)
slide-9:  #0F172A → #2563EB (CTA, azul fuerte)
```

### [x] 3. Cards blancas glassmorphism — estilo KDE/GNOME

El elemento visual principal de cada slide:

- **Background base**: `rgba(255, 255, 255, 0.75)` — blanca semitransparente
- **Backdrop-filter**: `blur(32px) saturate(1.1)` — blur pronunciado GNOME-style
- **Border**: `1px solid rgba(255, 255, 255, 0.9)` — borde blanco casi sólido
- **Border-radius**: `16px` (redondeado moderno KDE/GNOME)
- **Box-shadow**: `0 8px 32px rgba(0, 0, 0, 0.3)` — sombra suave oscura
- **Hover state**:
  - Background: `rgba(255, 255, 255, 0.88)`
  - Border: `1px solid #FFFFFF`
  - Box-shadow: `0 12px 40px rgba(0, 0, 0, 0.4)`, `0 0 0 1px rgba(59, 130, 246, 0.1)`
- **Layout interno**: padding generoso `32px`
- **Sin bordes brillantes ni glow** — elegante y limpio

### [x] 4. Blur orbs decorativos — azules corporativos

- Colores: `#3B82F6`, `#2563EB`, `#60A5FA`, `#4F46E5`, `#93C5FD`
- Opacidad baja: `opacity: 0.08` máximo
- Filter blur grande: `150px`+
- Posición estratégica detrás de las cards blancas
- Tamaño variable: `300px` a `700px` diámetro
- Animación sutil de flotación (movimiento 10-12px en 8s loop)

### [x] 5. Grid overlay estilo GNOME — casi invisible

- Líneas: `rgba(255, 255, 255, 0.03)`
- Espaciado: `64px` (más compacto que el anterior)
- Patrón tipo GNOME activities overview + patrón geométrico en overlays ::after
- Opacidad ultra-baja

### [x] 6. Partículas — azules tenues

- Color: `rgba(96, 165, 250, 0.2)` — azul tenue sin glow
- Reducir a 9 por slide
- Movimiento lento vertical (14-30s duración)

### [x] 7. Navegación — estilo KDE/GNOME minimalista

- **Dots**: fondo `rgba(255, 255, 255, 0.1)`, activo `#3B82F6` con ring blanco
- **Dot size**: `10px` diámetro, activo `14px` con box-shadow ring
- **Arrow buttons**: `background: rgba(255, 255, 255, 0.08)`, border `rgba(255, 255, 255, 0.12)`
- **Hover arrows**: `background: rgba(255, 255, 255, 0.14)`, color blanco
- **Fullscreen button**: glassmorphism — `rgba(255, 255, 255, 0.08)`, backdrop-blur `16px`
- **Counter**: `rgba(255, 255, 255, 0.15)`

### [x] 8. Logo mark, títulos, tags — azul corporativo

- **Logo mark**: fondo con gradiente `#2563EB → #3B82F6`, border `rgba(255, 255, 255, 0.15)`
- **Glow animation**: `box-shadow` azul tenue `rgba(59, 130, 246, 0.12)` — muy sutil
- **Títulos gradient text**: blanco → `#60A5FA` (azul claro)
- **Tags/labels**: color `#93C5FD` (azul cielo), tracking `.05em`, font-weight `500`
- **Dividers**: gradiente `#60A5FA → #3B82F6`
- **Número de slide**: `rgba(255, 255, 255, 0.15)`, font-size pequeño

### [x] 9. Tech tags, step numbers, elementos inline

- **Tech tag pills**: `background: rgba(59, 130, 246, 0.08)`, border `rgba(59, 130, 246, 0.15)` (adaptado para fondo blanco de cards)
- **Hover tech tags**: `background: rgba(59, 130, 246, 0.15)`, border `#3B82F6`
- **Step numbers**: gradiente `#2563EB → #60A5FA` sobre fondo circular — sin borde verde
- **Iconos/cards internas**: bordes redondeados `16px`, padding interno consistente

### [x] 10. Tipografía — corporativa y limpia

- **Font family**: Inter, system-ui (como GNOME/KDE usan)
- **Títulos**: font-weight `600`, letter-spacing `-0.02em`
- **Body**: font-weight `400`, line-height `1.7`
- **Labels/tags**: font-weight `500`, letter-spacing `.05em`, text-transform uppercase
- **Números de paso**: font-weight `600`

### [x] 11. Testing visual

- Verificado: NO queda verde anterior (`#2ECC71`, `#1ABC9C`, etc.) — grep confirma cero coincidencias
- Todas las cards blancas glassmorphism se notan sobre el fondo azul degradado (0.75 opacity)
- Contraste validado: texto #0F172A sobre cards blancas, texto blanco sobre fondos azules
- Responsive verificado en media query (@768px)

---

## Paleta Azul Corporativo + White Glassmorphism

```
#0F172A  — Slate oscuro profundo (fondo base)
#1E3A5F  — Azul marino medio-oscuro
#1E40AF  — Azul intenso (gradiente)
#2563EB  — Azul principal corporativo
#3B82F6  — Azul vibrante (acentos, activo)
#4F46E5  — Índigo (variación gradiente)
#60A5FA  — Azul claro (hover, acentos)
#93C5FD  — Azul cielo (tags, labels)
#BFDBFE  — Azul muy claro (bordes sutiles)
rgba(255,255,255,0.06-0.18) — Glass card opacities
rgba(255,255,255,0.12-0.35) — Glass card borders
#FFFFFF  — Text primary
```

## Notas de estilo KDE/GNOME corporativo

- Cards blancas semitransparentes como elemento central de cada slide
- Fondo degradado azul profundo: elegante y profesional
- Blur pronunciado en cards (32px) — estilo GNOME moderno
- Sombras suaves oscuras, sin glows intensos
- Bordes redondeados generosos (16px cards, 12px elementos internos)
- Tipografía limpia Inter/system-ui — como KDE Plasma y GNOME usan por defecto
- Espaciado generoso: padding amplio dentro de cada card
- Sin saturación excesiva — azules corporativos sobrios
- Glassmorphism elegante: blur alto + opacidad media blanca + borde sutil
- Partículas y orbs solo decorativos, nunca intrusivos
