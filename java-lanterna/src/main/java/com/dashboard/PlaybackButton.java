package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class PlaybackButton extends AbstractComponent<PlaybackButton> {

    public enum ButtonType { PLAY, PAUSE, PREV, NEXT, SHUFFLE, REPEAT }

    private final ButtonType type;
    private final boolean active;
    private final TextColor activeColor;
    private final TextColor inactiveColor;

    public PlaybackButton(ButtonType type, boolean active) {
        this.type = type;
        this.active = active;
        this.activeColor = MusicPlayerTheme.COLOR_PRIMARY;
        this.inactiveColor = MusicPlayerTheme.COLOR_TEXT_SECONDARY;
        setPreferredSize(new TerminalSize(10, 2));
    }

    public boolean isActive() {
        return active;
    }

    public ButtonType getType() {
        return type;
    }

    @Override
    protected ComponentRenderer<PlaybackButton> createDefaultRenderer() {
        return new ComponentRenderer<PlaybackButton>() {
            @Override
            public TerminalSize getPreferredSize(PlaybackButton component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, PlaybackButton component) {
                component.drawButton(graphics);
            }
        };
    }

    private void drawButton(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();

        TextColor color = active ? activeColor : inactiveColor;
        g.setForegroundColor(color);

        String icon;
        switch (type) {
            case PLAY:
                icon = active ? "⏸" : "▶";
                break;
            case PAUSE:
                icon = active ? "⏸" : "⏸";
                break;
            case PREV:
                icon = "⏮";
                break;
            case NEXT:
                icon = "⏭";
                break;
            case SHUFFLE:
                icon = active ? "🔁" : "🔀";
                break;
            case REPEAT:
                icon = active ? "🔁" : "🔂";
                break;
            default:
                icon = "●";
        }

        int x = (w - icon.length()) / 2;
        g.putString(Math.max(0, x), 0, icon);

        String label = getLabel();
        if (label.length() > 0 && h > 1) {
            int lx = (w - label.length()) / 2;
            g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
            g.putString(Math.max(0, lx), 1, label);
        }
    }

    private String getLabel() {
        switch (type) {
            case PLAY:
                return active ? "Pause" : "Play";
            case PAUSE:
                return active ? "Pause" : "Play";
            case PREV:
                return "Prev";
            case NEXT:
                return "Next";
            case SHUFFLE:
                return active ? "Shuffle ON" : "Shuffle";
            case REPEAT:
                return active ? "Repeat ON" : "Repeat";
            default:
                return "";
        }
    }
}
