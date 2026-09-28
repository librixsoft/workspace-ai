# Java Lanterna

Un entorno de desarrollo para Lanterna, una biblioteca de creación de interfaces de usuario en consola para Java.

## Requisitos

- Java JDK 8 o superior
- Maven 3.x

## Estructura del Proyecto

```
java-lanterna/
├── src/
│   └── main
│       └── java
│           └── Dashboard.java
├── pom.xml
└── README.md
```

## Configuración

Este proyecto está configurado con Lanterna XS (Extra Small) - una versión optimizada para entornos con recursos limitados.

## Uso Básico

```bash
# Compilar el proyecto
mvn compile

# Ejecutar el Dashboard
mvn exec:java -Dexec.mainClass=com.example.Dashboard
```

## Dashboard

La aplicación incluye un dashboard visual con:
- Información del usuario y estado
- Gráficas de recursos (CPU, Memoria, Disco)
- Lista de notificaciones
- Mensajes de ayuda

## Características del Dashboard

- Interfaz con bordes y colores ANSI
- Mensajes de usuario y estado
- Indicadores de recursos visuales
- Lista de notificaciones
- Control con teclas (Q para salir)

## Notas

- Lanterna XS proporciona una interfaz de usuario de consola ligera
- Ideal para aplicaciones que necesitan UI sin dependencias pesadas
- Incluye soporte para colores ANSI y caracteres Unicode

## Licencia

MIT