package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class NotificationItem extends AbstractComponent<NotificationItem> {

    private final String icon;
    private final String message;
    private final TextColor color;

    public NotificationItem(String icon, String message, TextColor color) {
        this.icon = icon;
        this.message = message;
        this.color = color;
        setPreferredSize(new TerminalSize(50, 2));
    }

    @Override
    protected ComponentRenderer<NotificationItem> createDefaultRenderer() {
        return new ComponentRenderer<NotificationItem>() {
            @Override
            public TerminalSize getPreferredSize(NotificationItem component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, NotificationItem component) {
                component.drawNotification(graphics);
            }
        };
    }

    private void drawNotification(TextGUIGraphics g) {
        g.setForegroundColor(color);
        g.putString(0, 0, "  " + icon + "  " + message);
        g.putString(0, 1, "  " + icon + "  " + message);
    }
}
