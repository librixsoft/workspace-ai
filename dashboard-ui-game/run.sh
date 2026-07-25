#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# 1. Activate virtual environment if it exists, otherwise create it
if [ -d ".venv" ]; then
    echo "✓ Virtualenv found — activating..."
else
    echo "Creating virtualenv..."
    python3 -m venv .venv
fi

source .venv/bin/activate

# 2. Install dependencies from requirements.txt (upgrade textual if missing)
echo "Installing/updating dependencies..."
pip install -q -r requirements.txt --upgrade

# 3. Load .env variables (for validation)
if [ -f ".env" ]; then
    export $(grep -v '^#' .env | xargs)
    echo "✓ Environment loaded from .env"
else
    echo "⚠ No .env file found — continuing without environment variables."
fi

# 4. Verify entry point exists
if [ ! -f "src/app.py" ]; then
    echo "✗ src/app.py not found — please create the application first."
    exit 1
fi

echo ""
echo "═══════════════════════════════════════"
APP_NAME="${APP_NAME:-Dashboard AI}"
echo "  Launching ${APP_NAME} ..."
echo "═══════════════════════════════════════"

# Run textual via python module (avoids PATH issues with CLI binary)
exec python -m textual run src/app.py "$@"
