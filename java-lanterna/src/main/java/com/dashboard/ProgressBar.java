package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class ProgressBar extends AbstractComponent<ProgressBar> {

    private final String label;
    private final int current;
    private final int total;
    private final TextColor fillColor;
    private final TextColor bgColor;
    private final TextColor textColor;

    public ProgressBar(String label, int current, int total) {
        this.label = label;
        this.current = Math.max(0, Math.min(current, total));
        this.total = Math.max(1, total);
        this.fillColor = MusicPlayerTheme.COLOR_PRIMARY;
        this.bgColor = new TextColor.RGB(0x33, 0x33, 0x44);
        this.textColor = MusicPlayerTheme.COLOR_TEXT_SECONDARY;
        setPreferredSize(new TerminalSize(60, 3));
    }

    @Override
    protected ComponentRenderer<ProgressBar> createDefaultRenderer() {
        return new ComponentRenderer<ProgressBar>() {
            @Override
            public TerminalSize getPreferredSize(ProgressBar component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, ProgressBar component) {
                component.drawProgress(graphics);
            }
        };
    }

    private void drawProgress(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 3) return;

        int barY = 1;
        int barHeight = 1;

        g.setForegroundColor(textColor);
        g.putString(0, 0, label);

        int progressWidth = Math.max(10, w - label.length() - 8);
        int barStart = label.length() + 2;

        g.setForegroundColor(bgColor);
        for (int i = barStart; i < barStart + progressWidth; i++) {
            if (i < w - 1) {
                g.setCharacter(i, barY, ' ');
            }
        }
        g.setBackgroundColor(null);

        int filledWidth = (progressWidth * current) / total;
        g.setForegroundColor(fillColor);
        for (int i = 0; i < filledWidth && (barStart + i) < w - 1; i++) {
            g.setCharacter(barStart + i, barY, '█');
        }

        String timeText = String.format("%d:%02d / %d:%02d", current / 60, current % 60, total / 60, total % 60);
        g.setForegroundColor(textColor);
        g.putString(barStart + progressWidth + 2, barY, timeText);
    }
}
