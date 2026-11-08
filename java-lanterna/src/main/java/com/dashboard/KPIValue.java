package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class KPIValue extends AbstractComponent<KPIValue> {

    private final String title;
    private final String value;
    private final String trend;
    private final boolean trendUp;
    private final TextColor valueColor;
    private final TextColor titleColor;
    private final TextColor borderColor;

    private static final TextColor COLOR_CYAN = new TextColor.RGB(0x00, 0xff, 0xff);
    private static final TextColor COLOR_GREEN = new TextColor.RGB(0x00, 0xff, 0x6e);
    private static final TextColor COLOR_RED = new TextColor.RGB(0xff, 0x55, 0x55);
    private static final TextColor COLOR_GRAY = new TextColor.RGB(0x88, 0x88, 0x88);
    private static final TextColor BORDER_COLOR = new TextColor.RGB(0x33, 0x33, 0x33);

    public KPIValue(String title, String value, String trend, boolean trendUp, TextColor valueColor) {
        this.title = title;
        this.value = value;
        this.trend = trend;
        this.trendUp = trendUp;
        this.valueColor = valueColor;
        this.titleColor = COLOR_GRAY;
        this.borderColor = BORDER_COLOR;
        setPreferredSize(new TerminalSize(30, 10));
    }

    @Override
    protected ComponentRenderer<KPIValue> createDefaultRenderer() {
        return new ComponentRenderer<KPIValue>() {
            @Override
            public TerminalSize getPreferredSize(KPIValue component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, KPIValue component) {
                component.drawKPI(graphics);
            }
        };
    }

    private void drawKPI(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 5) return;

        g.setForegroundColor(borderColor);
        g.drawRectangle(new TerminalPosition(0, 0), new TerminalSize(w, h), '░');

        g.putString(1, 1, title.toUpperCase());

        g.setForegroundColor(valueColor);
        int valueY = h / 2 - 1;
        g.putString(1, valueY, value);

        TextColor trendColor = trendUp ? COLOR_GREEN : COLOR_RED;
        g.setForegroundColor(trendColor);
        g.putString(1, h - 3, " " + trend);

        String sparkline = generateSparkline();
        g.putString(1, h - 2, sparkline);
    }

    private String generateSparkline() {
        StringBuilder sb = new StringBuilder();
        int[] bars = {2, 3, 1, 4, 2, 5, 3, 4, 2, 5, 3, 4};
        for (int bar : bars) {
            int chars = Math.max(1, bar);
            for (int i = 0; i < chars; i++) {
                sb.append('▌');
            }
            sb.append(' ');
        }
        String result = sb.toString().trim();
        while (result.length() < 24) result += ' ';
        return result.substring(0, Math.min(24, result.length()));
    }
}
