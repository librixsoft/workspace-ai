#!/bin/bash
set -e

echo "=== Building TambouUI Dashboard ==="
mvn clean compile package

echo ""
echo "=== Starting Dashboard ==="
java -jar target/tamboui-dashboard-1.0-SNAPSHOT.jar
