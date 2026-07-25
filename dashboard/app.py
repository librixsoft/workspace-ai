from textual.app import App, ComposeResult
from textual.containers import Container, Horizontal, Vertical
from textual.widgets import Static

from dashboard.components.header import AppHeader
from dashboard.components.footer import AppFooter
from dashboard.components.menu import AppMenu
from dashboard.components.cards import MetricsGrid


class DashboardView(Static):
    """Dynamic view shown in the content area."""

    DEFAULT_CSS = """
    .view-content {
        width: 100%;
        height: auto;
        padding: 2;
        border: wide round $primary;
        background: $surface-darken-1;
        text-align: center;
        vertical-align: middle;
        content-align: center middle;
    }
    """

    def compose(self):
        yield Static("📊 Bienvenido al Dashboard", classes="view-content")


class SettingsView(Static):
    DEFAULT_CSS = """
    .settings-view {
        width: 100%;
        height: auto;
        padding: 2;
        text-align: center;
        vertical-align: middle;
    }
    """

    def compose(self):
        yield Static("⚙️ Configuración", classes="settings-view")


class StatsView(Static):
    DEFAULT_CSS = """
    .stats-view {
        width: 100%;
        height: auto;
        padding: 2;
        text-align: center;
        vertical-align: middle;
    }
    """

    def compose(self):
        yield Static("📈 Estadísticas", classes="stats-view")


class DashboardApp(App):
    TITLE = "My Dashboard"

    CSS = """
    #content-area {
        width: 100%;
        height: auto;
        margin-top: -25;
        margin-left: 27;
    }
    """

    def compose(self) -> ComposeResult:
        yield AppHeader()
        yield Container(
            MenuLayout(),
            Vertical(MetricsGrid()),
            id="main-layout",
        )
        yield AppFooter()


class MenuLayout(Container):
    """Widget wrapping menu + content area."""

    DEFAULT_CSS = """
    .menu-content-wrapper {
        width: 100%;
        height: auto;
        margin-top: -25;
    }
    """

    def compose(self) -> ComposeResult:
        yield AppMenu()
        # Content area gets set dynamically


if __name__ == "__main__":
    DashboardApp().run()

