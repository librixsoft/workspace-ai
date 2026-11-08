package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class StatusIndicator extends AbstractComponent<StatusIndicator> {

    public enum Status { ONLINE, WARNING, OFFLINE }

    private final String label;
    private final Status status;
    private final TextColor labelColor;
    private final TextColor statusColor;

    private static final TextColor COLOR_GREEN = new TextColor.RGB(0x00, 0xff, 0x6e);
    private static final TextColor COLOR_YELLOW = new TextColor.RGB(0xff, 0xaa, 0x00);
    private static final TextColor COLOR_RED = new TextColor.RGB(0xff, 0x55, 0x55);
    private static final TextColor COLOR_GRAY = new TextColor.RGB(0x88, 0x88, 0x88);
    private static final TextColor COLOR_CYAN = new TextColor.RGB(0x00, 0xff, 0xff);

    public StatusIndicator(String label, Status status) {
        this.label = label;
        this.status = status;
        this.labelColor = COLOR_GRAY;
        this.statusColor = status == Status.ONLINE ? COLOR_GREEN :
                           status == Status.WARNING ? COLOR_YELLOW : COLOR_RED;
        setPreferredSize(new TerminalSize(30, 2));
    }

    @Override
    protected ComponentRenderer<StatusIndicator> createDefaultRenderer() {
        return new ComponentRenderer<StatusIndicator>() {
            @Override
            public TerminalSize getPreferredSize(StatusIndicator component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, StatusIndicator component) {
                component.drawStatus(graphics);
            }
        };
    }

    private void drawStatus(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        String statusText = status == Status.ONLINE ? "  Operational" :
                           status == Status.WARNING ? "  Degraded" : "  Down";
        String dot = status == Status.ONLINE ? "●" :
                     status == Status.WARNING ? "●" : "●";

        g.setForegroundColor(labelColor);
        g.putString(0, 0, "  " + label);

        g.setForegroundColor(statusColor);
        g.putString(w / 2 - 4, 0, dot + "  " + statusText);
    }
}
