# Tamboui Dashboard

Terminal dashboard built with **Tamboui 0.6.0** (Java + Maven).

## Features

- Live clock and refresh counter in the header
- Colored stat cards (CPU, MEM, DISK, NET)
- Process table with selection highlighting
- Bar chart for resource usage
- Footer with quit instructions
- Press `q` to exit

## Requirements

- Java 17+
- Maven 3+

## Build & Run

```bash
# Compile and run
./run.sh

# Or build fat jar
mvn package
java -jar target/tamboui-dashboard-1.0.0-jar-with-dependencies.jar
```

## Dependencies

| Artifact | Version |
|---|---|
| `dev.tamboui:tamboui-toolkit` | 0.6.0 |
| `dev.tamboui:tamboui-jline3-backend` | 0.6.0 |
| `org.slf4j:slf4j-simple` | 2.0.16 |

## License

MIT
