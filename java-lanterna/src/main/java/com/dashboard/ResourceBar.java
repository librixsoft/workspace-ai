package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class ResourceBar extends AbstractComponent<ResourceBar> {

    private final String label;
    private final String value;
    private final int percentage;
    private final TextColor labelColor;
    private final TextColor valueColor;
    private final TextColor barColor;
    private final TextColor bgColor;

    private static final TextColor COLOR_GRAY = new TextColor.RGB(0x88, 0x88, 0x88);
    private static final TextColor COLOR_GREEN = new TextColor.RGB(0x00, 0xff, 0x6e);
    private static final TextColor COLOR_YELLOW = new TextColor.RGB(0xff, 0xaa, 0x00);
    private static final TextColor COLOR_RED = new TextColor.RGB(0xff, 0x55, 0x55);
    private static final TextColor COLOR_DARK = new TextColor.RGB(0x22, 0x22, 0x22);

    public ResourceBar(String label, String value, int percentage) {
        this.label = label;
        this.value = value;
        this.percentage = Math.min(100, Math.max(0, percentage));
        this.labelColor = COLOR_GRAY;
        this.valueColor = getBarColor();
        this.barColor = getBarColor();
        this.bgColor = COLOR_DARK;
        setPreferredSize(new TerminalSize(40, 4));
    }

    private TextColor getBarColor() {
        if (percentage < 60) return COLOR_GREEN;
        if (percentage < 85) return COLOR_YELLOW;
        return COLOR_RED;
    }

    @Override
    protected ComponentRenderer<ResourceBar> createDefaultRenderer() {
        return new ComponentRenderer<ResourceBar>() {
            @Override
            public TerminalSize getPreferredSize(ResourceBar component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, ResourceBar component) {
                component.drawResource(graphics);
            }
        };
    }

    private void drawResource(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int barWidth = Math.max(10, w - 30);
        int barStart = w - barWidth - 10;
        int barY = 1;

        g.setForegroundColor(labelColor);
        g.putString(0, 0, label);

        g.setForegroundColor(valueColor);
        g.putString(0, barY, value);

        g.setBackgroundColor(bgColor);
        for (int i = barStart; i < barStart + barWidth; i++) {
            if (i < w - 1) {
                g.setCharacter(i, barY, ' ');
            }
        }
        g.setBackgroundColor(null);

        int filledWidth = (barWidth * percentage) / 100;
        g.setForegroundColor(barColor);
        for (int i = 0; i < filledWidth && (barStart + i) < w - 1; i++) {
            g.setCharacter(barStart + i, barY, '█');
        }

        g.putString(barStart + filledWidth + 1, barY, percentage + "%");
    }
}
