#!/usr/bin/env bash
#
# run.sh — Compila, empaqueta y ejecuta el dashboard TUI4J (Java 21 + Maven).
#
set -euo pipefail

# Ruta base del proyecto (donde vive este script)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

JAVA_HOME_VAL="${JAVA_HOME:-}"
if [ -n "$JAVA_HOME_VAL" ] && [ ! -x "$JAVA_HOME_VAL/bin/java" ]; then
  echo "ERROR: JAVA_HOME apunta a una ruta sin java ejecutable: $JAVA_HOME_VAL" >&2
  exit 1
fi

MVN_CMD="${MVN_CMD:-mvn}"
JAVA_CMD="${JAVA_CMD:-java}"

ARTIFACT="bolt-tui4j-dashboard-1.0.0-jar-with-dependencies.jar"

echo "[1/3] Compilando fuentes..."
"$MVN_CMD" -q compile

echo "[2/3] Empaquetando uber-jar..."
"$MVN_CMD" -q package

echo "[3/3] Levantando la app..."
exec "$JAVA_CMD" -jar "target/$ARTIFACT"
