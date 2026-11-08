package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class SectionTitle extends AbstractComponent<SectionTitle> {

    private final String title;
    private final TextColor color;

    private static final TextColor COLOR_GRAY = new TextColor.RGB(0x88, 0x88, 0x88);

    public SectionTitle(String title) {
        this.title = title;
        this.color = COLOR_GRAY;
        setPreferredSize(new TerminalSize(30, 2));
    }

    @Override
    protected ComponentRenderer<SectionTitle> createDefaultRenderer() {
        return new ComponentRenderer<SectionTitle>() {
            @Override
            public TerminalSize getPreferredSize(SectionTitle component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, SectionTitle component) {
                component.drawTitle(graphics);
            }
        };
    }

    private void drawTitle(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        g.setForegroundColor(color);
        g.putString(0, 0, "  " + title);
        g.putString(0, 1, "  " + title);
    }
}
