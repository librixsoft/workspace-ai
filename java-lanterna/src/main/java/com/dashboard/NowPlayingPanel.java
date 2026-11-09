package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class NowPlayingPanel extends AbstractComponent<NowPlayingPanel> {

    private final String title;
    private final String artist;
    private final String album;
    private final int currentTime;
    private final int totalTime;
    private final boolean playing;
    private final boolean shuffle;
    private final boolean repeat;

    public NowPlayingPanel(String title, String artist, String album,
                           int currentTime, int totalTime, boolean playing, boolean shuffle, boolean repeat) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.currentTime = currentTime;
        this.totalTime = totalTime;
        this.playing = playing;
        this.shuffle = shuffle;
        this.repeat = repeat;
        setPreferredSize(new TerminalSize(60, 20));
    }

    @Override
    protected ComponentRenderer<NowPlayingPanel> createDefaultRenderer() {
        return new ComponentRenderer<NowPlayingPanel>() {
            @Override
            public TerminalSize getPreferredSize(NowPlayingPanel component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, NowPlayingPanel component) {
                component.drawNowPlaying(graphics);
            }
        };
    }

    private void drawNowPlaying(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 5) return;

        int panelLeft = 1;
        int panelTop = 0;
        int panelWidth = w - 2;
        int panelHeight = h - 1;

        g.setForegroundColor(MusicPlayerTheme.COLOR_BORDER);
        g.drawRectangle(new TerminalPosition(panelLeft, panelTop), new TerminalSize(panelWidth, panelHeight), '░');

        int artLeft = panelLeft + 2;
        int artTop = panelTop + 1;
        int artWidth = 20;
        int artHeight = 8;

        drawAlbumArt(g, artLeft, artTop, artWidth, artHeight);

        int infoX = artLeft + artWidth + 2;
        int infoY = artTop;

        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_PRIMARY);
        String titleStr = title;
        if (titleStr.length() > 30) titleStr = titleStr.substring(0, 27) + "...";
        g.putString(infoX, infoY, titleStr);

        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
        g.putString(infoX, infoY + 1, artist);

        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
        String albumStr = album != null ? album : "";
        if (albumStr.length() > 30) albumStr = albumStr.substring(0, 27) + "...";
        g.putString(infoX, infoY + 2, albumStr);

        int progressY = artTop + artHeight + 1;
        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_PRIMARY);
        g.putString(artLeft, progressY, "┌─────────────────────────────────────────────────────────────┐");

        int barLeft = artLeft + 1;
        int barWidth = artWidth + 2 + 30;
        int barTop = progressY + 1;

        g.setForegroundColor(MusicPlayerTheme.COLOR_BORDER);
        g.drawLine(barLeft, barTop, barLeft + barWidth, barTop, '─');

        int filledWidth = (barWidth * currentTime) / Math.max(1, totalTime);
        g.setForegroundColor(MusicPlayerTheme.COLOR_PRIMARY);
        for (int i = 0; i < filledWidth && (barLeft + i) < barLeft + barWidth - 1; i++) {
            g.setCharacter(barLeft + i, barTop, '█');
        }
        g.setForegroundColor(MusicPlayerTheme.COLOR_BORDER);
        for (int i = filledWidth; i < barWidth && (barLeft + i) < barLeft + barWidth - 1; i++) {
            g.setCharacter(barLeft + i, barTop, '░');
        }

        String currentStr = String.format("%d:%02d", currentTime / 60, currentTime % 60);
        String totalStr = String.format("%d:%02d", totalTime / 60, totalTime % 60);
        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
        g.putString(barLeft, barTop + 1, currentStr);
        g.putString(barLeft + barWidth - currentStr.length() - 1, barTop + 1, totalStr);

        g.setForegroundColor(MusicPlayerTheme.COLOR_BORDER);
        g.drawLine(barLeft, barTop + 2, barLeft + barWidth, barTop + 2, '─');
        g.putString(artLeft, progressY + 3, "└─────────────────────────────────────────────────────────────┘");

        int controlsY = progressY + 4;
        drawControls(g, artLeft, controlsY, h);

        int volumeY = controlsY + 2;
        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
        g.putString(artLeft, volumeY, "Volume: 85%");
    }

    private void drawAlbumArt(TextGUIGraphics g, int left, int top, int width, int height) {
        g.setForegroundColor(MusicPlayerTheme.COLOR_BORDER);
        for (int y = top; y < top + height; y++) {
            g.setCharacter(left, y, '█');
            g.setCharacter(left + width - 1, y, '█');
        }
        for (int x = left; x < left + width; x++) {
            g.setCharacter(x, top, '█');
            g.setCharacter(x, top + height - 1, '█');
        }

        g.setForegroundColor(MusicPlayerTheme.COLOR_PRIMARY);
        for (int y = top + 1; y < top + height - 1; y++) {
            for (int x = left + 1; x < left + width - 1; x++) {
                if ((x + y) % 3 == 0) {
                    g.setCharacter(x, y, '█');
                } else {
                    g.setCharacter(x, y, '░');
                }
            }
        }

        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
        g.putString(left + 2, top + 1, "ALBUM ART");
    }

    private void drawControls(TextGUIGraphics g, int left, int top, int h) {
        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_PRIMARY);
        g.putString(left, top, "Controls");

        int btnWidth = 8;
        int gap = 2;
        int x = left + 1;

        String[][] btns = {
            {"⏮", "Prev"},
            {playing ? "⏸" : "▶", playing ? "Pause" : "Play"},
            {"⏭", "Next"},
            {shuffle ? "🔁" : "🔀", shuffle ? "Shuffle ON" : "Shuffle"},
            {repeat ? "🔁" : "🔂", repeat ? "Repeat ON" : "Repeat"}
        };

        TextColor[] colors = {
            MusicPlayerTheme.COLOR_TEXT_SECONDARY,
            playing ? MusicPlayerTheme.COLOR_PRIMARY : MusicPlayerTheme.COLOR_TEXT_SECONDARY,
            MusicPlayerTheme.COLOR_TEXT_SECONDARY,
            shuffle ? MusicPlayerTheme.COLOR_PRIMARY : MusicPlayerTheme.COLOR_TEXT_SECONDARY,
            repeat ? MusicPlayerTheme.COLOR_PRIMARY : MusicPlayerTheme.COLOR_TEXT_SECONDARY
        };

        for (int i = 0; i < btns.length; i++) {
            g.setForegroundColor(colors[i]);
            String icon = btns[i][0];
            int bx = x + (btnWidth - icon.length()) / 2;
            g.putString(Math.max(0, bx), top, icon);

            String label = btns[i][1];
            if (top + 1 < h) {
                g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
                int lx = x + (btnWidth - label.length()) / 2;
                g.putString(Math.max(0, lx), top + 1, label);
            }
            x += btnWidth + gap;
        }
    }
}
