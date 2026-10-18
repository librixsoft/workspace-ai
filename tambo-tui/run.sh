#!/bin/bash
set -e

echo "=== Building tambo-tui dashboard-demo ==="
mvn clean compile package -q

echo "=== Starting application ==="
java -jar target/dashboard-demo-1.0.0.jar
