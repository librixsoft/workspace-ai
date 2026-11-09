package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class PlaylistPanel extends AbstractComponent<PlaylistPanel> {

    private final String title;
    private final SongItem[] songs;
    private final int playingIndex;
    private final TextColor titleColor;
    private final TextColor borderColor;

    public PlaylistPanel(String title, SongItem[] songs, int playingIndex) {
        this.title = title;
        this.songs = songs;
        this.playingIndex = playingIndex;
        this.titleColor = MusicPlayerTheme.COLOR_PRIMARY;
        this.borderColor = MusicPlayerTheme.COLOR_BORDER;
        setPreferredSize(new TerminalSize(60, songs.length + 5));
    }

    @Override
    protected ComponentRenderer<PlaylistPanel> createDefaultRenderer() {
        return new ComponentRenderer<PlaylistPanel>() {
            @Override
            public TerminalSize getPreferredSize(PlaylistPanel component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, PlaylistPanel component) {
                component.drawPlaylist(graphics);
            }
        };
    }

    private void drawPlaylist(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 5) return;

        g.setForegroundColor(titleColor);
        g.putString(1, 0, " " + title + " ");

        g.setForegroundColor(borderColor);
        g.drawLine(0, 1, w - 1, 1, '-');
        g.drawLine(0, h - 1, w - 1, h - 1, '-');
        g.drawLine(0, 1, 0, h - 1, '|');
        g.drawLine(w - 1, 1, w - 1, h - 1, '|');

        int headerY = 2;
        g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
        g.putString(2, headerY, "#  Title");
        g.putString(42, headerY, "Artist");
        g.putString(w - 10, headerY, "Duration");

        g.setForegroundColor(borderColor);
        g.drawLine(2, headerY + 1, w - 3, headerY + 1, '-');

        int y = headerY + 2;
        for (int i = 0; i < songs.length && y < h - 2; i++) {
            SongItem song = songs[i];
            boolean isPlaying = (i == playingIndex);

            if (isPlaying) {
                g.setForegroundColor(MusicPlayerTheme.COLOR_PRIMARY);
                g.putString(2, y, "▶ ");
            } else {
                g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
                g.putString(2, y, "  ");
            }

            g.setForegroundColor(isPlaying ? MusicPlayerTheme.COLOR_PRIMARY : MusicPlayerTheme.COLOR_TEXT_PRIMARY);
            String titleStr = song.getTitle();
            if (titleStr.length() > 35) titleStr = titleStr.substring(0, 32) + "...";
            g.putString(5, y, titleStr);

            g.setForegroundColor(MusicPlayerTheme.COLOR_TEXT_SECONDARY);
            String artistStr = song.getArtist();
            if (artistStr.length() > 25) artistStr = artistStr.substring(0, 22) + "...";
            g.putString(42, y, artistStr);

            String durationStr = String.format("%d:%02d", song.getDuration() / 60, song.getDuration() % 60);
            g.putString(w - durationStr.length() - 2, y, durationStr);

            y++;
        }
    }
}
