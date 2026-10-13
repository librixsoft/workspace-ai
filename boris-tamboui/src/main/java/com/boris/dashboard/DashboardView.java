package com.boris.dashboard;

import static dev.tamboui.toolkit.Toolkit.*;
import static dev.tamboui.tui.Keys.isQuit;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Redesigned dashboard using TamboUI Toolkit DSL.
 * Features:
 * - Animated header with system info panel
 * - Metric cards with sparkline trends
 * - Widget-based gauges with color coding
 * - Real-time process table
 * - Activity log with List widget
 * - Alert panel with conditional styling
 */
public class DashboardView extends ToolkitApp {
    
    private final Random random = new Random();
    private int frame = 0;
    
    // Dynamic metrics
    private int cpuValue = 24;
    private int memValue = 62;
    private int diskValue = 45;
    private int netValue = 12;
    private int ioValue = 28;
    
    // Sparkline history for trends
    private int[] cpuHistory = new int[20];
    private int[] memHistory = new int[20];
    private int[] diskHistory = new int[20];
    
    // Process data
    private final String[][] processData = {
        {"PID", "USER", "CPU%", "MEM%", "COMMAND"},
        {"1234", "root", "45.2", "3.1", "java"},
        {"5678", "www-data", "12.1", "1.8", "nginx"},
        {"9012", "postgres", "8.5", "5.4", "postgres"},
        {"3456", "redis", "2.3", "0.9", "redis-server"},
        {"7890", "admin", "1.2", "2.1", "node"}
    };
    
    // Activity log
    private final String[] logEntries = {
        "[INFO] Application started successfully",
        "[WARN] High memory usage detected",
        "[ERROR] Connection timeout - retrying",
        "[INFO] Cache refreshed",
        "[DEBUG] Processing batch operations",
        "[INFO] Health check passed",
        "[WARN] Slow response time: 2.3s",
        "[INFO] New deployment detected"
    };
    
    // Alerts
    private final String[] alerts = {
        "⚠ Memory usage above 80%",
        "⚠ Disk I/O latency high",
        "✓ All services healthy",
        "⚠ Connection pool exhausted"
    };
    
    public DashboardView() {
        // Initialize history
        for (int i = 0; i < cpuHistory.length; i++) {
            cpuHistory[i] = cpuValue;
            memHistory[i] = memValue;
            diskHistory[i] = diskValue;
        }
    }
    
    @Override
    protected Element render() {
        updateFrame();
        
        return column(
            renderHeader(),
            spacer(),
            renderMetricsPanel(),
            spacer(),
            renderGaugesAndTrends(),
            spacer(),
            renderBottomSection(),
            spacer(),
            renderFooter()
        );
    }
    
    private void updateFrame() {
        frame++;
        
        // Update metrics with random walk
        cpuValue = Math.max(5, Math.min(95, cpuValue + random.nextInt(7) - 3));
        memValue = Math.max(20, Math.min(90, memValue + random.nextInt(5) - 2));
        diskValue = Math.max(30, Math.min(80, diskValue + random.nextInt(3) - 1));
        netValue = Math.max(1, Math.min(50, netValue + random.nextInt(5) - 2));
        ioValue = Math.max(5, Math.min(90, ioValue + random.nextInt(10) - 5));
        
        // Shift history
        shiftHistory(cpuHistory, cpuValue);
        shiftHistory(memHistory, memValue);
        shiftHistory(diskHistory, diskValue);
    }
    
    private void shiftHistory(int[] history, int value) {
        for (int i = 0; i < history.length - 1; i++) {
            history[i] = history[i + 1];
        }
        history[history.length - 1] = value;
    }
    
    private Element renderHeader() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        return panel(
            text(" ◈ TAMBOUI DASHBOARD v2.0 ").bold(),
            text(timestamp).dim()
        ).rounded();
    }
    
    private Element renderMetricsPanel() {
        return row(
            renderMetricCard("CPU", cpuValue, cpuHistory, true),
            spacer(),
            renderMetricCard("MEM", memValue, memHistory, true),
            spacer(),
            renderMetricCard("DISK", diskValue, diskHistory, true),
            spacer(),
            renderSimpleMetric("NET", netValue + " MB/s")
        );
    }
    
    private Element renderMetricCard(String title, int value, int[] history, boolean showGauge) {
        String borderColor = value > 80 ? "red" : (value > 50 ? "yellow" : "cyan");
        
        return panel(
            row(
                text(title).bold(),
                spacer(),
                text(value + "%").bold().onGreen()
            ).padding(1),
            gauge(value / 100.0)
                .gaugeColor("green")
                .rounded(),
            sparkline(history)
                .color("cyan")
        )
        .borderColor(borderColor)
        .rounded();
    }
    
    private Element renderSimpleMetric(String title, String value) {
        return panel(
            row(
                text(title).bold(),
                spacer(),
                text("↑ " + value).cyan().bold()
            ).padding(1),
            gauge(0.5)
                .gaugeColor("cyan")
                .rounded()
        )
        .borderColor("cyan")
        .rounded();
    }
    
    private Element renderGaugesAndTrends() {
        return row(
            panel(
                text("PERFORMANCE GAUGES").bold(),
                spacer(),
                renderGaugeRow("CPU", cpuValue),
                renderGaugeRow("MEM", memValue),
                renderGaugeRow("IO", ioValue),
                renderGaugeRow("NET", netValue)
            ).borderColor("magenta").rounded(),
            spacer(),
            panel(
                text("ACTIVITY LOG").bold(),
                spacer(),
                list(logEntries)
                    .highlightColor("cyan")
            ).borderColor("magenta").rounded()
        );
    }
    
    private Element renderGaugeRow(String label, int value) {
        String gaugeColor = value > 80 ? "red" : (value > 50 ? "yellow" : "green");
        return row(
            text(label + ":").width(6),
            gauge(value / 100.0)
                .label(value + "%")
                .gaugeColor(gaugeColor)
                .width(20),
            spacer()
        ).padding(1);
    }
    
    private Element renderBottomSection() {
        return row(
            panel(
                text("PROCESSES").bold(),
                spacer(),
                table()
                    .header("PID", "USER", "CPU%", "MEM%", "COMMAND")
                    .row("1234", "root", "45.2", "3.1", "java")
                    .row("5678", "www-data", "12.1", "1.8", "nginx")
                    .row("9012", "postgres", "8.5", "5.4", "postgres")
                    .row("3456", "redis", "2.3", "0.9", "redis-server")
                    .row("7890", "admin", "1.2", "2.1", "node")
            ).borderColor("green").rounded(),
            spacer(),
            panel(
                text("ALERTS").bold(),
                spacer(),
                column(
                    text("⚠ Memory usage above 80%").yellow(),
                    text("⚠ Disk I/O latency high").yellow(),
                    text("✓ All services healthy").green(),
                    text("⚠ Connection pool exhausted").red()
                ).padding(1)
            ).borderColor("green").rounded()
        );
    }
    
    private Element renderFooter() {
        return text("[q] Quit  [Space] Pause  [Tab] Navigate").dim().alignCenter();
    }
    
    public static void main(String[] args) throws Exception {
        new DashboardView().run();
    }
}
