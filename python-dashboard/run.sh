#!/usr/bin/env bash
# run.sh — lanza el dashboard TUI de Python (terminal UI)

set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VENV="$PROJECT_DIR/.venv"

echo "=== Dashboard TUI (Python) ==="
echo "Directorio: $PROJECT_DIR"

# Activar el entorno virtual si existe
if [ -d "$VENV" ]; then
    echo "Activando entorno virtual: $VENV"
    # shellcheck disable=SC1090
    source "$VENV/bin/activate"
else
    echo "ADVERTENCIA: no se encontró .venv. Usando Python del sistema."
fi

# Asegurar dependencia 'rich'
if [ -f "$PROJECT_DIR/requirements.txt" ]; then
    echo "Verificando dependencias..."
    pip install -r "$PROJECT_DIR/requirements.txt" --quiet
fi

# Lanzar el dashboard TUI
echo "Iniciando dashboard TUI..."
exec python "$PROJECT_DIR/tui_dashboard.py"
