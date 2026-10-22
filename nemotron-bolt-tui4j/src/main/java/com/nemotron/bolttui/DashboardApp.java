package com.nemotron.bolttui;

import com.williamcallahan.tui4j.compat.bubbletea.Command;
import com.williamcallahan.tui4j.compat.bubbletea.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.Message;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.QuitMessage;
import com.williamcallahan.tui4j.compat.bubbletea.UpdateResult;

import java.text.DecimalFormat;

/**
 * Dashboard completa en terminal con TUI4J 0.3.3 (arquitectura bubbletea) y Java 21.
 * Implementa la interfaz Model: init / update / view. El bucle de eventos y el
 * repintado los gestiona el Program de TUI4J.
 */
public class DashboardApp implements Model {

    private static final int WIDTH = 100;

    // Colores ANSI
    private static final String C_RESET = "\u001b[0m";
    private static final String C_BOLD = "\u001b[1m";
    private static final String C_GREEN = "\u001b[32m";
    private static final String C_CYAN = "\u001b[36m";
    private static final String C_YELLOW = "\u001b[33m";
    private static final String C_RED = "\u001b[31m";
    private static final String C_GRAY = "\u001b[90m";
    private static final String C_WHITE = "\u001b[37m";

    // Estado del modelo
    private Section section = Section.SYSTEM;
    private double cpu = 25.0;
    private double mem = 45.0;
    private double disk = 60.0;
    private double netRx = 10.5;
    private double netTx = 5.2;
    private int tasks = 12;
    private int requests = 1240;
    private long uptime = 0;

    public enum Section {
        DASHBOARD, SYSTEM, NETWORK, LOGS, EXIT
    }

    // Mensajes
    public static final class TickMessage implements Message {
        public static final TickMessage INSTANCE = new TickMessage();
    }

    @Override
    public Command init() {
        // Primer tick
        return () -> TickMessage.INSTANCE;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage k) {
            String key = k.key();
            switch (key) {
                case "q", "Ctrl+c" -> {
                    return new UpdateResult<>(this, QuitMessage::new);
                }
                case "right", "l", "1" -> section = Section.DASHBOARD;
                case "down", "j", "2" -> section = Section.SYSTEM;
                case "left", "h", "3" -> section = Section.NETWORK;
                case "up", "k", "4" -> section = Section.LOGS;
                case "5" -> section = Section.EXIT;
            }
            return new UpdateResult<>(this, () -> TickMessage.INSTANCE);
        }

        if (msg instanceof TickMessage) {
            cpu = clamp(cpu + (Math.random() * 6 - 3), 10, 90);
            mem = clamp(mem + (Math.random() * 4 - 2), 30, 80);
            disk = clamp(disk + (Math.random() * 2 - 1), 50, 75);
            netRx = clamp(netRx + (Math.random() * 2 - 1), 1, 50);
            netTx = clamp(netTx + (Math.random() * 2 - 1), 1, 30);
            tasks += (int) (Math.random() * 3) - 1;
            if (tasks < 1) tasks = 1;
            requests += (int) (Math.random() * 50);
            uptime += 1000;
            return new UpdateResult<>(this, () -> TickMessage.INSTANCE);
        }

        return new UpdateResult<>(this, null);
    }

    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        String line = " ".repeat(WIDTH - 2);

        // ---------- Título ----------
        sb.append(C_BOLD).append(C_CYAN).append("╔").append("═".repeat(WIDTH - 2)).append("╗").append(C_RESET).append("\n");
        sb.append("║ ").append(C_BOLD).append("NEMOTRON-BOLT DASHBOARD").append(C_RESET)
            .append("  ").append(C_GRAY).append("Java 21 + TUI4J 0.3.3 (bubbletea)")
            .append(C_RESET).append(" ".repeat(Math.max(0, WIDTH - 2 - 52))).append("║\n");
        sb.append("╠").append("═".repeat(WIDTH - 2)).append("╣").append(C_RESET).append("\n");

        // ---------- Cuerpo ----------
        // Sidebar: 24 cols; Main: resto
        int sbW = 24;
        int mvW = WIDTH - sbW - 3;

        // Líneas del cuerpo (sidebar y main lado a lado)
        String[] sbLines = new String[] {
            renderBoxTitle("NAVEGACIÓN", sbW - 2),
            menuItem(Section.DASHBOARD),
            menuItem(Section.SYSTEM),
            menuItem(Section.NETWORK),
            menuItem(Section.LOGS),
            menuItem(Section.EXIT),
            "" // relleno
        };
        String[] mvLines = new String[] {
            renderBoxTitle("MÉTRICAS EN TIEMPO REAL", mvW - 2),
            metricRow("CPU", fmtCpu(), estado(cpu, 70)),
            metricRow("MEMORIA", fmtMem(), estado(mem, 80)),
            metricRow("DISCO", fmtDisk(), estado(disk, 75)),
            metricRow("RED RX", String.format("%.1f MB/s", netRx), "OK"),
            metricRow("RED TX", String.format("%.1f MB/s", netTx), "OK"),
            metricRow("TAREAS", tasks + "", "OK"),
            metricRow("PETICIONES", requests + "", "OK"),
            ""
        };

        int bodyLines = Math.max(sbLines.length, mvLines.length);
        for (int r = 0; r < bodyLines; r++) {
            String s = sbLines.length > r ? sbLines[r] : "";
            String m = mvLines.length > r ? mvLines[r] : "";
            sb.append("║").append(C_GREEN).append("│").append(C_RESET)
                .append(s).append(" ".repeat(Math.max(0, sbW - 2 - s.length())))
                .append(C_GREEN).append("│").append(C_RESET).append(" ")
                .append(C_CYAN).append("│").append(C_RESET)
                .append(m).append(" ".repeat(Math.max(0, mvW - 2 - m.length())))
                .append(C_CYAN).append("│").append(C_RESET)
                .append("║\n");
        }

        // ---------- Barra de estado ----------
        sb.append("╠").append("═".repeat(WIDTH - 2)).append("╣").append(C_RESET).append("\n");
        sb.append("║ ").append(C_GRAY)
            .append(String.format("CPU %3d%%  MEM %3d%%  DISK %3d%%  RX %5.1f  TX %5.1f  TAREAS %4d  REQ %6d",
                    (int) cpu, (int) mem, (int) disk, netRx, netTx, tasks, requests))
            .append(C_RESET)
            .append(" ".repeat(Math.max(0, WIDTH - 2 - 62)))
            .append(C_GRAY).append("UPTIME ").append(formatUptime(uptime)).append(C_RESET).append(" ║\n");
        sb.append("╚").append("═".repeat(WIDTH - 2)).append("╝").append(C_RESET).append("\n");
        sb.append(C_GRAY).append("  [h/j/k/l o 1-5] navegan  |  q salir  |  Actualización cada 1s").append(C_RESET).append("\n");

        return sb.toString();
    }

    // ---------- helpers ----------

    private String pad(String s, int w) {
        if (s.length() >= w) return s.substring(0, w);
        return s + " ".repeat(w - s.length());
    }

    private String renderBoxTitle(String title, int w) {
        return C_BOLD + C_GREEN + pad(title, w) + C_RESET;
    }

    private String menuItem(Section s) {
        boolean sel = (s == section);
        String base = switch (s) {
            case DASHBOARD -> " 1. Dashboard";
            case SYSTEM -> " 2. Sistema";
            case NETWORK -> " 3. Red";
            case LOGS -> " 4. Logs";
            case EXIT -> " 5. Salir";
        };
        return sel ? C_BOLD + C_CYAN + "▸" + base + C_RESET : C_GRAY + base + C_RESET;
    }

    private String metricRow(String label, String value, String status) {
        return C_WHITE + pad(label, 12) + C_RESET
            + C_YELLOW + pad(value, 11) + C_RESET
            + C_GREEN + pad(status, 6) + C_RESET;
    }

    private String fmtCpu() { return String.format("%.1f%%", cpu); }
    private String fmtMem() { return String.format("%.1f%%", mem); }
    private String fmtDisk() { return String.format("%.1f%%", disk); }

    private String estado(double v, int umbral) {
        return v > umbral ? "ALTO" : "OK";
    }

    private String formatUptime(long millis) {
        long s = millis / 1000;
        long h = s / 3600;
        long m = (s % 3600) / 60;
        long sec = s % 60;
        return String.format("%02d:%02d:%02d", h, m, sec);
    }

    private double clamp(double v, double min, double max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    // ---------- punto de entrada ----------

    public static void main(String[] args) {
        Program app = new Program(new DashboardApp());
        app.run();
    }
}
