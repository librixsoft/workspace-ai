package com.example;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Dashboard {

    private static final String C_RESET = "\u001B[0m";
    private static final String C_BOLD = "\u001B[1m";
    private static final String C_RED = "\u001B[31m";
    private static final String C_GREEN = "\u001B[32m";
    private static final String C_YELLOW = "\u001B[33m";
    private static final String C_BLUE = "\u001B[34m";
    private static final String C_MAGENTA = "\u001B[35m";
    private static final String C_CYAN = "\u001B[36m";
    private static final String C_WHITE = "\u001B[37m";
    private static final String C_BG_BLUE = "\u001B[44m";
    private static final String C_GRAY = "\u001B[90m";

    private static int width = 100;
    private static int height = 30;

    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.doResizeIfNecessary();

            width = screen.getTerminalSize().getColumns();
            height = screen.getTerminalSize().getRows();
            if (width < 80) width = 80;
            if (height < 24) height = 24;

            while (true) {
                TextGraphics g = screen.newTextGraphics();
                drawFrame(g);
                drawHeader(g);
                drawSidebar(g);
                drawMain(g);
                drawFooter(g);
                screen.refresh();

                KeyStroke key = screen.pollInput();
                if (key != null) {
                    if (key.getKeyType() == KeyType.Escape) break;
                    if (key.getKeyType() == KeyType.Character) {
                        char c = key.getCharacter();
                        if (c == 'q' || c == 'Q') break;
                    }
                }
                Thread.sleep(100);
            }

            screen.stopScreen();
            terminal.close();
        } catch (Exception e) {
            // Sin tty real -> fallback ASCII por stdout
            runAsciiFallback();
        }
    }

    private static void runAsciiFallback() {
        String title = C_CYAN + C_BOLD + "  JAVA TUI DASHBOARD — LANTERNA  (modo texto: sin tty)" + C_RESET;
        String date = C_GRAY + "  " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy  HH:mm:ss")) + C_RESET;
        String status = C_GREEN + C_BOLD + "  ● ONLINE" + C_RESET;

        while (true) {
            clear();
            System.out.println();
            System.out.println(title);
            System.out.println(date);
            System.out.println(C_YELLOW + "──────────────────────────────────────────────────────────────────────────" + C_RESET);
            System.out.println(status);
            System.out.println();
            System.out.println(C_BG_BLUE + C_WHITE + C_BOLD + " MENU " + C_RESET);
            System.out.println("  ▲ Sistema");
            System.out.println("  ├── CPU / Memoria");
            System.out.println("  ├── Disco");
            System.out.println("  └── Red");
            System.out.println("  ▲ Usuario");
            System.out.println("  ├── Sesiones");
            System.out.println("  └── Logout");
            System.out.println("  ▲ Config");
            System.out.println("  └── Salir (q)");
            System.out.println(C_YELLOW + "──────────────────────────────────────────────────────────────────────────" + C_RESET);
            System.out.println(C_BOLD + C_WHITE + " RESUMEN DEL SISTEMA" + C_RESET);
            System.out.println(C_YELLOW + "──────────────────────────────────────────────────────────────────────────" + C_RESET);
            System.out.println(" CPU          " + C_RED + cpuLoad() + "% " + C_RESET + "        " + C_GREEN + cpuLoad() + "% " + C_RESET);
            System.out.println(" MEMORIA      " + C_YELLOW + memUsed() + "/" + memTotal() + " GB " + C_RESET + "   " + C_GREEN + memPercent() + "% " + C_RESET);
            System.out.println(" DISCO /      " + C_CYAN + diskUsed() + "/" + diskTotal() + " GB " + C_RESET + "  " + C_YELLOW + diskPercent() + "% " + C_RESET);
            System.out.println(" RED          " + C_GREEN + "↑" + netUp() + " KB/s " + C_RESET + "   " + C_BLUE + "↓" + netDown() + " KB/s " + C_RESET);
            System.out.println(C_YELLOW + "──────────────────────────────────────────────────────────────────────────" + C_RESET);
            System.out.println(C_BG_BLUE + C_WHITE + C_BOLD + " LOG / ALERTAS " + C_RESET);
            System.out.println(C_GREEN + "[OK]   Servicios iniciados" + C_RESET);
            System.out.println(C_YELLOW + "[WARN] Temperatura elevada" + C_RESET);
            System.out.println(C_RED + "[ERR]  Fallo de autenticación" + C_RESET);
            System.out.println(C_CYAN + "[INFO] Nueva sesión detectada" + C_RESET);
            System.out.println(C_YELLOW + "──────────────────────────────────────────────────────────────────────────" + C_RESET);
            System.out.println(C_GRAY + "  Presiona 'q' o ESC para salir   |   Lanterna 3.x • Java TUI" + C_RESET);
            System.out.println();
            System.out.print(C_CYAN + "  > " + C_RESET);

            Scanner sc = new Scanner(System.in);
            String line;
            try {
                line = sc.nextLine();
            } catch (java.util.NoSuchElementException e) {
                line = "q";
            }
            sc.close();
            if (line == null || line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit")) break;
        }
    }

    private static void clear() {
        // imprime líneas en blanco para simular scroll/limpieza en texto
        for (int i = 0; i < 30; i++) System.out.println();
    }

    private static void drawFrame(TextGraphics g) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                g.setCharacter(x, y, ' ');
            }
        }
        putString(g, 0, 0, C_BG_BLUE + C_WHITE + C_BOLD + "╔" + "═".repeat(width - 2) + "╗" + C_RESET);
        for (int y = 1; y < height - 1; y++) {
            putString(g, 0, y, C_BG_BLUE + C_WHITE + "║" + C_RESET);
            putString(g, width - 1, y, C_BG_BLUE + C_WHITE + "║" + C_RESET);
        }
        putString(g, 0, height - 1, C_BG_BLUE + C_WHITE + C_BOLD + "╚" + "═".repeat(width - 2) + "╝" + C_RESET);
    }

    private static void drawHeader(TextGraphics g) {
        int hx = 2, hy = 1;
        putString(g, hx, hy, C_CYAN + C_BOLD + "  JAVA TUI DASHBOARD — LANTERNA" + C_RESET);
        putString(g, hx, hy + 1, C_GRAY + "  " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy  HH:mm:ss")) + C_RESET);
        String status = C_GREEN + C_BOLD + "  ● ONLINE" + C_RESET;
        putString(g, width - status.length() - 4, hy, status);
        putString(g, hx, hy + 2, C_YELLOW + "─".repeat(width - 2 * hx - 2) + C_RESET);
    }

    private static void drawSidebar(TextGraphics g) {
        int sx = 2, sy = 4;
        int sw = 22;
        putString(g, sx, sy, C_BG_BLUE + C_WHITE + C_BOLD + " MENU " + C_RESET);
        String[] items = {
            C_CYAN + "  ▲ Sistema" + C_RESET,
            C_WHITE + "  ├── CPU / Memoria" + C_RESET,
            C_WHITE + "  ├── Disco" + C_RESET,
            C_WHITE + "  └── Red" + C_RESET,
            C_MAGENTA + "  ▲ Usuario" + C_RESET,
            C_WHITE + "  ├── Sesiones" + C_RESET,
            C_WHITE + "  └── Logout" + C_RESET,
            C_YELLOW + "  ▲ Config" + C_RESET,
            C_WHITE + "  └── Salir (q)" + C_RESET,
        };
        int yy = sy + 1;
        for (String item : items) {
            putString(g, sx, yy, item + C_RESET);
            yy++;
        }
        for (int y = sy; y < sy + items.length + 2; y++) {
            putString(g, sx + sw - 1, y, C_BG_BLUE + C_WHITE + "│" + C_RESET);
        }
    }

    private static void drawMain(TextGraphics g) {
        int mx = 26, my = 4;
        int mw = width - mx - 3;

        putString(g, mx, my, C_BOLD + C_WHITE + " RESUMEN DEL SISTEMA" + C_RESET);
        putString(g, mx, my + 1, C_YELLOW + "─".repeat(mw) + C_RESET);

        drawPanel(g, mx + 2, my + 3, 22, 6, "CPU",
            String.format("%s%s%% %s", C_RED, cpuLoad(), C_RESET),
            String.format("%s%s%% %s", C_GREEN, cpuLoad(), C_RESET));
        drawPanel(g, mx + 26, my + 3, 22, 6, "MEMORIA",
            String.format("%s%s/%s GB %s", C_YELLOW, memUsed(), memTotal(), C_RESET),
            String.format("%s%s%% %s", C_GREEN, memPercent(), C_RESET));
        drawPanel(g, mx + 2, my + 10, 22, 6, "DISCO /",
            String.format("%s%s/%s GB %s", C_CYAN, diskUsed(), diskTotal(), C_RESET),
            String.format("%s%s%% %s", C_YELLOW, diskPercent(), C_RESET));
        drawPanel(g, mx + 26, my + 10, 22, 6, "RED",
            String.format("%s↑%s KB/s %s", C_GREEN, netUp(), C_RESET),
            String.format("%s↓%s KB/s %s", C_BLUE, netDown(), C_RESET));
        drawPanel(g, mx + 2, my + 17, mw - 4, 4, "LOG / ALERTAS",
            C_GREEN + "[OK]   Servicios iniciados" + C_RESET,
            C_YELLOW + "[WARN] Temperatura elevada" + C_RESET,
            C_RED + "[ERR]  Fallo de autenticación" + C_RESET,
            C_CYAN + "[INFO] Nueva sesión detectada" + C_RESET);
    }

    private static void drawPanel(TextGraphics g, int x, int y, int w, int h, String title, String... lines) {
        putString(g, x, y, C_BG_BLUE + C_WHITE + C_BOLD + "┌" + "─".repeat(w - 2) + "┐" + C_RESET);
        putString(g, x, y + 1, C_BG_BLUE + C_WHITE + "│ " + C_BOLD + title + C_RESET + " │");
        putString(g, x, y + 2, C_BG_BLUE + C_WHITE + "├" + "─".repeat(w - 2) + "┤" + C_RESET);
        for (int i = 0; i < lines.length; i++) {
            putString(g, x + 2, y + 3 + i, "│  " + lines[i]);
        }
        putString(g, x, y + 3 + lines.length, C_BG_BLUE + C_WHITE + "└" + "─".repeat(w - 2) + "┘" + C_RESET);
    }

    private static void drawFooter(TextGraphics g) {
        int fy = height - 2;
        putString(g, 2, fy, C_GRAY + "  Presiona 'q' o ESC para salir   |   Lanterna 3.x • Java TUI" + C_RESET);
        String help = C_CYAN + "  F1 Ayuda   F2 Opciones" + C_RESET;
        putString(g, width - help.length() - 4, fy, help);
    }

    private static void putString(TextGraphics g, int x, int y, String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\n') {
                continue;
            }
            g.setCharacter(x + i, y, c);
        }
    }

    private static String cpuLoad() { return String.valueOf((int)(Math.random() * 80 + 10)); }
    private static String memUsed() { return String.valueOf((int)(Math.random() * 7 + 2)); }
    private static String memTotal() { return "16"; }
    private static String memPercent() { return String.valueOf((int)(Math.random() * 90 + 10)); }
    private static String diskUsed() { return String.valueOf((int)(Math.random() * 180 + 40)); }
    private static String diskTotal() { return "256"; }
    private static String diskPercent() { return String.valueOf((int)(Math.random() * 80 + 20)); }
    private static String netUp() { return String.valueOf((int)(Math.random() * 500)); }
    private static String netDown() { return String.valueOf((int)(Math.random() * 2000)); }
}
