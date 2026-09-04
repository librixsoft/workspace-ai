from textual.widgets import Static
from textual.containers import Horizontal, Grid
import random


class MetricCard(Static):
    """Single metric card with label + value."""

    DEFAULT_CSS = """
    .metric-card {
        width: 1fr;
        height: auto;
        padding: 2;
        border: wide round $primary;
        background: $surface-darken-1;
    }
    .card-label {
        color: $text-lighten-1;
        margin-bottom: 1;
    }
    .card-value {
        font-size: 3;
        text-style: bold;
        color: $accent;
    }
    """

    def __init__(self, label: str, value: float, suffix: str = "") -> None:
        self._label = label
        self._value = value
        self._suffix = suffix
        super().__init__()

    def compose(self):
        yield Static(f"{self._label}", classes="card-label")
        yield Static(
            f"{'💰' if 'Venta' in self._label else ''}{'👥' if 'Usuario' in self._label else ''}"
            f"{'⚡' if 'Rendimiento' in self._label else ''}",
            classes="card-value",
        )

    def refresh_value(self):
        """Update the displayed value with a random number."""
        if "Venta" in self._label:
            self._value = round(random.uniform(1000, 50000), 2)
            suffix = "$"
        elif "Usuario" in self._label:
            self._value = int(random.randint(100, 5000))
            suffix = ""
        else:
            self._value = round(random.uniform(60, 99), 1)
            suffix = "%"

        value_label = self.query_one(".card-value", Static)
        value_label.update(f"{self._value:,.2f}".rstrip("0").rstrip(".") + suffix)


class MetricsGrid(Grid):
    """Container of metric cards that auto-refreshes."""

    DEFAULT_CSS = """
    .metrics-grid {
        width: 100%;
        height: auto;
        padding: 2;
        columns: 3;
        gap: 2;
    }
    """

    METRICS = [
        ("Ventas Totales", "$45,230"),
        ("Usuarios Activos", "1,847"),
        ("Rendimiento del Sistema", "97.2%"),
    ]

    def compose(self):
        for label in ["Ventas Totales", "Usuarios Activos", "Rendimiento"]:
            yield MetricCard(label)

    def on_mount(self):
        self.set_interval(3, self._refresh_all)  # type: ignore[arg-type]

    def _refresh_all(self):
        cards = self.query("MetricCard")
        for card in cards:
            card.refresh_value()

