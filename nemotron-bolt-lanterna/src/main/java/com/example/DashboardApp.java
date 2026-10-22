package com.example;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Dashboard TUI simples usando Lanterna.
 * Muestra métricas, gráficos de barras, y una tabla de actividad reciente.
 */
public class DashboardApp {

    // Métricas simuladas
    private static final int[] CPU_USAGE = {65, 72, 45, 88, 55, 70, 80};
    private static final int[] MEMORY_USAGE = {40, 42, 45, 48, 50, 52, 55};
    private static final int[] NETWORK_IN = {10, 15, 25, 20, 30, 35, 40};
    private static final int[] NETWORK_OUT = {5, 10, 12, 8, 15, 18, 20};

    private static final String[] METRIC_LABELS = {"CPU", "Mem", "Net In", "Net Out"};
    private static final int[] METRIC_DATA = {65, 55, 40, 20};
    private static final TextColor[] METRIC_COLORS = {
            TextColor.ANSI.RED, TextColor.ANSI.YELLOW,
            TextColor.ANSI.GREEN, TextColor.ANSI.BLUE
    };

    private static final String[] PROCESS_NAMES = {
            "java", "python", "node", "nginx", "postgres", "redis"
    };
    private static final int[] PROCESS_PIDS = {1234, 5678, 9012, 3456, 7890, 2345};
    private static final int[] PROCESS_CPU = {35, 22, 15, 8, 12, 3};
    private static final int[] PROCESS_MEM = {120, 85, 45, 20, 95, 30};

    private static final String[] ALERTS = {
            "⚠ CPU usage above 80% on worker-3",
            "⚠ High memory usage detected on db-master",
            "ℹ Scheduled backup completed",
            "⚠ Network latency spike detected",
            "✔ Health check passed for all services"
    };
    private static final TextColor[] ALERT_COLORS = {
            TextColor.ANSI.RED, TextColor.ANSI.RED,
            TextColor.ANSI.GREEN, TextColor.ANSI.YELLOW,
            TextColor.ANSI.GREEN
    };

    // Control de cierre
    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {
        try {
            // Crear terminal y pantalla
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            // Configurar GUI
            WindowManager wm = new DefaultWindowManager();
            TextGUI textGUI = new DefaultWindowedTextGUI(screen, wm);

            // Crear dashboard
            Dashboard dashboard = new Dashboard();
            Window mainWindow = dashboard.buildWindow();

            // Agregar listener de salida
            textGUI.addWindow(mainWindow);

            // Listener de tecla Escape para salir
            textGUI.addKeyCaptureListener(key -> {
                if (key.getKeyType() == com.googlecode.lanterna.input.KeyType.ESCAPE) {
                    running.set(false);
                    textGUI.closeAllWindows();
                    return com.googlecode.lanterna.input.KeyStroke.EOF;
                }
                return key;
            });

            // Loop principal
            while (running.get() && !textGUI.isWindowsClosed()) {
                textGUI.processEvents();
                Thread.sleep(100);
            }

            screen.stopScreen();
            terminal.close();

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al iniciar dashboard: " + e.getMessage());
            System.exit(1);
        }
    }
}
