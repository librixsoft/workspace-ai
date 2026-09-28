#!/bin/bash

echo "Compilando proyecto Java Lanterna..."
mvn clean compile package

if [ $? -eq 0 ]; then
    echo "Compilacion exitosa. Ejecutando Dashboard..."
    mvn exec:java -Dexec.mainClass=com.example.Dashboard
else
    echo "Error en la compilacion."
    exit 1
fi