package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.GridLayout.GridLayoutData;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.LinearLayout.LayoutData;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class DashboardApp {

    private static final List<Label> dynamicLabels = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen;
        try {
            screen = new TerminalScreen(terminal);
        }
        catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        screen.startScreen();

        MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);

        BasicWindow window = new BasicWindow();
        window.setSize(new TerminalSize(110, 32));
        window.setTitle("Dashboard - Sistema de Monitoreo");

        Panel mainPanel = createDashboard();
        window.setComponent(mainPanel);

        window.setCloseWindowWithEscape(true);

        textGUI.addWindow(window);

        // Thread de actualización
        Thread updater = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    refreshData();
                    textGUI.invokeLater(() -> {
                        try {
                            textGUI.updateScreen();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        updater.setDaemon(true);
        updater.start();

        // Loop principal
        textGUI.updateScreen();
        while (true) {
            var input = textGUI.handleInput(screen.pollInput());
            if (input == null) break;
            Thread.sleep(50);
        }

        textGUI.removeWindow(window);
        window.close();
        screen.clear();
        screen.stopScreen();
        screen.close();
        terminal.close();
    }

    private static Panel createDashboard() {
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout());
        mainPanel.addComponent(createHeader());
        mainPanel.addComponent(new EmptySpace(TextColor.ANSI.BLACK));
        mainPanel.addComponent(createGridPanel());
        mainPanel.addComponent(new EmptySpace(TextColor.ANSI.BLACK));
        mainPanel.addComponent(createFooter());
        return mainPanel;
    }

    private static Panel createHeader() {
        Panel header = new Panel();
        header.setLayoutManager(new LinearLayout());

        Label title = new Label("  DASHBOARD - SISTEMA DE MONITOREO  ");
        title.setForegroundColor(TextColor.ANSI.BLUE);
        title.setBackgroundColor(new TextColor.RGB(230, 230, 250));
        header.addComponent(title);

        Label spacer = new Label("                                                                                          ");
        spacer.setBackgroundColor(new TextColor.RGB(230, 230, 250));
        header.addComponent(spacer);

        Label exitLabel = new Label("  ESC: Salir  ");
        exitLabel.setForegroundColor(TextColor.ANSI.RED);
        exitLabel.setBackgroundColor(new TextColor.RGB(230, 230, 250));
        header.addComponent(exitLabel);

        return header;
    }

    private static Panel createGridPanel() {
        Panel grid = new Panel();
        GridLayout gridLayout = new GridLayout(2)
            .setHorizontalSpacing(2)
            .setVerticalSpacing(2);
        grid.setLayoutManager(gridLayout);

        grid.addComponent(createCard("CPU Usage", new Color(0, 100, 255)));
        grid.addComponent(createCard("Memory", new Color(0, 200, 0)));
        grid.addComponent(createCard("Network", new Color(0, 200, 200)));
        grid.addComponent(createCard("Disk", new Color(255, 150, 0)));
        grid.addComponent(createCard("Processes", new Color(200, 0, 200)));
        grid.addComponent(createCard("System Info", new Color(255, 255, 0)));

        return grid;
    }

    private static Panel createCard(String title, Color color) {
        Panel card = new Panel();
        card.setLayoutManager(new LinearLayout());

        // Título
        Label titleLabel = new Label(" " + title + " ");
        titleLabel.setForegroundColor(TextColor.ANSI.WHITE);
        titleLabel.setBackgroundColor(new TextColor.RGB(color.getRed() / 3, color.getGreen() / 3, color.getBlue() / 3));
        titleLabel.setLabelWidth(30);
        card.addComponent(titleLabel);

        // Separador
        card.addComponent(new EmptySpace(TextColor.ANSI.BLACK));

        // Label dinámico
        Label dataLabel = new Label("");
        dataLabel.setForegroundColor(new TextColor.RGB(color.getRed(), color.getGreen(), color.getBlue()));
        card.addComponent(dataLabel);

        dynamicLabels.add(dataLabel);

        return card;
    }

    private static Panel createFooter() {
        Panel footer = new Panel();
        footer.setLayoutManager(new LinearLayout());

        Label footerLabel = new Label("  Refresh: 1s | Data: Simulated | Lanterna Dashboard v1.0  ");
        footerLabel.setForegroundColor(TextColor.ANSI.GREEN);
        footerLabel.setBackgroundColor(TextColor.ANSI.BLACK);
        footer.addComponent(footerLabel);

        return footer;
    }

    private static void refreshData() {
        String[][] data = {
            genCPU(),
            genMemory(),
            genNetwork(),
            genDisk(),
            genProcess(),
            genSystem()
        };

        for (int i = 0; i < dynamicLabels.size() && i < data.length; i++) {
            dynamicLabels.get(i).setText(data[i][0]);
        }
    }

    private static String[] genCPU() {
        double cpu = Math.random() * 80 + 10;
        String bar = generateBar((int) cpu, 30);
        return new String[]{
            String.format("  Current: %.1f%%\n  Avg: %.1f%%\n  Cores: 8\n  Temp: %d°C",
                cpu, cpu * 0.95, (int) (cpu * 0.8 + 30)) + "\n" + bar
        };
    }

    private static String[] genMemory() {
        double mem = Math.random() * 70 + 20;
        long totalMem = 16384;
        long usedMem = (long) (totalMem * mem / 100);
        String bar = generateBar((int) mem, 30);
        return new String[]{
            String.format("  Used: %d MB / %d MB\n  Cached: %d MB\n  Swap: %d MB",
                usedMem, totalMem, (long) (totalMem * 0.1), (long) (totalMem * 0.02)) + "\n" + bar
        };
    }

    private static String[] genNetwork() {
        double net = Math.random() * 100;
        return new String[]{
            String.format("  RX: %.1f MB/s\n  TX: %.1f MB/s\n  Packets: %d\n  Errors: %d\n%s",
                net * 0.6, net * 0.4, (int) (net * 10), (int) (net * 0.01),
                generateBar((int) (net / 2), 30))
        };
    }

    private static String[] genDisk() {
        double disk = Math.random() * 40 + 30;
        long totalDisk = 500000;
        long usedDisk = (long) (totalDisk * disk / 100);
        String bar = generateBar((int) disk, 30);
        return new String[]{
            String.format("  Used: %.1f GB / %.0f GB\n  Read: %.1f MB/s\n  Write: %.1f MB/s",
                usedDisk / 1024.0, totalDisk / 1024.0, disk * 0.3, disk * 0.2) + "\n" + bar
        };
    }

    private static String[] genProcess() {
        int procs = (int) (Math.random() * 300) + 50;
        String bar = generateBar((int) (Math.random() * 30 + 10), 30);
        return new String[]{
            String.format("  Running: %d\n  Sleeping: %d\n  Stopped: %d\n  Zombie: %d\n  Total: %d",
                procs / 10, procs * 8 / 10, procs / 50, procs / 200, procs) + "\n" + bar
        };
    }

    private static String[] genSystem() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new String[]{
            String.format("  Host: anibals-MacBook-Pro\n  OS: Mac OS X 14.5\n  Kernel: 23.5.0\n  Java: 17.0.1\n  Time: %s",
                sdf.format(new java.util.Date())) + "\n" + generateBar((int) (Math.random() * 20 + 5), 30)
        };
    }

    private static String generateBar(int value, int width) {
        StringBuilder sb = new StringBuilder();
        int filled = Math.min(width, Math.max(0, (int) (width * Math.min(100, value) / 100)));
        int empty = width - filled;
        for (int i = 0; i < filled; i++) sb.append('\u2588');
        for (int i = 0; i < empty; i++) sb.append('\u2591');
        return sb.toString();
    }
}
