package com.example;

import com.googlecode.lanterna.Terminal;
import com.googlecode.lanterna.Terminal.Size;
import com.googlecode.lanterna.graphics.Colour;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Keyboard;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.ui.*;
import com.googlecode.lanterna.ui.component.*;
import com.googlecode.lanterna.util.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Dashboard de monitoreo en tiempo real usando Lanterna 3.
 * Interfaz visual con header, panel de métricas y pie de página.
 */
public class Dashboard {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        try {
            Terminal terminal = terminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            BasicWindow dashboardWindow = new BasicWindow("Dashboard de Monitoreo");
            dashboardWindow.setHints(java.util.Arrays.asList(
                Window.Hint.FULL_SCREEN,
                Window.Hint.NO_DECORATIONS
            ));

            // Header superior con título y reloj
            Panel headerPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
            Label systemTitle = new Label(" MONITOREO DEL SISTEMA ");
            systemTitle.withBackgroundColor(Colour.ANSI.RED);
            systemTitle.withForegroundColor(Colour.ANSI.WHITE);
            headerPanel.addComponent(systemTitle);
            headerPanel.addComponent(new EmptySpace(new Terminal.Size(1, 0)));

            Label clockLabel = new Label("");
            scheduler.scheduleAtFixedRate(() -> {
                String dateStr = LocalDateTime.now().format(DATE_FORMAT);
                String timeStr = LocalDateTime.now().format(TIME_FORMAT);
                clockLabel.setText(" [" + dateStr + " - " + timeStr + "]");
            }, 0, 1, TimeUnit.SECONDS);

            // Panel central de métricas con progres bars en grid 4x3
            Panel metricsPanel = new Panel(new GridLayout(4, 3));

            Label cpuLabel = new Label("CPU:");
            ProgressBar cpuBar = new ProgressBar(0, 100, 30);
            Label cpuValue = new Label("0%");

            Label memLabel = new Label("Mem:");
            ProgressBar memBar = new ProgressBar(0, 100, 30);
            Label memValue = new Label("0%");

            Label diskLabel = new Label("Disk:");
            ProgressBar diskBar = new ProgressBar(0, 100, 30);
            Label diskValue = new Label("0%");

            Label netLabel = new Label("Net:");
            ProgressBar netBar = new ProgressBar(0, 100, 30);
            Label netValue = new Label("0 KB/s");

            metricsPanel.addComponent(cpuLabel);
            metricsPanel.addComponent(cpuBar);
            metricsPanel.addComponent(cpuValue);

            metricsPanel.addComponent(memLabel);
            metricsPanel.addComponent(memBar);
            metricsPanel.addComponent(memValue);

            metricsPanel.addComponent(diskLabel);
            metricsPanel.addComponent(diskBar);
            metricsPanel.addComponent(diskValue);

            metricsPanel.addComponent(netLabel);
            metricsPanel.addComponent(netBar);
            metricsPanel.addComponent(netValue);

            // Panel inferior con controles
            Panel footerPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
            Button refreshBtn = new Button("[ Refrescar ]", () -> {
                updateMetricValues(cpuBar, memBar, diskBar, cpuValue, memValue, diskValue);
            });

            Button closeBtn = new Button("[ Cerrar ]", dashboardWindow::close);

            Button helpBtn = new Button("[ Ayuda ]", () -> {
                MessageDialog.showMessageDialog(textGUI, "Ayuda del Dashboard",
                    "Dashboard de monitoreo en tiempo real\n\n" +
                    "• CPU: Uso del procesador (0-100%)\n" +
                    "• Mem: Memoria RAM utilizada (0-100%)\n" +
                    "• Disco: Espacio en disco utilizado (0-100%)\n" +
                    "• Net: Ancho de banda de red",
                    MessageDialogButton.Ok);
            });

            footerPanel.addComponent(helpBtn);
            footerPanel.addComponent(new EmptySpace(new Terminal.Size(1, 0)));
            footerPanel.addComponent(refreshBtn);
            footerPanel.addComponent(new EmptySpace(new Terminal.Size(1, 0)));
            footerPanel.addComponent(closeBtn);

            // Ensamblaje del layout completo
            Panel rootPanel = new Panel(new BorderLayout());
            rootPanel.addComponent(headerPanel, BorderLayout.Location.NORTH);
            rootPanel.addComponent(metricsPanel, BorderLayout.CENTER);
            rootPanel.addComponent(footerPanel, BorderLayout.Location.SOUTH);

            dashboardWindow.setComponent(rootPanel);

            textGUI.addWindowAndWait(dashboardWindow);

            // Inicializar valores
            updateMetricValues(cpuBar, memBar, diskBar, cpuValue, memValue, diskValue);

            // Programar actualizaciones cada 3 segundos
            scheduler.scheduleAtFixedRate(() -> {
                updateMetricValues(cpuBar, memBar, diskBar, cpuValue, memValue, diskValue);
            }, 0, 3, TimeUnit.SECONDS);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scheduler.shutdown();
            if (screen != null) {
                try {
                    screen.stopScreen();
                    screen.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Actualiza los valores de las métricas con valores simulados.
     */
    private static void updateMetricValues(ProgressBar cpuBar, ProgressBar memBar,
                                           ProgressBar diskBar, Label cpuValue, Label memValue,
                                           Label diskValue) {
        cpuBar.setValue(RANDOM.nextInt(100));
        memBar.setValue(RANDOM.nextInt(60) + 20);
        diskBar.setValue(RANDOM.nextInt(50) + 30);

        cpuValue.setText(cpuBar.getValue() + "%");
        memValue.setText(memBar.getValue() + "%");
        diskValue.setText(diskBar.getValue() + "%");
    }

    /**
     * Maneja el listener de teclado para navegación y cierre.
     * Lanterna ya maneja el cierre con la tecla Escape o el botón de cerrar.
     */
    private static void keyboardListener(WindowBasedTextGUI textGUI, BasicWindow window,
                                         ScheduledExecutorService scheduler) {
        // El sistema de ventanas de Lanterna ya maneja el cierre con la tecla Escape
        // o la combinación de hints NO_DECORATIONS con la tecla de atajo
    }
}