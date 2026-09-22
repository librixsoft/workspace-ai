# Boris Multi-Agent Framework

**Agente autónomo de desarrollo local**

## 🚀 Qué es

Boris es un agente de software autónomo que ejecuta tareas de desarrollo, investigación e integración directamente en tu entorno local.

## 🏗️ Arquitectura

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  Usuario        │────▶│  Orquestador     │────▶│  Subagentes     │
│  (Comandos)     │     │  (Boris)         │     │  (Ejecución)    │
└─────────────────┘     └──────────────────┘     └─────────────────┘
```

## 🎯 Características principales

- **Autonomía total**: Ejecuta tareas sin supervisión constante
- **Multi-agent**: Delega tareas a subagentes especializados
- **Ejecución paralela**: Trabaja múltiples tareas simultáneamente
- **Edición de archivos**: Modifica contenido localmente
- **Generación de contenido**: Crea PDFs, Office documents, código
- **Investigación web**: Busca información en tiempo real

## ⚡ Uso rápido

```bash
# Crear documento
create_office_document

# Ejecutar múltiples tareas en paralelo
run_parallel_tasks

# Editar archivo
apply_edit /ruta/archivo.txt "texto viejo" "texto nuevo"

# Generar PDF
generate_pdf

# Buscar en web
web_search "query"

# Leer archivo
read_file
```

## ⚙️ Configuración

| Parámetro | Descripción |
|-----------|-------------|
| `documentType` | word, powerpoint, excel |
| `outputPath` | Ruta de salida |
| `title` | Título del documento |
| `content` | Contenido (texto plano o tablas) |
| `customization` | JSON con estilos |

## 📚 Recursos técnicos

- [Documentación de Python](https://docs.python.org/)
- [Playwright](https://playwright.dev/)
- [Markdown](https://commonmark.org/)

---

Creado con **Boris-ai** y **Qwen 4b**

*Generado por Boris AI Agent*
