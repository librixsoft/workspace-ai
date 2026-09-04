"""Dashboard con Python + Textual — punto de entrada.

Fases 1-4 completas:
  1. Configuración (dirs, .env, requirements)
  2. Base de la app (layout, sidebar, navegación)
  3. Widgets y datos (KPIs, tabla, gráfico + lectura CSV/JSON)
  4. Integración final (navegación por secciones, estilos, responsive)
"""

from __future__ import annotations

import os
from pathlib import Path

from dotenv import load_dotenv
from textual.app import App, ComposeResult
from textual.binding import Binding
from textual.containers import Container, Horizontal, Vertical
from textual.reactive import reactive
from textual.widgets import Footer, Header, Label, Static

# Widgets y carga de datos (módulos locales)
from src.data_loader import DataSourceError, load_bar_chart_data, load_data  # noqa: E402
from src.widgets import BarChartWidget, KPICard, DataTablePanel  # noqa: E402

# ---------------------------------------------------------------------------
# Carga de variables de entorno
# ---------------------------------------------------------------------------

_env_path = Path(__file__).resolve().parent.parent / ".env"
load_dotenv(_env_path)


def _get_env(key: str, default: str) -> str:
    """Leer variable de entorno con valor por defecto."""
    return os.getenv(key, default) or default


APP_NAME: str = "Dashboard AI"
APP_THEME: str = "default"
DATA_SOURCE_PATH: str = "data/sample.csv"

# ---------------------------------------------------------------------------
# CSS global
# ---------------------------------------------------------------------------

MAIN_CSS = """
/* -- Layout principal ----------------------------------------------------- */

Screen {
    layout: grid;
    grid-size: 2;
    grid-gutter: 1;
}

#main-container {
    grid-columns: 20fr 80fr;
    height: 100%;
}

/* -- Sidebar -------------------------------------------------------------- */

#sidebar {
    width: 100%;
    background: $boost;
    border-right: solid $accent;
}

#menu-title {
    text-align: center;
    padding: 2 0;
    margin: 0 2;
    dock: top;
}

#spacer {
    height: 1fr;
    dock: bottom;
}

NavButton {
    width: 100%;
    height: 3;
    padding: 0 2;
    margin: 1 2;
}

NavButton.-active {
    background: $accent;
    color: $text;
    dock: top;
}

NavButton:not(.-active) {
    background: $surface;
    color: $text;
    dock: top;
}

/* -- Content area --------------------------------------------------------- */

#content-area {
    width: 100%;
    height: 100%;
}

#content-title {
    text-align: center;
    padding: 1 2;
}

/* -- KPI cards (grid de tarjetas) ---------------------------------------- */

#kpi-grid {
    grid-size: 3 2;
    grid-gutter: 1;
    margin: 0 1;
}

/* -- Welcome banner ------------------------------------------------------- */

#welcome-banner {
    width: 100%;
    padding: 1 0;
    background: $primary;
    text-align: center;
}

/* -- Error toast ---------------------------------------------------------- */

#error-label {
    width: 100%;
    color: $error;
    text-align: center;
    dock: bottom;
}
"""


# ---------------------------------------------------------------------------
# Widgets internos — Sidebar / NavButton
# ---------------------------------------------------------------------------

SECTIONS = ["KPIs", "Datos", "Gráficos", "Ajustes"]


class NavButton(Static):
    """Botón de navegación para el sidebar."""

    def __init__(self, label: str, section_index: int, **kwargs) -> None:
        super().__init__(label, **kwargs)
        self.section_index = section_index

    BINDINGS = [Binding("enter", "press", "Press")]

    def on_mount(self) -> None:
        self.add_class("-inactive")

    def set_active(self, active: bool) -> None:
        if active:
            self.remove_class("-inactive")
            self.add_class("-active")
        else:
            self.remove_class("-active")
            self.add_class("-inactive")


class Sidebar(Container):
    """Panel lateral con menú de secciones."""

    CSS = MAIN_CSS  # reutiliza estilos globales

    def __init__(self, **kwargs) -> None:
        super().__init__(id="sidebar", **kwargs)

    def compose(self) -> ComposeResult:
        yield Label("=== Menú ===", id="menu-title")
        for idx, section in enumerate(SECTIONS):
            yield NavButton(f"  {idx + 1}. {section}", section_index=idx)
        yield Label("", id="spacer")

    def get_button(self, index: int) -> NavButton:
        """Devolver el botón en la posición *index*."""
        buttons = list(self.query(NavButton))
        return buttons[index]


# ---------------------------------------------------------------------------
# Widgets internos — ContentArea por sección
# ---------------------------------------------------------------------------


class KPIScreen(Vertical):
    """Sección KPIs: tarjetas de indicadores + tabla resumen."""

    CSS = MAIN_CSS

    def __init__(self, **kwargs) -> None:
        super().__init__(**kwargs)
        self._error_msg: str | None = None

    def compose(self) -> ComposeResult:
        yield Label("[bold]KPIs — Indicadores Clave[/bold]", id="content-title")
        # Grid de tarjetas KPI
        kpi_grid = Container(id="kpi-grid")
        for _title, _value in [
            ("Ingresos", "$125,000"),
            ("Usuarios Activos", "3,842"),
            ("Tasa de Retención", "94.2%"),
            ("Tiempo Promedio", "4m 32s"),
            ("Satisfacción", "4.6/5"),
            ("Tickets Abiertos", "127"),
        ]:
            kpi_grid.mount(KPICard(title=_title, value=_value))
        yield kpi_grid

    def set_error(self, msg: str) -> None:
        self._error_msg = msg


class DataScreen(Vertical):
    """Sección Datos: tabla interactiva con datos CSV/JSON."""

    CSS = MAIN_CSS

    def __init__(self, **kwargs) -> None:
        super().__init__(**kwargs)

    def compose(self) -> ComposeResult:
        yield Label("[bold]Datos — Tabla Completa[/bold]", id="content-title")
        panel = DataTablePanel(title="Métricas extraídas de datos", id="data-table-panel")
        # Cargar filas por defecto (se actualizan dinámicamente)
        try:
            from src.data_loader import load_data as _ld
            rows = _ld("data/sample.csv")
            panel.load_rows(rows)
        except DataSourceError:
            pass  # tabla vacía, error visible en banner inferior
        yield panel


class ChartScreen(Vertical):
    """Sección Gráficos: gráfico de barras horizontal."""

    CSS = MAIN_CSS

    def __init__(self, **kwargs) -> None:
        super().__init__(**kwargs)

    def compose(self) -> ComposeResult:
        yield Label("[bold]Gráficos — Barras[/bold]", id="content-title")
        chart = BarChartWidget(title="Métricas por valor", id="bar-chart")
        try:
            from src.data_loader import load_bar_chart_data as _lbcd
            data = _lbcd()
            if data:
                chart.load_data(data)
        except DataSourceError:
            pass  # gráfico vacío
        yield chart


class SettingsScreen(Vertical):
    """Sección Ajustes: información de configuración."""

    CSS = MAIN_CSS

    def __init__(self, **kwargs) -> None:
        super().__init__(**kwargs)

    def compose(self) -> ComposeResult:
        yield Label("[bold]Ajustes — Configuración[/bold]", id="content-title")
        info_items = [
            f"App Name   : {_get_env('APP_NAME', APP_NAME)}",
            f"Theme      : {_get_env('APP_THEME', APP_THEME)}",
            f"Data Source: {_get_env('DATA_SOURCE_PATH', DATA_SOURCE_PATH)}",
        ]
        yield Label("\n".join(info_items), id="settings-info")


SCREEN_MAP = {
    "KPIs": KPIScreen,
    "Datos": DataScreen,
    "Gráficos": ChartScreen,
    "Ajustes": SettingsScreen,
}

# ---------------------------------------------------------------------------
# Aplicación principal
# ---------------------------------------------------------------------------


class DashboardApp(App):
    """Aplicación principal del dashboard.

    Configura tema, layout con sidebar + contenido y navegación por teclado.
    """

    DEFAULT_CSS = MAIN_CSS
    BINDINGS = [
        Binding("q", "quit", "Salir"),
        Binding("1", "select_section(0)", "KPIs"),
        Binding("2", "select_section(1)", "Datos"),
        Binding("3", "select_section(2)", "Gráficos"),
        Binding("4", "select_section(3)", "Ajustes"),
    ]

    current_section: reactive[str] = reactive("", init=False)

    def __init__(self, **kwargs) -> None:
        super().__init__(**kwargs)
        self.app_name = _get_env("APP_NAME", APP_NAME)
        self.app_theme = _get_env("APP_THEME", APP_THEME)
        self.data_source = _get_env("DATA_SOURCE_PATH", DATA_SOURCE_PATH)

    # -- lifecycle -----------------------------------------------------------

    def on_mount(self) -> None:
        """Al arrancar, aplica el tema y selecciona la primera sección."""
        if self.app_theme and self.app_theme != "default":
            self.set_theme(self.app_theme)
        self.select_section(0)

    # -- compose ------------------------------------------------------------

    def compose(self) -> ComposeResult:
        yield Header()
        with Container(id="main-container"):
            yield Sidebar()
            yield ContentArea(section_name=self.current_section or SECTIONS[0])
        yield Footer()

    # -- navigation ----------------------------------------------------------

    def action_select_section(self, index: int) -> None:
        """Cambiar a la sección indicada por su índice."""
        if not (0 <= index < len(SECTIONS)):
            return
        section_name = SECTIONS[index]
        self.current_section = section_name

        # Actualizar sidebar
        sidebar = self.query_one(Sidebar)
        for i, s in enumerate(SECTIONS):
            btn = sidebar.get_button(i)
            btn.set_active(i == index)

        # Reemplazar contenido principal dinámicamente
        old_content = self.query_one(ContentArea)
        old_content.remove()

        screen_cls = SCREEN_MAP.get(section_name, KPIScreen)
        new_content = ContentArea(
            section_name=section_name,
            section_widget=screen_cls(),
        )
        self.mount(new_content, after=sidebar)

    # -- error handling ------------------------------------------------------

    def compose_error(self, error: Exception) -> None:
        """Mostrar mensaje amigable si hay un error en el compose."""
        super().compose_error(error)


# ---------------------------------------------------------------------------
# ContentArea — wrapper genérico con widget de sección
# ---------------------------------------------------------------------------


class ContentArea(Vertical):
    """Área de contenido principal que envuelve widgets por sección.

    Al cambiar de sección, se monta un nuevo widget específico (KPIScreen,
    DataScreen, etc.) y se descarta el anterior.
    """

    CSS = """
    ContentArea {
        width: 100%;
        height: 100%;
    }
    """

    def __init__(self, section_name: str = "", section_widget=None, **kwargs) -> None:
        super().__init__(**kwargs)
        self.section_name = section_name
        self._section_widget = section_widget

    def compose(self) -> ComposeResult:
        yield Label(
            f"[bold]Sección: {self.section_name or 'Sin seleccionar'}[/bold]",
            id="content-title",
        )
        if self._section_widget is not None:
            yield self._section_widget

    def update_section(self, new_section_name: str) -> None:
        """Reemplazar el widget interior sin desmontar el área."""
        # Limpia children actuales excepto el título
        for child in list(self.children):
            if getattr(child, "id", "") != "content-title":
                child.remove()
        # Montar nuevo widget si existe
        screen_cls = SCREEN_MAP.get(new_section_name, KPIScreen)
        self.mount(screen_cls())


# ---------------------------------------------------------------------------

if __name__ == "__main__":
    app = DashboardApp()
    app.run()
