package com.example;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Construye el dashboard principal con métricas, gráfico de barras,
 * tabla de procesos y alertas.
 */
public class Dashboard {

    private final Panel rootPanel = new Panel();

    public Dashboard() {
        // Layout en grid: 2 columnas
        rootPanel.setLayoutManager(new GridLayout(2).withPadding(0));
    }

    public Window buildWindow() {
        BasicWindow window = new BasicWindow();
        window.setConfiguration(WindowConfiguration.withTitle("📊 Dashboard de Sistema"));
        window.setComponent(rootPanel.withBorder(Borders.singleLine(" Dashboard ")));

        // Header con título principal
        Panel header = new Panel();
        header.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        header.addComponent(new Label("╔══════════════════════════════════════════════════════════════════════╗").withStyle(com.googlecode.lanterna.SGR.BOLD).foregroundColor(TextColor.ANSI.CYAN));
        header.addComponent(new EmptySpace(new TerminalSize(0, 0)));
        rootPanel.addComponent(header);

        // Título central
        Panel titlePanel = new Panel();
        titlePanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        titlePanel.addComponent(new EmptySpace(new TerminalSize(0, 3)));
        titlePanel.addComponent(new Label("  SISTEMA DE MONITOREO - DASHBOARD ").withStyle(com.googlecode.lanterna.SGR.BOLD).foregroundColor(TextColor.ANSI.WHITE).backgroundColor(TextColor.ANSI.BLUE));
        titlePanel.addComponent(new EmptySpace(new TerminalSize(0, 3)));
        rootPanel.addComponent(titlePanel);

        // Fila de métricas principales
        Panel metricsPanel = new Panel();
        metricsPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        metricsPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        int[] values = DashboardApp.METRIC_DATA;
        TextColor[] colors = DashboardApp.METRIC_COLORS;
        String[] labels = DashboardApp.METRIC_LABELS;

        for (int i = 0; i < values.length; i++) {
            Panel metricBox = new Panel();
            metricBox.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            metricBox.addComponent(new EmptySpace(new TerminalSize(0, 1)));
            metricBox.addComponent(new Label("  " + labels[i]).foregroundColor(colors[i]).withStyle(com.googlecode.lanterna.SGR.BOLD));
            metricBox.addComponent(new EmptySpace(new TerminalSize(0, 0)));
            metricBox.addComponent(new Label("  " + values[i] + "%").foregroundColor(colors[i]).withStyle(com.googlecode.lanterna.SGR.BOLD));
            metricBox.addComponent(new EmptySpace(new TerminalSize(0, 1)));
            metricsPanel.addComponent(metricBox.withBorder(Borders.singleLine()).withMinimumSize(new TerminalSize(18, 6)));
            metricsPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        }
        metricsPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        rootPanel.addComponent(metricsPanel);

        // Separador
        Panel sep = new Panel();
        sep.addComponent(new Label("╚══════════════════════════════════════════════════════════════════════╝").foregroundColor(TextColor.ANSI.CYAN));
        rootPanel.addComponent(sep);

        // Panel izquierdo: gráfico de barras de CPU y Mem
        Panel leftPanel = new Panel();
        leftPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        leftPanel.addComponent(new Label("  Gráfico de Uso (CPU / Memoria)").foregroundColor(TextColor.ANSI.WHITE).withStyle(com.googlecode.lanterna.SGR.BOLD));
        leftPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        leftPanel.addComponent(new Label("  CPU:").foregroundColor(TextColor.ANSI.RED));
        leftPanel.addComponent(buildBarChart(DashboardApp.CPU_USAGE, TextColor.ANSI.RED, 40));
        leftPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        leftPanel.addComponent(new Label("  Memoria:").foregroundColor(TextColor.ANSI.YELLOW));
        leftPanel.addComponent(buildBarChart(DashboardApp.MEMORY_USAGE, TextColor.ANSI.YELLOW, 40));
        leftPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Panel derecho: tabla de procesos
        Panel rightPanel = new Panel();
        rightPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        rightPanel.addComponent(new Label("  Procesos Activos").foregroundColor(TextColor.ANSI.WHITE).withStyle(com.googlecode.lanterna.SGR.BOLD));
        rightPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        rightPanel.addComponent(buildProcessTable());
        rightPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Panel de alertas
        Panel alertPanel = new Panel();
        alertPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        alertPanel.addComponent(new Label("  Alertas Recientes").foregroundColor(TextColor.ANSI.WHITE).withStyle(com.googlecode.lanterna.SGR.BOLD));
        alertPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        for (int i = 0; i < DashboardApp.ALERTS.length; i++) {
            alertPanel.addComponent(new Label("    " + DashboardApp.ALERTS[i]).foregroundColor(DashboardApp.ALERT_COLORS[i]));
        }
        alertPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        // Split panel para procesos y alertas lado a lado
        Panel bottomRight = new Panel();
        bottomRight.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        bottomRight.addComponent(rightPanel);
        bottomRight.addComponent(new Separator());
        bottomRight.addComponent(alertPanel);

        // Split principal: gráfico izquierda, procesos+alertas derecha
        SplitPanel mainSplit = new SplitPanel(SplitPanel.Orientation.HORIZONTAL);
        mainSplit.addComponent(leftPanel);
        mainSplit.addComponent(bottomRight);

        rootPanel.addComponent(mainSplit);

        // Footer
        Panel footer = new Panel();
        footer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        footer.addComponent(new EmptySpace(new TerminalSize(0, 2)));
        footer.addComponent(new Label(" ESC: Salir | Tab: Navegar ").foregroundColor(TextColor.ANSI.WHITE).backgroundColor(TextColor.ANSI.BLUE).withStyle(com.googlecode.lanterna.SGR.BOLD));
        footer.addComponent(new EmptySpace(new TerminalSize(0, 2)));
        rootPanel.addComponent(footer);

        return window;
    }

    /**
     * Construye un gráfico de barras horizontal a partir de un array de valores.
     */
    private Panel buildBarChart(int[] values, TextColor color, int maxBarWidth) {
        Panel chart = new Panel();
        chart.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        chart.addComponent(new EmptySpace(new TerminalSize(0, 2)));

        for (int value : values) {
            // Barra
            StringBuilder bar = new StringBuilder("  ");
            int barLength = (value * maxBarWidth) / 100;
            for (int i = 0; i < barLength; i++) {
                bar.append("█");
            }
            for (int i = barLength; i < maxBarWidth; i++) {
                bar.append("░");
            }
            chart.addComponent(new Label(bar.toString()).foregroundColor(color));
            chart.addComponent(new Label(" ").foregroundColor(color));
            chart.addComponent(new Label(value + "%").foregroundColor(color));
            chart.addComponent(new EmptySpace(new TerminalSize(1, 0)));
        }
        return chart;
    }

    /**
     * Construye una tabla simple de procesos con formato ASCII.
     */
    private Panel buildProcessTable() {
        Panel table = new Panel();
        table.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Header
        table.addComponent(new Label("    PID      CPU     MEM    PROCE")
                .foregroundColor(TextColor.ANSI.WHITE)
                .withStyle(com.googlecode.lanterna.SGR.BOLD));
        table.addComponent(new Label("    ---      ---     ---    -------")
                .foregroundColor(TextColor.ANSI.CYAN));

        // Filas de procesos
        for (int i = 0; i < DashboardApp.PROCESS_NAMES.length; i++) {
            String row = String.format("    %6d  %4d%%  %4dMB  %s",
                    DashboardApp.PROCESS_PIDS[i],
                    DashboardApp.PROCESS_CPU[i],
                    DashboardApp.PROCESS_MEM[i],
                    DashboardApp.PROCESS_NAMES[i]);
            table.addComponent(new Label("    " + row)
                    .foregroundColor(TextColor.ANSI.GREEN));
        }

        return table;
    }
}
