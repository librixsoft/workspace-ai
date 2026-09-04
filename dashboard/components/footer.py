from textual.widgets import Static, Label
from textual.containers import Horizontal


class AppFooter(Horizontal):
    """Status bar at the bottom."""

    DEFAULT_CSS = """
    .footer-container {
        width: 100%;
        height: auto;
        border-top: solid $primary;
        padding: 1 2;
        background: $surface-darken-2;
    }
    .status-dot {
        color: $success;
        margin-right: 1;
    }
    .shortcut-label {
        width: auto;
        height: auto;
        padding: 0 1;
        color: $text-lighten-1;
    }
    """

    def compose(self):
        yield Static("● Connected", classes="status-dot")
        yield Label("v1.0.0", classes="shortcut-label")
        yield Label("Tab: navigate | Enter: select | Esc: back", classes="shortcut-label")

