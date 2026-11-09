package com.dashboard;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MusicPlayer {

    private static final TextColor COLOR_CYAN = new TextColor.RGB(0x00, 0xff, 0xff);
    private static final TextColor COLOR_GREEN = new TextColor.RGB(0x00, 0xff, 0x6e);
    private static final TextColor COLOR_RED = new TextColor.RGB(0xff, 0x55, 0x55);
    private static final TextColor COLOR_YELLOW = new TextColor.RGB(0xff, 0xaa, 0x00);
    private static final TextColor COLOR_MAGENTA = new TextColor.RGB(0xcc, 0x66, 0xff);
    private static final TextColor COLOR_WHITE = new TextColor.RGB(0xe0, 0xe0, 0xe0);
    private static final TextColor COLOR_GRAY = new TextColor.RGB(0x88, 0x88, 0x88);
    private static final TextColor COLOR_ORANGE = new TextColor.RGB(0xff, 0x88, 0x00);
    private static final TextColor COLOR_BLUE = new TextColor.RGB(0x55, 0x88, 0xff);
    private static final TextColor COLOR_DARK_BG = new TextColor.RGB(0x1a, 0x1a, 0x2e);

    public static void main(String[] args) {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);

            BasicWindow window = new BasicWindow();
            window.setHints(java.util.Set.of(Window.Hint.FIT_TERMINAL_WINDOW));

            Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

            buildHeader(contentPanel);
            contentPanel.addComponent(createEmptySpace(0));
            buildMainContent(contentPanel);
            contentPanel.addComponent(createEmptySpace(0));
            buildFooter(contentPanel);

            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Panel createEmptySpace(int rows) {
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        for (int i = 0; i < rows; i++) {
            panel.addComponent(new EmptySpace());
        }
        return panel;
    }

    private static Panel createGridPanel(int columns) {
        Panel panel = new Panel();
        GridLayout grid = new GridLayout(columns);
        panel.setLayoutManager(grid);
        return panel;
    }

    private static void configureGridLayout(Panel panel, int horizontalSpacing, int leftMargin, int rightMargin, int topMargin, int bottomMargin) {
        GridLayout grid = (GridLayout) panel.getLayoutManager();
        grid.setHorizontalSpacing(horizontalSpacing);
        grid.setLeftMarginSize(leftMargin);
        grid.setRightMarginSize(rightMargin);
        grid.setTopMarginSize(topMargin);
        grid.setBottomMarginSize(bottomMargin);
    }

    private static void buildHeader(Panel parent) {
        Panel headerPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        Panel topBar = createGridPanel(3);
        configureGridLayout(topBar, 0, 1, 1, 0, 0);

        Panel logoPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        logoPanel.addComponent(new Label(" ♪  ").setForegroundColor(COLOR_CYAN));
        logoPanel.addComponent(new Label(" LANTERNA MUSIC PLAYER ").setForegroundColor(COLOR_CYAN));
        topBar.addComponent(logoPanel);

        Panel navTabs = createGridPanel(4);
        configureGridLayout(navTabs, 0, 1, 1, 0, 0);
        String[] tabs = {"Playlist", "Library", "Search", "Settings"};
        TextColor[] tabColors = {COLOR_CYAN, COLOR_GRAY, COLOR_GRAY, COLOR_GRAY};
        for (int i = 0; i < tabs.length; i++) {
            Panel tab = new Panel(new LinearLayout(Direction.HORIZONTAL));
            Label tabLabel = new Label(" " + tabs[i] + " ");
            tabLabel.setForegroundColor(tabColors[i]);
            tab.addComponent(tabLabel);
            navTabs.addComponent(tab);
        }
        topBar.addComponent(navTabs);

        Panel rightPanel = createGridPanel(2);
        configureGridLayout(rightPanel, 0, 1, 1, 0, 0);

        Panel datetimePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        datetimePanel.addComponent(new Label(" ").setForegroundColor(COLOR_GRAY));
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        datetimePanel.addComponent(new Label(datetime).setForegroundColor(COLOR_GRAY));
        rightPanel.addComponent(datetimePanel);

        topBar.addComponent(rightPanel);
        headerPanel.addComponent(topBar);

        Panel separator = new Panel(new LinearLayout(Direction.HORIZONTAL));
        separator.addComponent(new EmptySpace());
        headerPanel.addComponent(separator);

        Panel userRow = createGridPanel(3);
        configureGridLayout(userRow, 0, 1, 1, 0, 0);

        Panel userPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        userPanel.addComponent(new Label(" ").setForegroundColor(COLOR_MAGENTA));
        userPanel.addComponent(new Label(" User ").setForegroundColor(COLOR_MAGENTA));
        userPanel.addComponent(new Label("  ").setForegroundColor(COLOR_GRAY));
        userPanel.addComponent(new Label(" ● ").setForegroundColor(COLOR_GREEN));
        userPanel.addComponent(new Label(" Online ").setForegroundColor(COLOR_GREEN));
        userRow.addComponent(userPanel);

        Panel rolePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        rolePanel.addComponent(new EmptySpace());
        rolePanel.addComponent(new Label(" v3.0.0 ").setForegroundColor(COLOR_GRAY));
        rolePanel.addComponent(new EmptySpace());
        userRow.addComponent(rolePanel);

        headerPanel.addComponent(userRow);
        parent.addComponent(headerPanel);
    }

    private static void buildMainContent(Panel parent) {
        Panel mainPanel = createGridPanel(1);
        configureGridLayout(mainPanel, 1, 1, 1, 0, 0);

        Panel topRow = createGridPanel(2);
        configureGridLayout(topRow, 1, 0, 0, 0, 0);

        buildSidebar(topRow);
        buildNowPlaying(topRow);

        mainPanel.addComponent(topRow);

        Panel bottomRow = createGridPanel(1);
        configureGridLayout(bottomRow, 0, 0, 0, 1, 0);
        buildPlaylist(bottomRow);

        mainPanel.addComponent(bottomRow);
        parent.addComponent(mainPanel);
    }

    private static void buildSidebar(Panel parent) {
        Panel sidebar = createGridPanel(1);
        configureGridLayout(sidebar, 0, 0, 0, 1, 1);

        Panel navTitle = new Panel();
        navTitle.addComponent(new SectionTitle(" NAVIGATION "));
        sidebar.addComponent(navTitle);
        sidebar.addComponent(createEmptySpace(0));

        String[] menuItems = {"Now Playing", "Playlist", "Library", "Albums", "Artists", "Settings"};
        for (int i = 0; i < menuItems.length; i++) {
            sidebar.addComponent(new MenuNavItem(menuItems[i], i == 0));
            sidebar.addComponent(createEmptySpace(0));
        }

        sidebar.addComponent(createEmptySpace(0));
        Panel divider = new Panel(new LinearLayout(Direction.HORIZONTAL));
        divider.addComponent(new EmptySpace());
        sidebar.addComponent(divider);
        sidebar.addComponent(createEmptySpace(0));

        Panel secTitle = new Panel();
        secTitle.addComponent(new SectionTitle(" QUICK ACTIONS "));
        sidebar.addComponent(secTitle);
        sidebar.addComponent(createEmptySpace(0));

        String[] links = {"Play/Pause", "Previous", "Next", "Shuffle", "Repeat"};
        TextColor[] linkColors = {COLOR_CYAN, COLOR_GREEN, COLOR_YELLOW, COLOR_MAGENTA, COLOR_ORANGE};
        for (int i = 0; i < links.length; i++) {
            sidebar.addComponent(new ActionItem(links[i], linkColors[i]));
            sidebar.addComponent(createEmptySpace(0));
        }

        parent.addComponent(sidebar);
    }

    private static void buildNowPlaying(Panel parent) {
        Panel contentArea = createGridPanel(1);
        configureGridLayout(contentArea, 0, 0, 0, 1, 1);

        SongItem[] songs = createDummySongs();
        int playingIndex = 0;

        NowPlayingPanel nowPlaying = new NowPlayingPanel(
                "Midnight Drive",
                "The Weeknd",
                "After Hours",
                45,
                200,
                true,
                true,
                false
        );
        contentArea.addComponent(nowPlaying);

        parent.addComponent(contentArea);
    }

    private static void buildPlaylist(Panel parent) {
        Panel playlistPanel = createGridPanel(1);
        configureGridLayout(playlistPanel, 0, 0, 0, 1, 0);

        SongItem[] songs = createDummySongs();
        int playingIndex = 0;

        PlaylistPanel playlist = new PlaylistPanel(" PLAYLIST / QUEUE ", songs, playingIndex);
        playlistPanel.addComponent(playlist);

        parent.addComponent(playlistPanel);
    }

    private static SongItem[] createDummySongs() {
        return new SongItem[]{
            new SongItem(1, "Midnight Drive", "The Weeknd", "After Hours", 200, true),
            new SongItem(2, "Blinding Lights", "The Weeknd", "After Hours", 200, false),
            new SongItem(3, "Drive", "Miley Cyrus", "End of Summer", 225, false),
            new SongItem(4, "Someone Like You", "Adele", "25", 255, false),
            new SongItem(5, "Shape of You", "Ed Sheeran", "Divide", 233, false),
            new SongItem(6, "Bohemian Rhapsody", "Queen", "A Night at the Opera", 355, false),
            new SongItem(7, "Hotel California", "Eagles", "Hotel California", 411, false),
            new SongItem(8, "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", 402, false),
            new SongItem(9, "Imagine", "John Lennon", "Imagine", 183, false),
            new SongItem(10, "Billie Jean", "Michael Jackson", "Thriller", 289, false)
        };
    }

    private static void buildFooter(Panel parent) {
        Panel footerPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        Panel borderLine = new Panel(new LinearLayout(Direction.HORIZONTAL));
        borderLine.addComponent(new EmptySpace());
        footerPanel.addComponent(borderLine);

        Panel footerContent = createGridPanel(3);
        configureGridLayout(footerContent, 0, 1, 1, 0, 0);

        Panel helpText = new Panel(new LinearLayout(Direction.HORIZONTAL));
        helpText.addComponent(new Label("  HELP").setForegroundColor(COLOR_GRAY));
        helpText.addComponent(new EmptySpace());
        helpText.addComponent(new Label("F1").setForegroundColor(COLOR_CYAN));
        helpText.addComponent(new EmptySpace());
        helpText.addComponent(new Label("Help").setForegroundColor(COLOR_GRAY));
        footerContent.addComponent(helpText);

        Panel centerInfo = new Panel(new LinearLayout(Direction.HORIZONTAL));
        centerInfo.addComponent(new EmptySpace());
        centerInfo.addComponent(new Label("  Lanterna Music Player v3.0.0 ").setForegroundColor(COLOR_GRAY));
        centerInfo.addComponent(new EmptySpace());
        footerContent.addComponent(centerInfo);

        Panel statusText = new Panel(new LinearLayout(Direction.HORIZONTAL));
        statusText.addComponent(new EmptySpace());
        statusText.addComponent(new Label("  STATUS").setForegroundColor(COLOR_GRAY));
        statusText.addComponent(new EmptySpace());
        statusText.addComponent(new Label("Playing ●").setForegroundColor(COLOR_GREEN));
        statusText.addComponent(new EmptySpace());
        statusText.addComponent(new Label("Shuffle: ON").setForegroundColor(COLOR_CYAN));
        statusText.addComponent(new EmptySpace());
        statusText.addComponent(new Label("Repeat: OFF").setForegroundColor(COLOR_GRAY));
        footerContent.addComponent(statusText);

        footerPanel.addComponent(footerContent);
        parent.addComponent(footerPanel);
    }
}
