package dev.tamboui.dashboard;

import dev.tamboui.tui.TuiConfig;
import dev.tamboui.tui.TuiRunner;
import dev.tamboui.tui.EventHandler;
import dev.tamboui.tui.event.Event;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.terminal.Frame;
import dev.tamboui.widgets.chart.Chart;
import dev.tamboui.widgets.chart.Dataset;
import dev.tamboui.widgets.chart.GraphType;
import dev.tamboui.widgets.gauge.Gauge;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.barchart.BarChart;
import dev.tamboui.widgets.barchart.BarGroup;
import dev.tamboui.widgets.barchart.Bar;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;

public class DashboardApp implements EventHandler {

    private static final Style HEADER_STYLE = Style.EMPTY.fg(Color.WHITE).bg(Color.BLUE);
    private static final Style LABEL_STYLE = Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY);
    private static final Style VALUE_GREEN = Style.EMPTY.fg(Color.GREEN).bg(Color.DARK_GRAY);
    private static final Style VALUE_YELLOW = Style.EMPTY.fg(Color.YELLOW).bg(Color.DARK_GRAY);
    private static final Style VALUE_RED = Style.EMPTY.fg(Color.RED).bg(Color.DARK_GRAY);
    private static final Style VALUE_CYAN = Style.EMPTY.fg(Color.CYAN).bg(Color.DARK_GRAY);
    private static final Style FOOTER_STYLE = Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY);

    @Override
    public boolean handle(Event event, TuiRunner runner) {
        if (event instanceof KeyEvent keyEvent) {
            if (keyEvent.character() == 'Q' && keyEvent.hasAlt()) {
                runner.quit();
                return true;
            }
        }
        return false;
    }

    public void render(Frame frame) {
        Buffer buffer = frame.buffer();
        Rect area = frame.area();
        int w = area.width();
        int h = area.height();

        // Header bar
        Rect headerRect = new Rect(0, 0, w, 1);
        buffer.setStyle(headerRect, Style.EMPTY.fg(Color.WHITE).bg(Color.BLUE));
        buffer.setString(2, 1, "  TAMBO UI DASHBOARD  ",
            Style.EMPTY.fg(Color.WHITE).bg(Color.BLUE));

        // Metrics row
        int metricY = 3;
        int quarterWidth = w / 4;

        // Metric 1: CPU
        Block m1 = Block.bordered();
        Rect m1Area = new Rect(0, metricY, quarterWidth, 5);
        m1.render(m1Area, buffer);
        buffer.setString(2, metricY + 1, "CPU", Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY));
        buffer.setString(2, metricY + 3, "72%", Style.EMPTY.fg(Color.GREEN).bg(Color.DARK_GRAY));

        // Metric 2: MEM
        Block m2 = Block.bordered();
        Rect m2Area = new Rect(quarterWidth + 1, metricY, quarterWidth, 5);
        m2.render(m2Area, buffer);
        buffer.setString(quarterWidth + 3, metricY + 1, "MEM", Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY));
        buffer.setString(quarterWidth + 3, metricY + 3, "4.2 GB", Style.EMPTY.fg(Color.YELLOW).bg(Color.DARK_GRAY));

        // Metric 3: DISK
        Block m3 = Block.bordered();
        Rect m3Area = new Rect((quarterWidth + 1) * 2, metricY, quarterWidth, 5);
        m3.render(m3Area, buffer);
        buffer.setString((quarterWidth + 1) * 2 + 2, metricY + 1, "DISK", Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY));
        buffer.setString((quarterWidth + 1) * 2 + 2, metricY + 3, "68%", Style.EMPTY.fg(Color.RED).bg(Color.DARK_GRAY));

        // Metric 4: NET
        Block m4 = Block.bordered();
        Rect m4Area = new Rect((quarterWidth + 1) * 3 + 1, metricY, quarterWidth, 5);
        m4.render(m4Area, buffer);
        buffer.setString((quarterWidth + 1) * 3 + 3, metricY + 1, "NET", Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY));
        buffer.setString((quarterWidth + 1) * 3 + 3, metricY + 3, "1.2 Gbps", Style.EMPTY.fg(Color.CYAN).bg(Color.DARK_GRAY));

        // Main content area
        int contentStartY = 9;
        int contentHeight = h - contentStartY - 2;
        int halfWidth = w / 2;

        // Left: Chart
        double[][] cpuChartData = new double[][]{
            new double[]{0, 30, 1, 45, 2, 35, 3, 50, 4, 40, 5, 60, 6, 55, 7, 70, 8, 45, 9, 65, 10, 50, 11, 72}
        };

        Dataset cpuDataset = Dataset.builder()
            .name("CPU Usage")
            .data(cpuChartData)
            .graphType(GraphType.LINE)
            .style(Style.EMPTY.fg(Color.GREEN))
            .build();

        Chart chart = Chart.builder()
            .datasets(cpuDataset)
            .build();

        Rect chartArea = new Rect(0, contentStartY, halfWidth, contentHeight);
        Block chartBlock = Block.bordered();
        chartBlock.render(chartArea, buffer);
        Rect chartInner = chartBlock.inner(chartArea);
        chart.render(chartInner, buffer);

        // Right: Gauges
        int gaugeY = contentStartY + 2;
        int gaugeWidth = halfWidth - 2;
        int gaugeX = halfWidth + 1;

        // CPU Gauge
        Block gauge1 = Block.bordered();
        Rect gauge1Area = new Rect(gaugeX, gaugeY, gaugeWidth, 6);
        gauge1.render(gauge1Area, buffer);
        Gauge cpuGauge = Gauge.builder()
            .percent(72)
            .label("72%")
            .gaugeStyle(Style.EMPTY.fg(Color.GREEN))
            .build();
        cpuGauge.render(gauge1Area, buffer);

        // Memory Gauge
        Block gauge2 = Block.bordered();
        Rect gauge2Area = new Rect(gaugeX, gaugeY + 7, gaugeWidth, 6);
        gauge2.render(gauge2Area, buffer);
        Gauge memGauge = Gauge.builder()
            .percent(65)
            .label("65%")
            .gaugeStyle(Style.EMPTY.fg(Color.YELLOW))
            .build();
        memGauge.render(gauge2Area, buffer);

        // Bottom: Bar Chart
        int barY = contentStartY + contentHeight - 4;
        int barWidth = w - 2;

        BarChart barChart = BarChart.builder()
            .data(
                BarGroup.of("Q1", Bar.of(100, "Jan")),
                BarGroup.of("Q2", Bar.of(150, "Feb")),
                BarGroup.of("Q3", Bar.of(130, "Mar")),
                BarGroup.of("Q4", Bar.of(180, "Apr"))
            )
            .barStyle(Style.EMPTY.fg(Color.CYAN))
            .build();

        Rect barArea = new Rect(1, barY, barWidth, 4);
        Block barBlock = Block.bordered();
        barBlock.render(barArea, buffer);
        Rect barInner = barBlock.inner(barArea);
        barChart.render(barInner, buffer);

        // Footer
        Rect footerRect = new Rect(0, h - 1, w, 1);
        buffer.setStyle(footerRect, Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY));
        buffer.setString(2, h - 1, " ESC: Exit | TAMBO UI DASHBOARD v1.0 ",
            Style.EMPTY.fg(Color.WHITE).bg(Color.DARK_GRAY));
    }

    public static void main(String[] args) throws Exception {
        TuiConfig config = TuiConfig.builder().build();
        try (TuiRunner tui = TuiRunner.create(config)) {
            DashboardApp app = new DashboardApp();
            tui.run(app, app::render);
        }
    }
}
