package dev.tambo.dashboard;

import static dev.tamboui.toolkit.Toolkit.*;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.style.Color;
import dev.tamboui.layout.Flex;
import dev.tamboui.layout.Alignment;

public class DashboardApp extends ToolkitApp {

    private int selectedTab = 0;

    @Override
    protected Element render() {
        Element header = row(
            text("System Dashboard").bold(),
            spacer(),
            text("[Tab] Switch | [Q] Quit").dim()
        );

        Element tabBar = row(
            tabButton("Overview", selectedTab == 0),
            tabButton("Metrics", selectedTab == 1),
            tabButton("Logs", selectedTab == 2)
        );

        Element content = switch (selectedTab) {
            case 0 -> renderOverview();
            case 1 -> renderMetrics();
            case 2 -> renderLogs();
            default -> renderOverview();
        };

        return column(
            header,
            spacer(),
            tabBar,
            spacer(),
            content
        ).onKeyEvent(this::handleKeys);
    }

    private Element renderOverview() {
        return column(
            row(
                renderStatPanel("CPU Usage", "23%", Color.GREEN),
                spacer(1),
                renderStatPanel("Memory", "4.2 GB / 16 GB", Color.YELLOW),
                spacer(1),
                renderStatPanel("Disk", "128 GB / 512 GB", Color.YELLOW),
                spacer(1),
                renderStatPanel("Network", "Up 12 Down 45", Color.BLUE)
            ),
            spacer(2),
            renderMiniChart(),
            spacer(2),
            renderActivityPanel()
        );
    }

    private Element renderMetrics() {
        return column(
            panel("Performance Metrics",
                column(
                    row(
                        renderStatPanel("Requests/sec", "1,234", Color.GREEN),
                        spacer(2),
                        renderStatPanel("Avg Response", "45ms", Color.GREEN)
                    ),
                    spacer(2),
                    row(
                        renderStatPanel("Error Rate", "0.2%", Color.GREEN),
                        spacer(2),
                        renderStatPanel("Uptime", "99.97%", Color.GREEN)
                    )
                ),
                spacer(2)
            ).rounded().borderless(),
            spacer(2),
            panel("Activity Chart",
                sparkline(new int[]{10, 15, 13, 18, 22, 19, 25, 23, 28, 30})
            ).rounded().borderless()
        );
    }

    private Element renderLogs() {
        return panel("Recent Logs",
            column(
                text("[INFO]  Application started successfully").dim(),
                text("[INFO]  Connected to database").dim(),
                text("[WARN]  High memory usage detected").yellow(),
                text("[INFO]  Cache cleared").dim(),
                text("[INFO]  User session created").dim(),
                text("[ERROR] Timeout on external API call").red(),
                text("[INFO]  Retry succeeded").dim(),
                text("[INFO]  Scheduled task completed").dim()
            )
        ).rounded().borderless();
    }

    private Element renderStatPanel(String label, String value, Color color) {
        return panel(
            column(
                text(label).dim(),
                text(value).bold().fg(color)
            ).flex(Flex.CENTER)
        ).rounded().borderColor(color);
    }

    private Element renderMiniChart() {
        return panel("System Load (24h)",
            sparkline(new int[]{10, 15, 13, 18, 22, 19, 25, 23, 28, 30})
        ).rounded().borderless();
    }

    private Element renderActivityPanel() {
        return panel("Activity",
            column(
                row(text("●").green(), text(" Online")),
                row(text("●").green(), text(" All systems operational")),
                row(text("●").yellow(), text(" Minor latency spike").dim()),
                row(text("●").green(), text(" Backup completed").dim()),
                row(text("●").green(), text(" Monitoring active").dim())
            )
        ).rounded().borderless();
    }

    private Element tabButton(String label, boolean selected) {
        return panel(
            text(" " + label + " ")
                .bold()
                .fg(selected ? Color.CYAN : Color.WHITE)
        ).padding(1);
    }

    private EventResult handleKeys(KeyEvent event) {
        if (event.isQuit()) {
            quit();
            return EventResult.HANDLED;
        }
        if (event.isLeft()) {
            selectedTab = Math.max(0, selectedTab - 1);
            return EventResult.HANDLED;
        }
        if (event.isRight()) {
            selectedTab = Math.min(2, selectedTab + 1);
            return EventResult.HANDLED;
        }
        return EventResult.UNHANDLED;
    }

    public static void main(String[] args) throws Exception {
        new DashboardApp().run();
    }
}
