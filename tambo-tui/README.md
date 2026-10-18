# TambouUI Dashboard

> **Fully created with Boris AI** — Intelligent autonomous AI software engineer

A terminal-based system dashboard built with **TambouUI**, a modern Java TUI framework. Displays real-time system metrics, performance charts, and log entries in a clean, interactive terminal interface.

## 🚀 Features

- **Multi-tab navigation**: Switch between Overview, Metrics, and Logs views
- **System monitoring panels**: CPU, Memory, Disk, and Network stats with color-coded indicators
- **Sparkline charts**: Visual representation of system load and performance metrics
- **Keyboard controls**: Navigate tabs with arrow keys, quit with `Q`
- **Responsive layout**: Flexible column-based UI that adapts to content

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | TambouUI (TUI toolkit) |
| Build Tool | Maven |
| Backend | JLine3 |

## 📦 Dependencies

- **tamboui-toolkit** — Core TUI toolkit (layout, elements, widgets)
- **tamboui-jline3-backend** — JLine3 terminal backend integration

## 📋 Prerequisites

- **Java 17+** (JDK or JRE)
- **Maven 3.8+**

## 🔧 Build & Run

### Option 1: Using the provided script

```bash
chmod +x run.sh
./run.sh
```

### Option 2: Manual build

```bash
mvn clean package
java -jar target/tamboui-dashboard-1.0-SNAPSHOT.jar
```

## ⌨️ Keyboard Controls

| Key | Action |
|-----|--------|
| `←` | Switch to previous tab |
| `→` | Switch to next tab |
| `Q` | Quit application |

## 📁 Project Structure

```
tambo-tui/
├── pom.xml                          # Maven project configuration
├── run.sh                           # Build and run script
├── README.md                        # This file
└── src/
    └── main/
        └── java/
            └── dev/tambo/dashboard/
                └── DashboardApp.java  # Main application class
```

## 🖥️ Screens

The dashboard presents three tabs:

1. **Overview** — System resource panels (CPU, Memory, Disk, Network) with sparkline chart and activity status
2. **Metrics** — Performance metrics (Requests/sec, Response time, Error rate, Uptime) with activity sparkline
3. **Logs** — Recent system log entries color-coded by severity (INFO, WARN, ERROR)

## 🏗️ Build Configuration

- **Shade Plugin**: Bundles all dependencies into a single executable JAR
- **Manifest**: Points to `dev.tambo.dashboard.DashboardApp` as the entry point
- **Repository**: Snapshots from Sonatype OSSRH

## 🤖 About Boris AI

This project was **fully created with Boris AI**, an autonomous AI software engineer that:

- Analyzes requirements and plans architecture
- Writes, compiles, and debugs code iteratively
- Resolves framework API conflicts and build issues
- Generates documentation and deployment scripts

Boris operates directly in your local environment, using tools for file manipulation, Maven builds, symbol queries, and web search to deliver production-ready code.

## 📄 License

MIT
