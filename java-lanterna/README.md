# Lanterna Dashboard

Dashboard de monitoreo de sistema en tiempo real con interfaz de terminal, desarrollado con **Boris AI**.

## Tecnologías

- **Java 11** + Maven
- **Lanterna 3.1.5** - Interfaz de usuario en terminal
- **Maven Shade Plugin** - Empaquetado como fat JAR

## Funcionalidades

- Monitoreo de CPU, memoria y disco
- Información de red y hostname
- Interfaz terminal colorizada y responsiva

## Build & Run

```bash
# Compilar y ejecutar
./run.sh

# O directamente con Maven
mvn clean package -q
java -jar target/lanterna-dashboard-1.0-SNAPSHOT.jar
```

## Sobre Boris AI

Este proyecto fue creado con **Boris AI**, un agente de desarrollo autónomo que demuestra el feature de **aprendizaje por repositorios** de GitHub.

Boris AI analiza el contexto del repositorio (estructura, dependencias, código existente) para generar código coherente, mantener convenciones del proyecto y tomar decisiones técnicas informadas. Este proyecto es una prueba de concepto de dicha capacidad: Boris leyó el repositorio, entendió el stack tecnológico (Java + Lanterna + Maven) y construyó una aplicación funcional completa.

### Características demostradas:
- Análisis automático de dependencias (pom.xml)
- Generación de código siguiendo convenciones del proyecto
- Creación de scripts de build y configuración
- Gestión de repositorio (commits, pushes, gitignore)
- Documentación contextual generada automáticamente

---

*Proyecto generado por Boris AI - Prueba de aprendizaje por repositorios*
