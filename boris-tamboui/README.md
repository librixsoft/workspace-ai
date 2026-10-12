# TamboUI Dashboard v2.0

A professional terminal dashboard built with **TamboUI** framework. Features real-time metrics, animated gauges, process monitoring, activity logging, and alert management.

## Features

- **Live Metrics** - CPU, Memory, Disk, Network with auto-refresh
- **Performance Gauges** - Animated progress bars with color-coded thresholds
- **Process Table** - Real-time process monitoring
- **Activity Log** - Dynamic log feed with level indicators
- **Alert Panel** - System status notifications
- **Navigation** - Sidebar with active state indicators

## Requirements

- **Java 21+**
- **Maven 3.8+**

## Installation

```bash
git clone https://github.com/yourusername/boris-tamboui.git
cd boris-tamboui
```

## Build & Run

### Quick Start
```bash
./run.sh
```

### Manual Build
```bash
mvn clean compile
mvn exec:java
```

### With JAR
```bash
mvn clean package
java -jar target/tamboui-dashboard-1.0.0.jar
```

## Controls

| Key | Action |
|-----|--------|
| `q` | Exit dashboard |

## Project Structure

```
boris-tamboui/
├── pom.xml
├── README.md
├── run.sh
└── src/main/java/com/boris/dashboard/
    ├── DashboardApp.java
    └── DashboardView.java
```

## Technologies

- **Java 21** - Language version
- **TamboUI 0.5.0** - Terminal UI framework
- **Maven** - Build tool
- **ANSI Escape Codes** - Terminal styling

## TamboUI Dependencies

```xml
<dependency>
    <groupId>dev.tamboui</groupId>
    <artifactId>tamboui-core</artifactId>
    <version>0.5.0</version>
</dependency>
<dependency>
    <groupId>dev.tamboui</groupId>
    <artifactId>tamboui-toolkit</artifactId>
    <version>0.5.0</version>
</dependency>
<dependency>
    <groupId>dev.tamboui</groupId>
    <artifactId>tamboui-tui</artifactId>
    <version>0.5.0</version>
</dependency>
<dependency>
    <groupId>dev.tamboui</groupId>
    <artifactId>tamboui-widgets</artifactId>
    <version>0.5.0</version>
</dependency>
<dependency>
    <groupId>dev.tamboui</groupId>
    <artifactId>tamboui-css</artifactId>
    <version>0.5.0</version>
</dependency>
<dependency>
    <groupId>dev.tamboui</groupId>
    <artifactId>tamboui-panama-backend</artifactId>
    <version>0.5.0</version>
</dependency>
<dependency>
    <groupId>dev.tamboui</groupId>
    <artifactId>tamboui-annotations</artifactId>
    <version>0.5.0</version>
</dependency>
```

## License

MIT
