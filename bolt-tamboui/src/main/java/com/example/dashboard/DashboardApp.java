package com.example.dashboard;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import dev.tamboui.layout.Constraint;
import dev.tamboui.style.Color;
import dev.tamboui.toolkit.app.ToolkitRunner;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.tui.TuiConfig;

import static dev.tamboui.toolkit.Toolkit.*;
import dev.tamboui.toolkit.elements.Spacer;

public class DashboardApp {

    private static final AtomicInteger refreshCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        var config = TuiConfig.builder()
                .tickRate(java.time.Duration.ofMillis(100))
                .build();

        try (var runner = ToolkitRunner.create(config)) {
            runner.run(DashboardApp::renderDashboard);
        }
    }

    private static Element renderDashboard() {
        int tick = refreshCounter.getAndIncrement();

        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        return column(
                // Header with title and time
                panel(() -> row(
                        text(" TAMBOUI DASHBOARD ").bold().bg(Color.BLUE).white(),
                        Spacer.length(2),
                        text(" Refresh: " + tick + "  |  ").dim(),
                        text(timeStr).bold().cyan()
                ).spacing(1).length(1))
                .title("System Dashboard")
                .rounded()
                .borderColor(Color.CYAN)
                .length(3),

                text("").length(1),

                // Row 1: Stats cards
                row(
                        statCard("CPU", "12%", Color.RED, "  4 cores active"),
                        statCard("MEM", "45%", Color.YELLOW, "  8.2 GB / 16 GB"),
                        statCard("DISK", "68%", Color.GREEN, "  120 GB / 256 GB"),
                        statCard("NET", "2.1 MB/s", Color.CYAN, "  ↓ 1.5 MB/s ↑ 0.6 MB/s")
                ).spacing(1),

                text("").length(1),

                // Row 2: Process table on left, chart on right
                row(
                        // Left: Process list
                        panel(() -> column(
                                text(" Running Processes").bold().length(1),
                                processesTable()
                        ))
                        .title("Processes")
                        .rounded()
                        .borderColor(Color.DARK_GRAY)
                        .fill(),

                        // Right: Resource chart
                        panel(() -> column(
                                text(" Resource Usage").bold().length(1),
                                barChartWidget()
                        ))
                        .title("Resources")
                        .rounded()
                        .borderColor(Color.MAGENTA)
                        .fill()
                ).fill(),

                text("").length(1),

                // Footer
                panel(() -> row(
                        text(" Press 'q' to quit  |  ").dim(),
                        text(" TamboUI 0.6.0").dim(),
                        text("  |  Java 17").dim()
                ).spacing(1).length(1))
                .rounded()
                .borderColor(Color.DARK_GRAY)
                .length(1),

                // Quit handler on the root column
                text("").length(1)
        ).onKeyEvent(event -> {
            if (event.isChar('q')) {
                return EventResult.HANDLED;
            }
            return EventResult.UNHANDLED;
        });
    }

    private static Element barChartWidget() {
        return barChart()
                .data(12, 18, 25, 15, 22, 18, 28, 20)
                .barColor(Color.CYAN)
                .title("CPU Usage (%)")
                .rounded()
                .borderColor(Color.CYAN)
                .fill();
    }

    private static Element processesTable() {
        return table()
                .header("PID", "USER", "CPU%", "MEM%", "CMD")
                .row("1001", "root", "5.2", "45.1", "systemd")
                .row("1002", "root", "3.1", "12.3", "sshd")
                .row("1003", "admin", "1.8", "8.7", "bash")
                .row("1004", "admin", "0.3", "5.2", "vim")
                .row("1005", "root", "0.1", "2.1", "cron")
                .row("1006", "admin", "12.5", "45.0", "java -jar app.jar")
                .widths(
                        Constraint.length(8),
                        Constraint.length(8),
                        Constraint.length(7),
                        Constraint.length(7),
                        Constraint.fill()
                )
                .highlightColor(Color.BLUE)
                .title("Process List")
                .rounded()
                .fill();
    }

    private static Element statCard(String label, String value, Color color, String detail) {
        return panel(() -> column(
                text(label).bold().fg(color).length(1),
                text(value).bold().length(1),
                text(detail).dim().length(1)
        ))
        .rounded()
        .borderColor(color)
        .fill();
    }
}
