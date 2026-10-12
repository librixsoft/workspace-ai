package com.boris.dashboard;

import static dev.tamboui.toolkit.Toolkit.markupText;

import java.util.Random;

/**
 * Main dashboard view using the Tui layer directly.
 * Provides a complete terminal dashboard with:
 * - Animated header with system stats
 * - Sidebar navigation with active indicator
 * - Metric cards with status indicators
 * - Performance gauges with animated progress
 * - Real-time process table
 * - Activity log feed
 * - Alert panel
 * - Footer with controls
 */
public class DashboardView {
    
    private boolean running = true;
    private int frame = 0;
    private Random random = new Random();
    
    // Simulated dynamic values
    private int cpuValue = 24;
    private int memValue = 62;
    private int diskValue = 45;
    private int netValue = 12;
    private int ioValue = 28;
    
    // Log entries
    private String[] logMessages = {
        "[INFO] Application started successfully",
        "[WARN] High memory usage detected",
        "[ERROR] Connection timeout - retrying",
        "[INFO] Cache refreshed",
        "[DEBUG] Processing batch operations",
        "[INFO] Health check passed",
        "[WARN] Slow response time: 2.3s",
        "[INFO] New deployment detected"
    };
    
    // Recent activities
    private String[] activities = {
        "User admin logged in",
        "Backup completed",
        "Config updated",
        "Alert triggered",
        "Service restarted"
    };
    
    public DashboardView() {
    }
    
    public void render() {
        try {
            renderLoop();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Reset terminal
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    
    private void renderLoop() throws Exception {
        while (running) {
            clearScreen();
            renderAll();
            
            // Update dynamic values
            updateValues();
            frame++;
            
            // Read user input
            int key = System.in.read();
            if (key == 'q' || key == 'Q') {
                running = false;
            }
            
            Thread.sleep(150);
        }
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private void updateValues() {
        cpuValue = Math.max(5, Math.min(95, cpuValue + random.nextInt(7) - 3));
        memValue = Math.max(20, Math.min(90, memValue + random.nextInt(5) - 2));
        diskValue = Math.max(30, Math.min(80, diskValue + random.nextInt(3) - 1));
        netValue = Math.max(1, Math.min(50, netValue + random.nextInt(5) - 2));
        ioValue = Math.max(5, Math.min(90, ioValue + random.nextInt(10) - 5));
    }
    
    private void renderAll() {
        renderHeader();
        renderContent();
        renderFooter();
    }
    
    private void renderHeader() {
        String timestamp = String.format("%02d:%02d:%02d", frame / 3600, (frame / 60) % 60, frame % 60);
        
        System.out.println();
        System.out.println(markupText("\033[47m\033[30m  ◈  [bold]TAMBOUI DASHBOARD[/] [dim]v2.0[/]  " + timestamp + "  \033[49m\033[0m"));
        System.out.println(markupText("\033[1m┌──────────────────────────────────────────────────────────────────────────────────────┐[/]"));
        System.out.println();
    }
    
    private void renderContent() {
        renderTopRow();
        renderMiddleRow();
        renderBottomRow();
        System.out.println();
    }
    
    private void renderTopRow() {
        // Left: Navigation
        renderNavigation();
        
        // Right: Metric cards
        System.out.print("    ");
        renderMetricCards();
        
        System.out.println();
    }
    
    private void renderNavigation() {
        System.out.println(markupText("\033[1;36m[bold]▣ NAVIGATION[/]"));
        System.out.println();
        System.out.println(markupText("\033[1;32m▶[/] [bold]Overview[/] [dim]Active[/]"));
        System.out.println(markupText("  ○ Metrics[/]"));
        System.out.println(markupText("  ○ Logs[/]"));
        System.out.println(markupText("  ○ Settings[/]"));
        System.out.println(markupText("  ○ Alerts[/]"));
        System.out.println();
    }
    
    private void renderMetricCards() {
        System.out.println(markupText("\033[1m[bold]METRICS[/]"));
        System.out.println();
        
        // Card 1: CPU
        String cpuColor = cpuValue > 80 ? "\033[31m" : (cpuValue > 50 ? "\033[33m" : "\033[32m");
        System.out.print(cpuColor + "  CPU " + cpuValue + "%  ");
        
        // Card 2: MEM  
        String memColor = memValue > 80 ? "\033[31m" : (memValue > 50 ? "\033[33m" : "\033[32m");
        System.out.print(memColor + "  MEM " + memValue + "%  ");
        
        // Card 3: DISK
        String diskColor = diskValue > 80 ? "\033[31m" : (diskValue > 50 ? "\033[33m" : "\033[32m");
        System.out.print(diskColor + "  DISK " + diskValue + "%  ");
        
        // Card 4: NET
        System.out.print("\033[36m  NET ↑" + netValue + "MB[/]");
        
        System.out.println();
    }
    
    private void renderMiddleRow() {
        // Left: Gauges
        renderGauges();
        
        // Right: Activity log
        System.out.print("    ");
        renderActivityLog();
        
        System.out.println();
    }
    
    private void renderGauges() {
        System.out.println(markupText("\033[1m[bold]PERFORMANCE[/]"));
        System.out.println();
        
        printGauge("CPU", cpuValue, cpuValue > 80 ? "\033[31m" : (cpuValue > 50 ? "\033[33m" : "\033[32m"));
        printGauge("MEM", memValue, memValue > 80 ? "\033[31m" : (memValue > 50 ? "\033[33m" : "\033[32m"));
        printGauge("IO", ioValue, "\033[36m");
        printGauge("NET", netValue, "\033[35m");
        
        System.out.println();
    }
    
    private void printGauge(String label, int value, String color) {
        System.out.print("  [bold]" + label + ":[/] ");
        
        int barWidth = 30;
        int filled = (value * barWidth) / 100;
        
        for (int i = 0; i < barWidth; i++) {
            if (i < filled) {
                System.out.print(color + "█");
            } else {
                System.out.print("░");
            }
        }
        
        System.out.print("\033[0m ");
        System.out.printf("[bold]%d%%[/]", value);
        System.out.println();
    }
    
    private void renderActivityLog() {
        System.out.println(markupText("\033[1m[bold]ACTIVITY LOG[/]"));
        System.out.println();
        
        int logIndex = frame % logMessages.length;
        for (int i = 0; i < 5; i++) {
            int idx = (logIndex - i + logMessages.length) % logMessages.length;
            String level = logMessages[idx].substring(0, 6);
            String msg = logMessages[idx].substring(7);
            
            String levelColor = "";
            if (level.contains("INFO")) levelColor = "\033[32m";
            else if (level.contains("WARN")) levelColor = "\033[33m";
            else if (level.contains("ERROR")) levelColor = "\033[31m";
            else if (level.contains("DEBUG")) levelColor = "\033[90m";
            
            System.out.println(levelColor + level + " " + msg + "\033[0m");
        }
        
        System.out.println();
    }
    
    private void renderBottomRow() {
        // Left: Process table
        renderProcessTable();
        
        // Right: Alerts panel
        System.out.print("    ");
        renderAlerts();
        
        System.out.println();
    }
    
    private void renderProcessTable() {
        System.out.println(markupText("\033[1m[bold]PROCESSES[/]"));
        System.out.println();
        
        // Table header
        System.out.printf("  [bold]%-20s[/] [bold]%-8s[/] [bold]%-8s[/] [bold]%-7s[/] [bold]%-7s[/] %n", 
                "NAME", "PID", "STATE", "CPU", "MEM");
        
        // Separator
        System.out.println("  " + "─".repeat(52));
        
        // Table rows with simulated values
        String[][] rows = {
            {"java", "1234", "RUN", String.valueOf(cpuValue) + "%", String.valueOf(memValue) + "%"},
            {"nginx", "5678", "RUN", "5%", "12%"},
            {"postgres", "9012", "SLEEP", "2%", "18%"},
            {"redis", "3456", "RUN", "8%", "10%"},
            {"tamboui", "7890", "RUN", "15%", "35%"}
        };
        
        for (String[] row : rows) {
            System.out.printf("  %-20s %-8s %-8s %-7s %-7s%n", 
                    row[0], row[1], row[2], row[3], row[4]);
        }
        
        System.out.println();
    }
    
    private void renderAlerts() {
        System.out.println(markupText("\033[1m[bold]ALERTS[/]"));
        System.out.println();
        
        String[] alerts = {
            "⚠ Memory approaching limit",
            "ℹ Service health OK",
            "⚡ Performance degraded",
            "✓ Backup completed"
        };
        
        for (int i = 0; i < 4; i++) {
            String icon = alerts[i].charAt(0) == '⚠' ? "\033[33m" : 
                          alerts[i].charAt(0) == '⚡' ? "\033[31m" :
                          alerts[i].charAt(0) == '✓' ? "\033[32m" : "\033[36m";
            String text = alerts[i].substring(2);
            System.out.println(icon + text + "\033[0m");
        }
        
        System.out.println();
    }
    
    private void renderFooter() {
        System.out.println(markupText("\033[1m└──────────────────────────────────────────────────────────────────────────────────────┘[/]"));
        System.out.println();
        System.out.println(markupText("\033[2mPress 'q' to exit | Java 21+ | TamboUI 0.5.0 | Auto-refresh: 150ms[/]"));
    }
}
