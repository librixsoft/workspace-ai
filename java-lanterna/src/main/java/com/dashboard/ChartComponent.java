package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class ChartComponent extends AbstractComponent<ChartComponent> {

    public enum ChartType { BAR_CHART, LINE_CHART, DONUT_CHART }

    private final ChartType type;
    private final String title;
    private final String[] labels;
    private final int[][] datasets;
    private final String[] datasetLabels;
    private final TextColor[] colors;
    private final TextColor titleColor;
    private final TextColor axisColor;
    private final TextColor labelColor;
    private final TextColor gridColor;
    private final int maxValue;
    private final boolean showGrid;
    private final boolean showValues;

    public ChartComponent(ChartType type, String title, String[] labels, int[][] datasets,
                          String[] datasetLabels, TextColor[] colors) {
        this.type = type;
        this.title = title;
        this.labels = labels;
        this.datasets = datasets;
        this.datasetLabels = datasetLabels;
        this.colors = colors;
        this.titleColor = new TextColor.RGB(0x88, 0x88, 0x88);
        this.axisColor = new TextColor.RGB(0x55, 0x55, 0x55);
        this.labelColor = new TextColor.RGB(0x99, 0x99, 0x99);
        this.gridColor = new TextColor.RGB(0x33, 0x33, 0x33);
        this.maxValue = calculateMaxValue();
        this.showGrid = true;
        this.showValues = true;
        setPreferredSize(new TerminalSize(55, 18));
    }

    public ChartComponent(ChartType type, String title, int[] values, String[] labels, TextColor color) {
        this.type = type;
        this.title = title;
        this.labels = labels;
        this.datasets = new int[][]{values};
        this.datasetLabels = new String[]{""};
        this.colors = new TextColor[]{color};
        this.titleColor = new TextColor.RGB(0x88, 0x88, 0x88);
        this.axisColor = new TextColor.RGB(0x55, 0x55, 0x55);
        this.labelColor = new TextColor.RGB(0x99, 0x99, 0x99);
        this.gridColor = new TextColor.RGB(0x33, 0x33, 0x33);
        this.maxValue = values.length > 0 ? max(values) : 1;
        this.showGrid = true;
        this.showValues = true;
        setPreferredSize(new TerminalSize(55, 18));
    }

    public ChartComponent(ChartType type, String title, int[] values, TextColor[] colors) {
        this.type = type;
        this.title = title;
        this.labels = new String[]{"", "", "", "", "", "", "", ""};
        this.datasets = new int[][]{values};
        this.datasetLabels = new String[]{""};
        this.colors = colors;
        this.titleColor = new TextColor.RGB(0x88, 0x88, 0x88);
        this.axisColor = new TextColor.RGB(0x55, 0x55, 0x55);
        this.labelColor = new TextColor.RGB(0x99, 0x99, 0x99);
        this.gridColor = new TextColor.RGB(0x33, 0x33, 0x33);
        this.maxValue = values.length > 0 ? max(values) : 1;
        this.showGrid = false;
        this.showValues = false;
        setPreferredSize(new TerminalSize(40, 12));
    }

    public ChartComponent(ChartType type, String title, int[] values, String[] labels, TextColor[] colors) {
        this.type = type;
        this.title = title;
        this.labels = labels != null ? labels : new String[]{"", "", "", "", "", "", "", ""};
        this.datasets = new int[][]{values};
        this.datasetLabels = labels != null ? labels : new String[]{""};
        this.colors = colors;
        this.titleColor = new TextColor.RGB(0x88, 0x88, 0x88);
        this.axisColor = new TextColor.RGB(0x55, 0x55, 0x55);
        this.labelColor = new TextColor.RGB(0x99, 0x99, 0x99);
        this.gridColor = new TextColor.RGB(0x33, 0x33, 0x33);
        this.maxValue = values.length > 0 ? max(values) : 1;
        this.showGrid = false;
        this.showValues = false;
        setPreferredSize(new TerminalSize(40, 12));
    }

    private int calculateMaxValue() {
        int max = 0;
        for (int[] dataset : datasets) {
            for (int v : dataset) {
                if (v > max) max = v;
            }
        }
        return Math.max(max, 1);
    }

    private int max(int[] arr) {
        int m = 0;
        for (int v : arr) if (v > m) m = v;
        return Math.max(m, 1);
    }

    @Override
    protected ComponentRenderer<ChartComponent> createDefaultRenderer() {
        return new ComponentRenderer<ChartComponent>() {
            @Override
            public TerminalSize getPreferredSize(ChartComponent component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, ChartComponent component) {
                component.drawChart(graphics);
            }
        };
    }

    private void drawChart(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 5) return;

        int titleY = 0;
        int chartTop = 3;
        int chartBottom = h - 2;
        int chartLeft = 2;
        int chartRight = w - 2;
        int chartHeight = chartBottom - chartTop;
        int chartWidth = chartRight - chartLeft;

        g.putString(1, titleY, " " + title + " ");
        g.setForegroundColor(axisColor);

        if (type == ChartType.BAR_CHART) {
            drawBarChart(g, chartLeft, chartTop, chartWidth, chartHeight);
        } else if (type == ChartType.LINE_CHART) {
            drawLineChart(g, chartLeft, chartTop, chartWidth, chartHeight);
        } else if (type == ChartType.DONUT_CHART) {
            drawDonutChart(g, w, h);
        }

        if (datasetLabels != null && datasetLabels.length > 0 && datasetLabels[0].length() > 0) {
            drawLegend(g, chartRight - 18, chartBottom + 1);
        }
    }

    private void drawBarChart(TextGUIGraphics g, int left, int top, int width, int height) {
        int n = labels.length;
        if (n == 0) return;

        int barAreaWidth = Math.max(width - 4, 4);
        int barWidth = Math.max(1, barAreaWidth / (n * 2));
        int gap = Math.max(1, barAreaWidth / (n * 3));
        int totalSlot = barWidth + gap;

        g.setForegroundColor(gridColor);
        for (int row = 1; row < height; row += 2) {
            int y = top + row;
            if (y >= top + height - 1) break;
            g.drawLine(left, y, left + barAreaWidth, y, '-');
        }

        g.setForegroundColor(axisColor);
        g.drawLine(left, top + height, left + barAreaWidth, top + height, '-');

        int[] barHeights = new int[n];
        for (int i = 0; i < n; i++) {
            int val = datasets[0][i];
            barHeights[i] = maxValue > 0 ? (val * (height - 2)) / maxValue : 0;
            barHeights[i] = Math.min(barHeights[i], height - 2);
        }

        for (int i = 0; i < n; i++) {
            int x = left + 1 + i * totalSlot;
            int barH = barHeights[i];
            if (barH <= 0) continue;

            int barTop = top + height - 1 - barH;
            g.setForegroundColor(colors[0]);
            for (int row = 0; row < barH; row++) {
                int drawY = barTop + row;
                for (int col = 0; col < barWidth; col++) {
                    if (x + col < left + barAreaWidth) {
                        g.setCharacter(x + col, drawY, '█');
                    }
                }
            }

            if (showValues && barH > 2) {
                g.setForegroundColor(colors[0]);
                g.putString(x + barWidth / 2 - 1, barTop, " " + datasets[0][i] + " ");
            }

            g.setForegroundColor(labelColor);
            String label = labels[i];
            if (label.length() > barWidth) label = label.substring(0, Math.min(barWidth, 6));
            g.putString(x, top + height + 1, " " + label);
        }

        g.putString(left - 3, top + 1, "50");
        g.putString(left - 3, top + height / 2, "25");
        g.putString(left - 3, top + height - 2, "0");
    }

    private void drawLineChart(TextGUIGraphics g, int left, int top, int width, int height) {
        int n = labels.length;
        if (n == 0) return;

        int plotWidth = Math.max(width - 4, 4);
        int plotHeight = Math.max(height - 2, 2);

        g.setForegroundColor(gridColor);
        for (int row = 1; row < plotHeight; row += 2) {
            int y = top + row;
            if (y >= top + plotHeight - 1) break;
            g.drawLine(left, y, left + plotWidth, y, '-');
        }

        g.setForegroundColor(axisColor);
        g.drawLine(left, top + plotHeight, left + plotWidth, top + plotHeight, '-');

        int[] points = new int[n];
        for (int i = 0; i < n; i++) {
            points[i] = maxValue > 0 ? (datasets[0][i] * (plotHeight - 2)) / maxValue : 0;
            points[i] = Math.min(points[i], plotHeight - 2);
        }

        for (int ds = 0; ds < datasets.length && ds < colors.length; ds++) {
            g.setForegroundColor(colors[ds]);
            for (int i = 0; i < n - 1; i++) {
                int x1 = left + 1 + (i * plotWidth) / Math.max(n - 1, 1);
                int x2 = left + 1 + ((i + 1) * plotWidth) / Math.max(n - 1, 1);
                int y1 = top + plotHeight - 1 - points[i];
                int y2 = top + plotHeight - 1 - points[i + 1];
                drawLineBresenham(g, x1, y1, x2, y2, '·');
            }

            for (int i = 0; i < n; i++) {
                int x = left + 1 + (i * plotWidth) / Math.max(n - 1, 1);
                int y = top + plotHeight - 1 - points[i];
                if (y >= top && y < top + plotHeight) {
                    g.setForegroundColor(colors[ds]);
                    g.setCharacter(x, y, '●');
                }
            }
        }

        g.setForegroundColor(labelColor);
        for (int i = 0; i < n; i++) {
            int x = left + 1 + (i * plotWidth) / Math.max(n - 1, 1);
            if (x < left + plotWidth) {
                g.putString(x, top + plotHeight + 1, labels[i]);
            }
        }

        g.putString(left - 3, top + 1, "50");
        g.putString(left - 3, top + plotHeight / 2, "25");
        g.putString(left - 3, top + plotHeight - 2, "0");
    }

    private void drawDonutChart(TextGUIGraphics g, int w, int h) {
        int cx = w / 2;
        int cy = h / 2;
        int radius = Math.min(w, h) / 3;
        radius = Math.min(radius, 8);
        radius = Math.max(radius, 3);

        int total = 0;
        for (int v : datasets[0]) total += v;
        if (total == 0) return;

        int startAngle = 0;
        for (int i = 0; i < datasets[0].length && i < colors.length; i++) {
            int angle = (datasets[0][i] * 360) / total;
            g.setForegroundColor(colors[i]);
            drawArc(g, cx, cy, radius, startAngle, startAngle + angle);
            startAngle += angle;
        }

        g.setBackgroundColor(null);
        g.setForegroundColor(null);
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) {
                    int valPct = total > 0 ? (datasets[0][0] * 100) / total : 0;
                    g.putString(cx - 1, cy, valPct + "%");
                } else {
                    g.setCharacter(cx + dx, cy + dy, ' ');
                }
            }
        }

        if (cy + 3 < h) {
            g.setForegroundColor(new TextColor.RGB(0x66, 0x66, 0x66));
            g.putString(cx - 4, cy + 3, "Total: " + total);
        }
    }

    private void drawArc(TextGUIGraphics g, int cx, int cy, int r, int start, int end) {
        for (int angle = start; angle < end; angle++) {
            double rad = Math.toRadians(angle);
            int x1 = cx + (int) (r * Math.cos(rad));
            int y1 = cy - (int) (r * Math.sin(rad));
            int x2 = cx + (int) ((r - 1) * Math.cos(rad));
            int y2 = cy - (int) ((r - 1) * Math.sin(rad));
            g.setForegroundColor(g.getForegroundColor());
            if (x1 >= 0 && x1 < g.getSize().getColumns() && y1 >= 0 && y1 < g.getSize().getRows()) {
                g.setCharacter(x1, y1, '█');
            }
            if (x2 >= 0 && x2 < g.getSize().getColumns() && y2 >= 0 && y2 < g.getSize().getRows()) {
                g.setCharacter(x2, y2, '█');
            }
        }
    }

    private void drawLineBresenham(TextGUIGraphics g, int x1, int y1, int x2, int y2, char c) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        int x = x1, y = y1;
        while (true) {
            if (x >= 0 && x < g.getSize().getColumns() && y >= 0 && y < g.getSize().getRows()) {
                g.setCharacter(x, y, c);
            }
            if (x == x2 && y == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 < dx) { err += dx; y += sy; }
        }
    }

    private void drawLegend(TextGUIGraphics g, int x, int y) {
        for (int i = 0; i < datasetLabels.length && i < colors.length; i++) {
            if (y + i < g.getSize().getRows()) {
                g.setForegroundColor(colors[i]);
                g.putString(x, y + i, "■ " + datasetLabels[i]);
            }
        }
    }
}
