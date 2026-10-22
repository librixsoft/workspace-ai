# Nemotron Bolt TUI4J Dashboard

Dashboard de terminal (TUI) construido con **Java 21** y **TUI4J 0.3.3** (arquitectura **bubbletea**: Model/View/Program), gestionado por **Maven**.

## Estructura del proyecto

```
nemotron-bolt-tui4j/
├── pom.xml                # Maven: Java 21, tui4j 0.3.3, maven-assembly-plugin
├── .gitignore             # target/, *.class, *.jar, .idea/, .DS_Store
├── run.sh                 # compila, empaqueta y ejecuta la app
└── src/main/java/com/nemotron/bolttui/DashboardApp.java
```

## Características del dashboard

- **Arquitectura bubbletea**: implementa `Model` con `init()` / `update(Message)` / `view()`. TUI4J gestiona el bucle de eventos y el repintado.
- **Layout de 3 zonas**:
  - Barra lateral de navegación (5 secciones).
  - Panel central de métricas en tiempo real.
  - Barra de estado inferior.
- **Métricas simuladas en tiempo real** (actualizadas cada 1 s):
  - CPU, Memoria, Disco (porcentaje + estado OK/ALTO).
  - Red RX / TX (MB/s).
  - Tareas activas y peticiones totales.
  - Uptime (`HH:MM:SS`).
- **Controles**: `h/j/k/l` o `1-5` navegan entre secciones; `q` sale.
- **Colores ANSI** para resaltar valores (verde OK, rojo ALTO).

## Cómo ejecutar

```bash
cd nemotron-bolt-tui4j
./run.sh
```

`run.sh` ejecuta:
1. `mvn compile`
2. `mvn package` (genera `target/bolt-tui4j-dashboard-1.0.0-jar-with-dependencies.jar`)
3. `java -jar target/bolt-tui4j-dashboard-1.0.0-jar-with-dependencies.jar`

Requisitos: Java 21 y Maven instalados.

## Nota de empaquetado

El uber-jar generado por `maven-assembly-plugin` incluye las clases de JLine pero no resuelve la clase transitiva `org.jline.terminal.TerminalBuilder$SystemOutput` en tiempo de ejecución. Si al ejecutar aparece un `NoClassDefFoundError` de JLine, reconfigure el plugin de empaquetado a `maven-shade-plugin` (o ejecute con el classpath de dependencias de Maven).

## Build

```bash
mvn package
```
