# Plan de Rediseño — Reproductor de Música Lanterna

## 1. Estado Actual

El proyecto actual (`src/main/java/com/dashboard/`) es un dashboard terminal con KPIs, gráficos, tablas y paneles de estado. No existe un reproductor de música. Se debe agregar como nuevo módulo o vista dentro de la misma app Lanterna.

## 2. Objetivos de Diseño

1. **Interfaz inmersiva**: panel central con arte del álbum, info de la canción y controles de reproducción.
2. **Barra de progreso visual**: con tiempo transcurrido, total y posición actual.
3. **Controles de playback**: play/pause, anterior, siguiente, shuffle, repeat con estados visuales.
4. **Lista de cola/playlist**: con artista, álbum y duración, con fila actual resaltada.
5. **Control de volumen**: barra vertical u horizontal con nivel.
6. **Status bar**: tiempo total, modo de reproducción, conexión, volumen.
7. **Sistema de diseño centralizado**: colores para estados (play, pause, error, idle).

## 3. Diagrama ASCII del Reproductor

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  ◆  LANTERNA MUSIC PLAYER                                                   │
│  [ Playlist ] [ Library ] [ Search ] [ Settings ]                           │  [ 12:34 ]  [ VOL: 85% ]
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  ██████████████████████████████████████████████████████████████████ │   │
│  │  ██  ALBUM ART                                                    ██ │   │
│  │  ██  ──────────────────────────────────────────────────────────── ██ │   │
│  │  ██                                                              ██ │   │
│  │  ██  "Midnight Drive"                                             ██ │   │
│  │  ██  The Weeknd                                                   ██ │   │
│  │  ██  After Hours                                                  ██ │   │
│  │  ██                                                              ██ │   │
│  │  ██  ┌─────────────────────────────────────────────────────────┐ ██ │   │
│  │  ██  │  0:45 ────────────────────────────────────────── 3:20   │ ██ │   │
│  │  ██  │  ●───────────────────────────────────────────────────── │ ██ │   │
│  │  ██  └─────────────────────────────────────────────────────────┘ ██ │   │
│  │  ██                                                              ██ │   │
│  │  ██  [ ▶ ]  [ ⏮ ]  [ ⏭ ]  [ ⏮ ]  [ 🔁 ]  [ 🔁 ]                    ██ │   │
│  │  ██  Play  Prev  Next  Shuffle  Repeat                           ██ │   │
│  │  ██                                                              ██ │   │
│  │  ██  ┌─────────────────────────────────────────────────────────┐ ██ │   │
│  │  ██  │  VOLUME                                                  │ ██ │   │
│  │  ██  │  ██████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ │ ██ │   │
│  │  ██  │  85%                                                    │ ██ │   │
│  │  ██  └─────────────────────────────────────────────────────────┘ ██ │   │
│  │  ██                                                              ██ │   │
│  │  └─────────────────────────────────────────────────────────────────┘ ██ │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  PLAYLIST / QUEUE                                                   │   │
│  │  ┌────────────────────────────────────────────────────────────────┐ │   │
│  │  │  #  Title                                                  Artist   │ │   │
│  │  ├────────────────────────────────────────────────────────────────┤ │   │
│  │  │  ▶ 1  Midnight Drive                                         The Weeknd   │ │   │
│  │  │  │  After Hours · 3:20                                      │ │   │
│  │  ├────────────────────────────────────────────────────────────────┤ │   │
│  │  │  2  Blinding Lights                                          The Weeknd   │ │   │
│  │  │  │  After Hours · 3:20                                      │ │   │
│  │  ├────────────────────────────────────────────────────────────────┤ │   │
│  │  │  3  Drive                                                    Miley Cyrus   │ │   │
│  │  │  │  End of Summer · 3:45                                      │ │   │
│  │  ├────────────────────────────────────────────────────────────────┤ │   │
│  │  │  4  Someone Like You                                         Adele       │ │   │
│  │  │  │  25 · 4:15                                               │ │   │
│  │  └────────────────────────────────────────────────────────────────┘ │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  CONTROLS                                                           │   │
│  │  [ Playlist ] [ Library ] [ Now Playing ] [ Devices ]               │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
│  STATUS  Playing ●  Now: "Midnight Drive"  0:45 / 3:20  Shuffle: OFF  │
└─────────────────────────────────────────────────────────────────────────────┘
```

## 4. Elementos a Implementar

### 4.1 Panel de Reproducción Central (Now Playing)
- **Arte del álbum**: panel cuadrado con imagen simulada usando caracteres bloque.
- **Información**: título, artista, álbum.
- **Barra de progreso**: línea con posición actual (●) y tiempos (actual/total).
- **Controles de playback**: fila de botones (Play/Pause, Prev, Next, Shuffle, Repeat).
- **Volumen**: barra horizontal con nivel y porcentaje.

### 4.2 Lista de Playlist/Queue
- Tabla con columnas: #, Título, Artista, Álbum, Duración.
- Fila actual resaltada con borde y color.
- Scroll simulado si hay más canciones que filas visibles.

### 4.3 Controles de Playback (Botones)
- `Play/Pause`: toggle con icono y color diferente.
- `Prev/Next`: flechas con estado deshabilitado en bordes.
- `Shuffle`: icono con estado activado/desactivado.
- `Repeat`: icono con estados (off/one/all).

### 4.4 Barra de Estado (Status Bar)
- Estado de reproducción (Playing/Paused/Stopped).
- Canción actual con tiempo.
- Modo shuffle y repeat.
- Volumen.
- Reloj.

### 4.5 Sistema de Diseño Centralizado
- Colores: background oscuro, surface, border, text primary/secondary.
- Estados: play (verde), pause (amarillo), stop (rojo), idle (gris).
- Tipografía monoespaciada.

## 5. Nuevas Clases a Crear

| Clase | Descripción |
|-------|-------------|
| `MusicPlayer` | Clase principal que orquesta el reproductor y su UI. |
| `NowPlayingPanel` | Panel central con arte, info, progreso y controles. |
| `ProgressBar` | Barra de progreso de reproducción con posición actual. |
| `PlaybackButton` | Botón de playback con estados visuales (play/pause). |
| `VolumeControl` | Control de volumen con barra y porcentaje. |
| `PlaylistPanel` | Lista de canciones con fila actual resaltada. |
| `SongItem` | Item individual de la playlist. |
| `MusicPlayerTheme` | Tema de colores para el reproductor. |

## 6. Modificaciones a Clases Existentes

| Clase | Cambio |
|-------|--------|
| `DashboardApp` | Agregar vista de reproductor o integrar como panel. |
| `BaseButton` (si se crea) | Reusar para controles de playback. |
| `DesignSystem` (si se crea) | Extender con colores de reproductor. |

## 7. Fases de Implementación

### Fase 1 — Sistema de Diseño y Base
- Crear `MusicPlayerTheme` con paleta de colores.
- Crear `ProgressBar` base.
- Crear `PlaybackButton` con estados.

### Fase 2 — Panel de Reproducción
- Crear `NowPlayingPanel` con arte del álbum.
- Integrar barra de progreso y controles.
- Agregar control de volumen.

### Fase 3 — Playlist
- Crear `PlaylistPanel` con tabla de canciones.
- Implementar resaltado de fila actual.
- Agregar scroll simulado.

### Fase 4 — Integración y Control
- Crear `MusicPlayer` para orquestar reproducción.
- Integrar en `DashboardApp` como nueva vista.
- Lógica de playback (play, pause, prev, next, shuffle, repeat).

### Fase 5 — Pulido
- Ajustar colores y espaciado.
- Verificar renderizado en terminal.
- Probar interactividad.

## 8. Comandos de Build y Run

```bash
# Build
mvn clean package

# Run
java -jar target/lanterna-dashboard-1.0-SNAPSHOT.jar
```

## 9. Métricas de Éxito

- [ ] Panel central con arte del álbum y info de canción.
- [ ] Barra de progreso con posición actual y tiempos.
- [ ] Controles de playback (play/pause, prev, next, shuffle, repeat).
- [ ] Playlist con fila actual resaltada.
- [ ] Control de volumen visual.
- [ ] Status bar con estado de reproducción.
- [ ] Sistema de colores centralizado.
- [ ] App compila y ejecuta sin errores.
