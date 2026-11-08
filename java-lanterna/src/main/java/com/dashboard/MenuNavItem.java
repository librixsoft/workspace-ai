package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class MenuNavItem extends AbstractComponent<MenuNavItem> {

    private final String label;
    private final boolean active;
    private final TextColor activeColor;
    private final TextColor inactiveColor;

    private static final TextColor COLOR_CYAN = new TextColor.RGB(0x00, 0xff, 0xff);
    private static final TextColor COLOR_WHITE = new TextColor.RGB(0xe0, 0xe0, 0xe0);
    private static final TextColor COLOR_GRAY = new TextColor.RGB(0x88, 0x88, 0x88);

    public MenuNavItem(String label, boolean active) {
        this.label = label;
        this.active = active;
        this.activeColor = COLOR_CYAN;
        this.inactiveColor = active ? COLOR_CYAN : COLOR_GRAY;
        setPreferredSize(new TerminalSize(25, 2));
    }

    @Override
    protected ComponentRenderer<MenuNavItem> createDefaultRenderer() {
        return new ComponentRenderer<MenuNavItem>() {
            @Override
            public TerminalSize getPreferredSize(MenuNavItem component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, MenuNavItem component) {
                component.drawItem(graphics);
            }
        };
    }

    private void drawItem(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        g.setBackgroundColor(null);

        if (active) {
            g.setForegroundColor(activeColor);
            g.putString(0, 0, " ▶ " + label);
            g.putString(0, 1, "   " + label);
        } else {
            g.setForegroundColor(inactiveColor);
            g.putString(0, 0, "  " + label);
            g.putString(0, 1, "  " + label);
        }
    }
}
