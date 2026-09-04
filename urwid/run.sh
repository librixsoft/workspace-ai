#!/bin/bash

# Simple run script for Urwid dashboard
echo "Starting Urwid Dashboard..."

# Check if Python is available
if ! command -v python3 &> /dev/null; then
    echo "Error: Python 3 is required but not found"
    exit 1
fi

# Check if urwid is installed
if ! python3 -c "import urwid" &> /dev/null; then
    echo "Installing Urwid library..."
    pip install urwid || pip3 install urwid
fi

echo "Running dashboard application..."
python3 urwid_dashboard.py