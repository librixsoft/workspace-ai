from textual.widgets import Static, Label
from textual.containers import Horizontal


class AppHeader(Horizontal):
    """Top bar: title + live clock."""

    DEFAULT_CSS = """
    .header-container {
        width: 100%;
        height: auto;
        border-bottom: solid $primary;
        padding: 1 2;
        background: $surface-darken-2;
    }
    .title-label {
        color: $text-lighten-3;
        text-style: bold;
        width: auto;
        height: auto;
        margin-right: 2;
    }
    .clock-label {
        width: auto;
        height: auto;
        padding: 0 1;
        color: $text-lighten-1;
    }
    """

    def compose(self):
        yield Static("📊 Dashboard", classes="title-label")
        yield Label(id="clock", classes="clock-label")

    def on_mount(self):
        self._update_clock()
        self.set_interval(1, self._update_clock)  # type: ignore[arg-type]

    def _update_clock(self):
        from datetime import datetime

        clock = self.query_one("#clock", Label)
        clock.update(datetime.now().strftime("%Y-%m-%d %H:%M:%S"))

