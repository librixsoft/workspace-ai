package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class ActionItem extends AbstractComponent<ActionItem> {

    private final String label;
    private final TextColor color;

    public ActionItem(String label, TextColor color) {
        this.label = label;
        this.color = color;
        setPreferredSize(new TerminalSize(30, 2));
    }

    @Override
    protected ComponentRenderer<ActionItem> createDefaultRenderer() {
        return new ComponentRenderer<ActionItem>() {
            @Override
            public TerminalSize getPreferredSize(ActionItem component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, ActionItem component) {
                component.drawAction(graphics);
            }
        };
    }

    private void drawAction(TextGUIGraphics g) {
        g.setForegroundColor(color);
        g.putString(0, 0, "  " + label);
        g.putString(0, 1, "  " + label);
    }
}
