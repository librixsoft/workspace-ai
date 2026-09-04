from textual.widgets import Static, Button, Footer as TextualFooter
from textual.containers import Vertical
from textual.app import ComposeResult


class AppMenu(Vertical):
    """Left sidebar menu."""

    DEFAULT_CSS = """
    .menu-container {
        width: 25;
        height: 100%;
        border-right: solid $primary;
        background: $surface-darken-1;
        padding: 1;
    }
    .nav-button {
        margin-bottom: 1;
        width: 100%;
    }
    """

    VIEWS = [
        ("Home", "🏠"),
        ("Stats", "📈"),
        ("Settings", "⚙️"),
    ]

    def compose(self) -> ComposeResult:
        yield Static("Navigation", classes="menu-title")
        for view, icon in self.VIEWS:
            yield Button(icon=f"{icon} ", variant="default", label=view, classes="nav-button")

