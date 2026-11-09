package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class VolumeControl extends AbstractComponent<VolumeControl> {

    private final int level;
    private final TextColor fillColor;
    private final TextColor bgColor;
    private final TextColor textColor;

    public VolumeControl(int level) {
        this.level = Math.max(0, Math.min(100, level));
        this.fillColor = MusicPlayerTheme.COLOR_PRIMARY;
        this.bgColor = new TextColor.RGB(0x33, 0x33, 0x44);
        this.textColor = MusicPlayerTheme.COLOR_TEXT_SECONDARY;
        setPreferredSize(new TerminalSize(40, 3));
    }

    @Override
    protected ComponentRenderer<VolumeControl> createDefaultRenderer() {
        return new ComponentRenderer<VolumeControl>() {
            @Override
            public TerminalSize getPreferredSize(VolumeControl component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, VolumeControl component) {
                component.drawVolume(graphics);
            }
        };
    }

    private void drawVolume(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 3) return;

        g.setForegroundColor(textColor);
        g.putString(0, 0, " VOLUME ");

        int barWidth = Math.max(10, w - 20);
        int barStart = 5;
        int barY = 1;

        g.setForegroundColor(bgColor);
        for (int i = barStart; i < barStart + barWidth; i++) {
            if (i < w - 1) {
                g.setCharacter(i, barY, ' ');
            }
        }
        g.setBackgroundColor(null);

        int filledWidth = (barWidth * level) / 100;
        g.setForegroundColor(fillColor);
        for (int i = 0; i < filledWidth && (barStart + i) < w - 1; i++) {
            g.setCharacter(barStart + i, barY, '█');
        }

        g.setForegroundColor(textColor);
        g.putString(barStart + barWidth + 2, barY, level + "%");
    }
}
