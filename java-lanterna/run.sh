#!/usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "========================================="
echo " Lanterna Dashboard - Build & Run"
echo "========================================="

echo ""
echo "[1/3] Limpieza..."
mvn clean -q

echo "[2/3] Compilando y empaquetando (fat JAR)..."
mvn package -q

echo ""
echo "[3/3] Levantando la aplicación..."
echo ""
java -jar target/lanterna-dashboard-1.0-SNAPSHOT.jar
