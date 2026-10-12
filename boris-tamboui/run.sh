#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

echo "=== TamboUI Dashboard ==="
echo ""

# Limpiar build anterior
echo "[1/3] Limpiando build..."
mvn clean -q

# Compilar
echo "[2/3] Compilando..."
mvn compile -q

# Ejecutar
echo "[3/3] Ejecutando..."
echo ""
mvn exec:java -Dexec.mainClass="com.boris.dashboard.DashboardApp" -q
