#!/bin/bash

# Navigate to the project directory
cd "$(dirname "$0")"

echo "=== Compilando el proyecto Maven ==="
mvn clean compile -q

echo "=== Empaquetando el JAR ==="
mvn package -q

echo "=== Ejecutando la aplicación ==="
java -cp "target/classes:$(cat classpath.txt)" -ea com.example.HelloWorld