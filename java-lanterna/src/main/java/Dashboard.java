import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

public class Dashboard {

    // Colores
    private static final TextColor BLUE = new TextColor.RGB(0, 150, 255);
    private static final TextColor CYAN = new TextColor.RGB(0, 200, 200);
    private static final TextColor GREEN = new TextColor.RGB(0, 200, 0);
    private static final TextColor YELLOW = new TextColor.RGB(255, 200, 0);
    private static final TextColor RED = new TextColor.RGB(255, 50, 50);
    private static final TextColor WHITE = new TextColor.RGB(255, 255, 255);
    private static final TextColor GRAY = new TextColor.RGB(128, 128, 128);
    private static final TextColor DARK_GRAY = new TextColor.RGB(64, 64, 64);

    public static void main(String[] args) {
        try {
            // Inicializar terminal
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setInitialTerminalSize(new TerminalSize(132, 50));
            Terminal terminal = factory.createTerminal();

            // Inicializar screen
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            // Ocultar cursor
            terminal.setCursorPosition(0, 0);

            boolean running = true;

            while (running) {
                // Limpiar pantalla
                TextGraphics graphics = screen.newTextGraphics();
                terminal.clearScreen();

                // Dibujar la UI
                drawTitle(graphics);
                drawMetricsPanel(graphics);
                drawGraphPanel(graphics);
                drawStatusPanel(graphics);
                drawNavigationPanel(graphics);

                // Refrescar pantalla
                screen.refresh();

                // Leer input
                KeyStroke key = terminal.readInput();
                if (key != null && key.getCharacter() != null) {
                    char c = key.getCharacter();
                    if (c == 'q' || c == 'Q') {
                        running = false;
                    }
                }

                Thread.sleep(50);
            }

            // Limpiar
            terminal.setCursorPosition(0, 0);
            screen.stopScreen();
            terminal.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void drawTitle(TextGraphics g) {
        int x = 30;
        int y = 2;

        // Título con fondo
        g.setBackgroundColor(DARK_GRAY);
        g.setForegroundColor(BLUE);
        g.putString(x - 2, y, "  ");
        g.putString(x + 25, y, "  ");

        g.setForegroundColor(BLUE);
        g.putString(x, y, "═══ DASHBOARD MONITOR ═══");

        g.setForegroundColor(GRAY);
        g.putString(x, y + 1, "Real-time System Monitoring");
    }

    private static void drawMetricsPanel(TextGraphics g) {
        int x = 5;
        int y = 7;
        int width = 120;

        // Dibujar caja
        drawBox(g, x, y, width, 6);

        // Título del panel
        g.setForegroundColor(BLUE);
        g.putString(x + 3, y + 1, "METRICS");

        // Métricas
        g.setForegroundColor(WHITE);
        g.putString(x + 3, y + 2, "CPU:");
        g.setForegroundColor(GREEN);
        g.putString(x + 8, y + 2, "45%");

        g.setForegroundColor(WHITE);
        g.putString(x + 3, y + 3, "MEM:");
        g.setForegroundColor(YELLOW);
        g.putString(x + 8, y + 3, "2.1 GB");

        g.setForegroundColor(WHITE);
        g.putString(x + 3, y + 4, "DISK:");
        g.setForegroundColor(RED);
        g.putString(x + 9, y + 4, "67%");
    }

    private static void drawGraphPanel(TextGraphics g) {
        int x = 5;
        int y = 15;
        int width = 120;

        // Dibujar caja
        g.setForegroundColor(CYAN);
        drawBox(g, x, y, width, 12);

        // Título del panel
        g.setForegroundColor(CYAN);
        g.putString(x + 3, y + 1, "PERFORMANCE GRAPH");

        // Gráfico de barras placeholder
        g.setForegroundColor(GRAY);
        int graphX = x + 5;
        int graphY = y + 3;

        for (int i = 0; i < 20; i++) {
            int height = (int) (Math.random() * 6) + 1;
            for (int j = 0; j < height; j++) {
                g.setBackgroundColor(YELLOW);
                g.putString(graphX + i, graphY + (6 - j), "  ");
            }
        }

        // Labels
        g.setForegroundColor(GRAY);
        g.putString(graphX, graphY + 7, "00:00");
        g.putString(graphX + 100, graphY + 7, "23:59");
    }

    private static void drawStatusPanel(TextGraphics g) {
        int x = 5;
        int y = 29;
        int width = 120;

        // Dibujar caja
        g.setForegroundColor(GREEN);
        drawBox(g, x, y, width, 6);

        // Título del panel
        g.setForegroundColor(GREEN);
        g.putString(x + 3, y + 1, "SYSTEM STATUS");

        // Estado
        g.setForegroundColor(WHITE);
        g.putString(x + 3, y + 2, "Server:");
        g.setForegroundColor(GREEN);
        g.putString(x + 11, y + 2, "ONLINE");

        g.setForegroundColor(WHITE);
        g.putString(x + 3, y + 3, "Uptime:");
        g.setForegroundColor(YELLOW);
        g.putString(x + 11, y + 3, "24h 15m");

        g.setForegroundColor(WHITE);
        g.putString(x + 3, y + 4, "Active:");
        g.setForegroundColor(CYAN);
        g.putString(x + 11, y + 4, "142 req/s");
    }

    private static void drawNavigationPanel(TextGraphics g) {
        int x = 5;
        int y = 38;
        int width = 120;

        // Dibujar caja
        g.setForegroundColor(DARK_GRAY);
        drawBox(g, x, y, width, 4);

        // Navegación
        g.setForegroundColor(WHITE);
        g.putString(x + 5, y + 1, "NAVIGATION");

        g.setForegroundColor(GRAY);
        g.putString(x + 5, y + 2, "[1] Overview");
        g.putString(x + 25, y + 2, "[2] Logs");
        g.putString(x + 40, y + 2, "[3] Settings");
        g.putString(x + 60, y + 2, "[Q] Quit");
    }

    private static void drawBox(TextGraphics g, int x, int y, int width, int height) {
        // Esquinas
        g.putString(x, y, "┌");
        g.putString(x + width - 2, y, "┐");
        g.putString(x, y + height - 1, "└");
        g.putString(x + width - 2, y + height - 1, "┘");

        // Bordes horizontales
        for (int i = 1; i < width - 2; i += 2) {
            g.putString(x + i, y, "─");
            g.putString(x + i, y + height - 1, "─");
        }

        // Bordes verticales
        for (int i = 1; i < height - 1; i++) {
            g.putString(x, y + i, "│");
            g.putString(x + width - 2, y + i, "│");
        }
    }
}
