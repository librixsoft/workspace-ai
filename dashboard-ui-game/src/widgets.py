"""Widgets reutilizables para el dashboard — Fases 3 y 4."""

from textual.binding import Binding
from textual.containers import Container, ScrollableContainer
from textual.widgets import DataTable, Static


# ---------------------------------------------------------------------------
# Estilos compartidos
# ---------------------------------------------------------------------------

_KPI_CSS = """
KPICard {
    width: 1fr;
    height: auto;
    min-height: 8;
    margin: 0 1;
    padding: 1 2;
    border: solid $accent;
    background: $surface;
}

KPICard .kpi-title {
    width: 100%;
    text-align: center;
    dock: top;
}

KPICard .kpi-value {
    width: 100%;
    text-align: center;
    height: auto;
    margin-top: 1;
}
"""


class KPICard(ScrollableContainer):
    """Tarjeta para un indicador KPI (Key Performance Indicator).

    Uso:
        KPICard(title="Usuarios Activos", value="3,842")
    """

    DEFAULT_CSS = _KPI_CSS

    def __init__(self, title: str, value: str, **kwargs) -> None:
        super().__init__(**kwargs)
        self._title_text = title
        self._value_text = value

    def compose(self):
        yield Static(f"[bold]{self._title_text}[/bold]", classes="kpi-title")
        yield Static(
            f"[bold large]{self._value_text}[/bold large]",
            classes="kpi-value",
        )


# ---------------------------------------------------------------------------
# Tabla de datos
# ---------------------------------------------------------------------------

_TABLE_CSS = """
DataTablePanel {
    width: 100%;
    height: 100%;
    margin: 1;
}

DataTablePanel .data-table-title {
    width: 100%;
    padding: 1;
}
"""


class DataTablePanel(Container):
    """Tabla interactiva para mostrar datos CSV/JSON.

    Uso:
        panel = DataTablePanel(title="Métricas")
        panel.load_from_csv("data/sample.csv")
    """

    DEFAULT_CSS = _TABLE_CSS
    BINDINGS = [Binding("escape", "blur", "Salir de tabla")]

    def __init__(self, title: str = "", **kwargs) -> None:
        super().__init__(**kwargs)
        self._title = title
        self.table = DataTable()

    def compose(self):
        if self._title:
            yield Static(f"[bold]{self._title}[/bold]", classes="data-table-title")
        self.table.add_columns("Métrica", "Valor", "Dirección")
        yield self.table

    def load_rows(self, rows: list[tuple[str, str, str]]) -> None:
        """Insertar filas en la tabla (sin borrar columnas existentes)."""
        for row in rows:
            if len(row) >= 3:
                self.table.add_row(*row[:3])

    def clear_rows(self) -> None:
        self.table.clear()


# ---------------------------------------------------------------------------
# Gráfico de barras básico
# ---------------------------------------------------------------------------

_BAR_CSS = """
BarChartWidget {
    width: 100%;
    height: 100%;
    margin: 1;
}
"""


class BarChartWidget(ScrollableContainer):
    """Gráfico de barras horizontal simplificado.

    Renderiza datos usando caracteres ASCII ─ útil para terminales sin soporte
    gráfico.

    Uso:
        chart = BarChartWidget(title="Ingresos mensuales")
        chart.load_data([("Ene", 120), ("Feb", 150), ...])
    """

    DEFAULT_CSS = _BAR_CSS

    MAX_BAR_WIDTH = 60  # columnas máximas para la barra más larga
    BAR_CHAR = "█"

    def __init__(self, title: str = "", **kwargs) -> None:
        super().__init__(**kwargs)
        self._title = title
        self._data: list[tuple[str, float]] = []
        self._max_value: float = 0.0

    def load_data(self, data: list[tuple[str, float]]) -> None:
        """Cargar pares (etiqueta, valor) y recalcular la escala."""
        self._data = [(label, max(0, val)) for label, val in data]
        self._max_value = max((v for _, v in self._data), default=1.0)

    def compose(self):
        if not self._data:
            yield Static("[dim]Sin datos para mostrar[/dim]")
            return

        # Título
        yield Static(f"[bold]{self._title}[/bold]", id="bar-chart-title")

        container = Container(id="bars-container", classes="bars-row")
        for label, value in self._data:
            bar_width = int((value / self._max_value) * self.MAX_BAR_WIDTH) if self._max_value else 0
            bar = "█" * max(bar_width, 1)
            yield Static(f"{label:>8} │ {bar} {value:.0f}", classes="bar-row")
