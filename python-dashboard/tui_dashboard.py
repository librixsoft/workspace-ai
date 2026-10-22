#!/usr/bin/env python3
"""
Dashboard TUI (Terminal UI) — prototipo visual sin funcionalidad.
Maqueta visual que se mantiene abierta refrescando hasta Ctrl+C.
"""

import time
from rich.panel import Panel
from rich.columns import Columns
from rich.table import Table
from rich.text import Text
from rich.console import Console

console = Console(width=120)


def header():
    return [
        Panel(
            Text("DASHBOARD PYTHON", style="bold white on #1f3864", justify="center"),
            style="#1f3864",
            padding=(1, 4),
        ),
        Panel(
            Text(
                "Prototipo visual — sin backend, sin datos reales",
                style="italic #666666",
                justify="center",
            ),
            style="#2e405a",
            padding=(0, 4),
        ),
    ]


def kpi_row():
    kpis = [
        ("💰", "VENTAS TOTALES", "$450,000", "#1f77b4"),
        ("📈", "PROMÉDIO DIARIO", "$5,000", "#ff7f0e"),
        ("🏷", "CATEGORÍAS", "5", "#2ca02c"),
        ("👥", "CLIENTES", "1,240", "#d62728"),
    ]
    panels = []
    for icon, label, value, color in kpis:
        panels.append(
            Panel(
                Text(f"{icon}\n{value}\n{label}", style="bold", justify="center"),
                style=color,
                border_style=color,
                padding=(1, 2),
                width=27,
            )
        )
    return Columns(panels, equal=True, expand=True)


def charts_row():
    cats = ["Electrónica", "Ropa", "Hogar", "Deportes", "Libros"]
    vals = [12000, 8500, 6200, 4800, 3500]
    max_v = max(vals)

    lines = []
    lines.append(
        Panel(
            Text("VENTAS POR CATEGORÍA", style="bold"),
            style="#1f3864",
            border_style="#1f3864",
            padding=(1, 2),
            width=42,
        )
    )
    for name, val in zip(cats, vals):
        nblocks = int(val / max_v * 30)
        bar = "█" * nblocks + "░" * (30 - nblocks)
        lines.append(Text(f"{name:<14} {bar}  {val:,}"))

    lines.append(Text())
    lines.append(
        Panel(
            Text("COMPOSICIÓN", style="bold"),
            style="#1f3864",
            border_style="#1f3864",
            width=30,
        )
    )
    for pct in [30, 25, 20, 15, 10]:
        blocks = int(pct / 100 * 20)
        lines.append(Text(f"{pct:>3}% {'█' * blocks}{'░' * (20 - blocks)}"))

    return lines


def table_row():
    table = Table("TRANSACIONES RECIENTES", box=None, style="#1f3864")
    table.add_column("Fecha", style="cyan", width=10)
    table.add_column("Categoría", style="magenta", width=14)
    table.add_column("Ventas", style="green", justify="right", width=10)
    table.add_column("Clientes", style="yellow", justify="right", width=10)
    rows = [
        ("2026-10-20", "Electrónica", "12,450", "340"),
        ("2026-10-19", "Ropa", "8,900", "210"),
        ("2026-10-18", "Hogar", "6,120", "180"),
        ("2026-10-17", "Deportes", "4,800", "150"),
        ("2026-10-16", "Libros", "3,500", "120"),
    ]
    for r in rows:
        table.add_row(*r, end_section=False)
    return Panel(
        table, style="#2e405a", border_style="#2e405a", padding=(1, 2)
    )


def progress_row():
    return [
        Panel(
            "PROGRESO VS META",
            style="bold",
            border_style="#1f3864",
            padding=(0, 2),
        ),
        Text("Meta mensual: $450,000      Actual: $450,000       100%"),
        Text("[█" * 50 + "] 100%"),
    ]


def footer():
    return Panel(
        Text("Presione Ctrl+C para salir", style="italic #666666", justify="center"),
        style="#1f3864",
        padding=(1, 4),
    )


def draw():
    console.print()
    console.print(*header())
    console.print()
    console.print(kpi_row())
    console.print()
    console.print(*charts_row())
    console.print()
    console.print(table_row())
    console.print()
    console.print(*progress_row())
    console.print()
    console.print(footer())


def main():
    while True:
        console.clear()
        draw()
        time.sleep(2)


if __name__ == "__main__":
    main()
