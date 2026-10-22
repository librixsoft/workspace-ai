#!/usr/bin/env python3
"""
Dashboard TUI (Terminal UI) — rediseño visual v3.

Secciones:
  1. Header: título + hora + estado de conexión del feed
  2. KPIs: tarjetas con valor, label, sparkline y delta vs. anterior
  3. Charts: barras de ventas por categoría + donut de distribución
  4. Tabla: transacciones recientes con zebra striping
  5. Progreso: barra de avance vs. meta mensual + tendencia
  6. Feed de actividad: última operación + alertas
  7. Footer: controles de teclado + hint de salida

Controles:
  Q  = salir
  R  = refrescar datos manualmente
"""

import random
import time
from datetime import datetime
from typing import Dict, List, Tuple

from rich import box
from rich.console import Console
from rich.table import Table
from rich.columns import Columns
from rich.panel import Panel
from rich.text import Text
from rich.style import Style
from rich.align import Align

console = Console(width=130)

# ---------------------------------------------------------------------------
# Paleta de diseño (GitHub Dark-ish + acentos)
# ---------------------------------------------------------------------------
BG = "#0d1116"
PANEL_BG = "#161b22"
PANEL_BORDER = "#30363d"
ACCENT = "#58a6ff"
ACCENT_DIM = "#4c8bf2"
GREEN = "#3fbf6b"
GREEN_DIM = "#2e7d32"
RED = "#f0584a"
RED_DIM = "#c44e46"
YELLOW = "#d29922"
MUTED = "#8b949e"
WHITE = "#c6d0d0"
CYAN = "#39c5cf"
MAGENTA = "#bc8cff"
BLUE = "#79aae8"

SUCCESS_BG = "#1fff1f"
WARN_BG = "#3e240f"
DANGER_BG = "#3e1010"

PANEL_STYLE = Style(color=WHITE, bgcolor=PANEL_BG)

# ---------------------------------------------------------------------------
# Datos base (se fluctúan en cada ciclo para simular telemetry en vivo)
# ---------------------------------------------------------------------------
CATEGORIES: List[str] = ["Electrónica", "Ropa", "Hogar", "Deportes", "Libros"]
BASE_SALES: List[int] = [12000, 8500, 6200, 4800, 3500]

LAST_UPDATED = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

# Métricas de referencia del período anterior (escala acorde a los datos base)
PREV = {
    "VENTAS_TOTALES": 33000,
    "PROM_DIARIA": 6600,
    "CATEGORIAS": 5,
    "CLIENTES": 1240,
}


def jitter(value: int, pct: float = 8.0) -> int:
    """Aplica una fluctuación aleatoria suave a un valor."""
    delta = int(value * pct / 100.0 * random.uniform(-1, 1))
    return max(1, value + delta)


def fmt_money(n: int) -> str:
    return f"${n:,}"


# ---------------------------------------------------------------------------
# Componentes de UI
# ---------------------------------------------------------------------------
def header() -> Panel:
    state = "CONECTADO" if random.random() > 0.02 else "RECONECTANDO..."
    st_style = Style(color=GREEN, bold=True) if state == "CONECTADO" else Style(color=YELLOW, bold=True)
    subtitle = Text(
        f"Actualizado {datetime.now().strftime('%H:%M:%S')}  •  feed {state}  •  intervalo 2s",
        style=Style(color=MUTED, italic=True),
        justify="center",
    )
    return Panel(
        Align.center(Text("DASHBOARD PYTHON", style="bold white")),
        box=box.ROUNDED,
        border_style=ACCENT,
        title=Text("OVERVIEW", style=Style(color=ACCENT, bold=True)),
        title_align="center",
        subtitle=subtitle,
        subtitle_align="center",
        padding=(1, 2),
        style=PANEL_STYLE,
    )


def sparkline(values: List[int]) -> str:
    """Mini sparkline braille."""
    if not values:
        return ""
    mn, mx = min(values), max(values)
    if mx == mn:
        mx = mn + 1
    chars = "⠠⠡⠢⠣⠄⠥⠦⠧⠨⠩⠤⠆⠇⠧⠩⠸⠹"
    # map value to 10 levels
    idxs = [int((v - mn) / (mx - mn) * (len(chars) - 1)) for v in values]
    return "".join(chars[i] for i in idxs)


def kpi_card(icon: str, label: str, value: str, delta: str, positive: bool, color: str, spark: str) -> Panel:
    delta_style = Style(color=GREEN, bold=True) if positive else Style(color=RED, bold=True)
    content = Text(justify="center")
    content.append(icon + "\n", Style(color=MUTED, bold=True))
    content.append(value + "\n", Style(color=color, bold=True))
    content.append(label + "\n", Style(color=MUTED, bold=True))
    content.append((("▲" if positive else "▼") + " " + delta), delta_style)
    content.append("\n", "")
    content.append(spark, Style(color=color, bold=True))
    return Panel(
        content,
        box=box.ROUNDED,
        border_style=color,
        padding=(1, 2),
        width=24,
        style=PANEL_STYLE,
    )


def kpi_row() -> Columns:
    now = datetime.now()
    # valores base + jitter
    ventas_tot = sum(jitter(v) for v in BASE_SALES)
    prom_dia = int(ventas_tot / len(BASE_SALES))
    categorias = len(CATEGORIES)
    clientes = 1240 + random.randint(-30, 30)

    def delta_str(cur: int, prev: int) -> Tuple[str, bool]:
        d = cur - prev
        pct = (d / prev * 100) if prev else 0.0
        return (f"+{pct:.1f}%", d > 0) if d >= 0 else (f"{pct:.1f}%", False)

    d_ventas, pos_ventas = delta_str(ventas_tot, PREV["VENTAS_TOTALES"])
    d_prom, pos_prom = delta_str(prom_dia, PREV["PROM_DIARIA"])
    d_clientes, pos_clientes = delta_str(clientes, PREV["CLIENTES"])

    kpis = [
        ("💰", "VENTAS TOTALES", fmt_money(ventas_tot), d_ventas, pos_ventas, GREEN, sparkline([jitter(v) for v in BASE_SALES])),
        ("📈", "PROM. DIARIA", fmt_money(prom_dia), d_prom, pos_prom, ACCENT, sparkline([jitter(v) for v in BASE_SALES])),
        ("🏷️", "CATEGORÍAS", str(categorias), "+2", True, MAGENTA, ""),
        ("👥", "CLIENTES", f"{clientes:,}", f"{clientes - PREV['CLIENTES']:+}", pos_clientes, YELLOW, sparkline([random.randint(1100, 1350) for _ in range(7)])),
    ]
    panels = [kpi_card(*k) for k in kpis]
    return Columns(panels, equal=True, expand=True)


def bar_chart(cats: List[str], vals: List[int], title: str, color: str) -> Panel:
    max_v = max(vals) if vals else 1
    content = Text(justify="left")
    content.append(title + "\n", Style(color=color, bold=True))
    for name, val in zip(cats, vals):
        blocks = max(1, int(val / max_v * 34))
        bar = "█" * blocks + "░" * (34 - blocks)
        content.append(f"{name:<15} {bar}  {val:>8,}\n", Style(color=WHITE, bold=True))
    return Panel(
        content,
        box=box.ROUNDED,
        border_style=color,
        title=Text(title, style=Style(color=color, bold=True)),
        title_align="center",
        padding=(1, 2),
        style=PANEL_STYLE,
    )


def donut(pcts: List[Tuple[str, int, str]]) -> Panel:
    total = sum(p for _, p, _ in pcts)
    content = Text(justify="left")
    content.append("DISTRIBUCIÓN\n", Style(color=ACCENT, bold=True))
    for name, pct, color in pcts:
        blocks = max(1, int(pct / 100.0 * 28))
        bar = "█" * blocks + "░" * (28 - blocks)
        content.append(f"{name:<14} {bar} {pct:>3}%\n", Style(color=color, bold=True))
    return Panel(
        content,
        box=box.ROUNDED,
        border_style=PANEL_BORDER,
        title=Text("DISTRIBUCIÓN", style=Style(color=ACCENT, bold=True)),
        title_align="center",
        padding=(1, 2),
        style=PANEL_STYLE,
    )


def transactions_table() -> Table:
    table = Table(
        "Fecha",
        "Categoría",
        "Ventas",
        "Clientes",
        title="TRANSACCIONES RECIENTES",
        title_style="bold " + ACCENT,
        border_style=PANEL_BORDER,
        header_style="bold " + ACCENT,
        style=WHITE,
        box=box.ROUNDED,
        show_lines=False,
    )
    table.add_column("Fecha", style=CYAN, width=11, justify="center")
    table.add_column("Categoría", style=MAGENTA, width=14)
    table.add_column("Ventas", style=GREEN, justify="right", width=10)
    table.add_column("Clientes", style=YELLOW, justify="right", width=10)

    rows = [
        ("2026-10-20", "Electrónica", "12,450", "340"),
        ("2026-10-19", "Ropa", "8,900", "210"),
        ("2026-10-18", "Hogar", "6,120", "180"),
        ("2026-10-17", "Deportes", "4,800", "150"),
        ("2026-10-16", "Libros", "3,500", "120"),
    ]
    for i, r in enumerate(rows):
        style = "" if i % 2 == 0 else Style(bgcolor="#1e2938")
        table.add_row(*r, style=style)
    return table


def progress_row() -> Panel:
    pct = 78.0 + random.uniform(-2, 2)
    pct = max(0, min(100, pct))
    meta = "$450,000"
    actual = f"${int(pct / 100 * 450000):,}"
    bar_width = 42
    filled = int(round(pct / 100.0 * bar_width))
    bar = "█" * filled + "░" * (bar_width - filled)
    trend = random.choice(["▲ +2.1% vs. semana pasada", "▼ -0.8% vs. semana pasada", "▬ estable"])
    content = Text(justify="left")
    content.append("Meta mensual: " + meta + "\n", Style(color=MUTED, bold=True))
    content.append("Actual: " + actual + "\n", Style(color=WHITE, bold=True))
    content.append(f"[{bar}] {pct:.0f}%\n", Style(color=GREEN, bold=True))
    content.append(trend, Style(color=YELLOW, bold=True))
    return Panel(
        content,
        box=box.ROUNDED,
        border_style=GREEN,
        title=Text("PROGRESO VS META", style=Style(color=GREEN, bold=True)),
        title_align="center",
        padding=(1, 2),
        style=PANEL_STYLE,
    )


def activity_feed() -> Panel:
    alerts = [
        ("Nueva venta $2,400 — Electrónica", GREEN),
        ("Cliente nuevo: María López", CYAN),
        ("Alerta: stock bajo en Ropa", RED),
    ]
    item = random.choice(alerts)
    content = Text(justify="left")
    content.append("ÚLTIMA ACTIVIDAD  ", Style(color=ACCENT, bold=True))
    content.append(item[0] + "\n", Style(color=WHITE, bold=True))
    content.append(f"[{datetime.now().strftime('%H:%M:%S')}] ", Style(color=MUTED))
    return Panel(
        content,
        box=box.ROUNDED,
        border_style=PANEL_BORDER,
        title=Text("FEED", style=Style(color=ACCENT, bold=True)),
        title_align="left",
        padding=(1, 2),
        style=PANEL_STYLE,
    )


def footer() -> Panel:
    hints = [
        ("Q", "salir"),
        ("R", "refrescar"),
    ]
    chips = "   ".join(f"[{k}] {v}" for k, v in hints)
    return Panel(
        Align.center(Text(f"Controles: {chips}   •   Ctrl+C para salir", style=Style(color=MUTED, italic=True))),
        box=box.ROUNDED,
        border_style=PANEL_BORDER,
        padding=(1, 2),
        style=PANEL_STYLE,
    )


# ---------------------------------------------------------------------------
# Layout principal
# ---------------------------------------------------------------------------
def draw() -> None:
    console.clear()
    console.print()
    console.print(header())
    console.print()
    console.print(kpi_row())
    console.print()
    # charts: dos columnas
    charts = Columns(
        [bar_chart(CATEGORIES, BASE_SALES, "VENTAS POR CATEGORÍA", ACCENT),
         donut([("Electrónica", 30, "#58a6ff"), ("Ropa", 25, "#f778ba"), ("Hogar", 20, "#3fb950"), ("Deportes", 15, "#d29922"), ("Libros", 10, "#bc8cff")])],
        equal=True,
        expand=True,
    )
    console.print(charts)
    console.print()
    console.print(transactions_table())
    console.print()
    console.print(progress_row())
    console.print()
    console.print(activity_feed())
    console.print()
    console.print(footer())


def main() -> None:
    manual_refresh = False
    try:
        while True:
            draw()
            # leer tecla sin bloquear (ver si hay input)
            import select, sys
            if manual_refresh:
                manual_refresh = False
            if select.select([sys.stdin], [], [], 0)[0]:
                ch = sys.stdin.read(1).lower()
                if ch == "q":
                    console.print("\nSaliendo...", style=Style(color=MUTED))
                    break
                elif ch == "r":
                    manual_refresh = True
                    draw()
                    manual_refresh = False
            time.sleep(2)
    except KeyboardInterrupt:
        console.print("\nInterrumpido por el usuario.", style=Style(color=MUTED))


if __name__ == "__main__":
    main()
