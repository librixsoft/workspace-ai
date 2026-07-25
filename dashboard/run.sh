#!/bin/bash
set -e

# Load .env (now inside dashboard/)
if [ -f ".env" ]; then set -a && source .env && set +a; fi

echo "🚀 Starting dashboard app..."

# Create virtual environment if it doesn't exist
if [ ! -d "venv" ]; then
    python3 -m venv venv
fi

source venv/bin/activate

# Install dependencies
pip install -q textual rich python-dotenv

# Launch the app with Textual dev server (hot-reload)
python -m textual run dashboard.app:DashboardApp
