# Larry Cum - 8 Bit Adventure

Juego arcade estilo 8-bit hecho en HTML5 Canvas puro (sin dependencias), donde controlás a Larry, un espermatozoide que debe llegar al óvulo esquivando anticonceptivos.

## Qué hace el juego

- **Objetivo:** sobreviví esquivando obstáculos (preservativos, jeringas, píldoras, DIUs, diafragmas y papas fritas) que caen desde arriba. Cada obstáculo que pasa o destruyes suma puntos y progresa la barra de "LLEGADA AL OVULO".
- **Progresión por niveles:** al llenar la barra de progreso, Larry llega al óvulo y pasás al siguiente nivel, con spawn más rápido, más tipos de obstáculos y velocidades mayores.
- **Vidas:** empezás con 3 vidas. Los óvulos que caen de vez en cuando te dan una vida extra.
- **Power-ups:** escudo (absorbe un golpe y da invencibilidad), velocidad (multiplica el movimiento) y multi (disparo triple + bonus de score).
- **Disparo:** podés disparar un escudo de energía para destruir obstáculos y sumar puntos extra.
- **Efectos:** estrellas de fondo con scroll, explosiones de partículas, screen shake, flashes de daño, y elementos decorativos animados a los costados.
- **Sonido:** efectos de sonido 8-bit embebidos en base64 (sin archivos externos necesarios).
- **Responsive:** el canvas ocupa full width y full height de la ventana, se re-escala al redimensionar, y en móvil muestra botones táctiles de movimiento (tocar la pantalla dispara).

## Controles

| Acción | Teclado | Móvil |
|---|---|---|
| Moverse | A/D o flechas ←/→ | Botones ◀ ▶ |
| Disparar | ESPACIO o click | Tap en pantalla |
| Empezar / Reintentar / Siguiente nivel | Click en botón | Tap en botón |

## Cómo correrlo

Abrí `index.html` en el navegador, o servilo con cualquier servidor estático:

```
python3 -m http.server 8000
```

y entrá a `http://localhost:8000/larry-cum-8-bit/`.

## Archivos

- `index.html` — juego completo (HTML, CSS y JS en un solo archivo).
- `_snds.js` — sistema de sonido con WAVs embebidos en base64.
- `*.wav` — efectos de sonido originales.
