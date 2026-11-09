package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class SongItem extends AbstractComponent<SongItem> {

    private final int number;
    private final String title;
    private final String artist;
    private final String album;
    private final int duration;
    private final boolean playing;
    private final TextColor textColor;
    private final TextColor highlightColor;
    private final TextColor borderColor;

    public SongItem(int number, String title, String artist, String album, int duration, boolean playing) {
        this.number = number;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.playing = playing;
        this.textColor = MusicPlayerTheme.COLOR_TEXT_PRIMARY;
        this.highlightColor = MusicPlayerTheme.COLOR_PRIMARY;
        this.borderColor = MusicPlayerTheme.COLOR_BORDER;
        setPreferredSize(new TerminalSize(60, 3));
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    protected ComponentRenderer<SongItem> createDefaultRenderer() {
        return new ComponentRenderer<SongItem>() {
            @Override
            public TerminalSize getPreferredSize(SongItem component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, SongItem component) {
                component.drawSong(graphics);
            }
        };
    }

    private void drawSong(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 3) return;

        TextColor color = playing ? highlightColor : textColor;

        g.setForegroundColor(color);
        String numberStr = String.format("%2d.", number);
        g.putString(0, 0, numberStr);

        int titleX = 5;
        g.putString(titleX, 0, title);

        int artistX = titleX + Math.min(title.length() + 2, 40);
        if (artistX < w - 20) {
            g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
            g.putString(artistX, 0, artist);
        }

        String durationStr = String.format("%d:%02d", duration / 60, duration % 60);
        g.putString(w - durationStr.length() - 2, 0, durationStr);

        if (h > 1) {
            g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
            String albumStr = album != null ? album : "";
            if (albumStr.length() > 30) albumStr = albumStr.substring(0, 27) + "...";
            g.putString(titleX, 1, albumStr);
        }

        if (playing) {
            g.setForegroundColor(highlightColor);
            g.putString(0, 1, "▶ ");
        }
    }
}
