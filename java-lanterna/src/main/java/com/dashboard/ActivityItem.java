package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class ActivityItem extends AbstractComponent<ActivityItem> {

    private final String timestamp;
    private final String message;
    private final TextColor color;

    public ActivityItem(String timestamp, String message, TextColor color) {
        this.timestamp = timestamp;
        this.message = message;
        this.color = color;
        setPreferredSize(new TerminalSize(50, 2));
    }

    @Override
    protected ComponentRenderer<ActivityItem> createDefaultRenderer() {
        return new ComponentRenderer<ActivityItem>() {
            @Override
            public TerminalSize getPreferredSize(ActivityItem component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, ActivityItem component) {
                component.drawActivity(graphics);
            }
        };
    }

    private void drawActivity(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        String timePart = timestamp;
        String msgPart = message;

        g.setForegroundColor(new TextColor.RGB(0x66, 0x66, 0x66));
        g.putString(0, 0, timePart);

        g.setForegroundColor(color);
        int msgStart = Math.min(timePart.length() + 2, w - 1);
        g.putString(msgStart, 0, msgPart);
    }
}
