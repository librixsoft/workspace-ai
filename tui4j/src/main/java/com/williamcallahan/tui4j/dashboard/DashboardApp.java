package com.williamcallahan.tui4j.dashboard;

import com.williamcallahan.tui4j.compat.bubbletea.Program;
import com.williamcallahan.tui4j.compat.bubbletea.Model;
import com.williamcallahan.tui4j.compat.bubbletea.Message;

import java.util.ArrayList;
import java.util.List;

// ==================== MESSAGE TYPES ====================
class DashboardMessage implements Message {
    private final Type type;
    private final Object data;
    
    public enum Type {
        TICK, QUIT, RESIZE
    }
    
    public DashboardMessage(Type type) {
        this(type, null);
    }
    
    public DashboardMessage(Type type, Object data) {
        this.type = type;
        this.data = data;
    }
    
    public Type type() { return type; }
    public Object data() { return data; }
}

// ==================== MAIN APP ====================
public class DashboardApp implements Model {
    
    private static final String APP_TITLE = "  SYSTEM DASHBOARD  ";
    private int width = 80;
    private int height = 24;
    
    // Data for tables
    private final List<String[]> cpuData = new ArrayList<>();
    private final List<String[]> memoryData = new ArrayList<>();
    private final List<String[]> networkData = new ArrayList<>();
    private final List<String[]> processData = new ArrayList<>();
    
    private int selectedSection = 0; // 0=CPU, 1=Memory, 2=Network, 3=Processes
    
    public DashboardApp() {
        generateCPUData();
        generateMemoryData();
        generateNetworkData();
        generateProcessData();
    }
    
    private void generateCPUData() {
        String[] headers = {"Core", "Usage %", "Temp C", "Freq GHz"};
        cpuData.add(headers);
        for (int i = 0; i < 8; i++) {
            int usage = (int)(15 + Math.random() * 75);
            int temp = 35 + (int)(Math.random() * 40);
            double freq = 2.5 + Math.random() * 2.0;
            cpuData.add(new String[]{
                "CPU " + i,
                String.format("%3d", usage),
                String.format("%3d", temp),
                String.format("%.2f", freq)
            });
        }
    }
    
    private void generateMemoryData() {
        String[] headers = {"Process", "RSS MB", "VSZ MB", "%MEM"};
        memoryData.add(headers);
        String[] procs = {"java", "python3", "node", "nginx", "postgres", "redis", "docker", "kafka"};
        for (String proc : procs) {
            int rss = 10 + (int)(Math.random() * 500);
            int vsz = rss + 50 + (int)(Math.random() * 200);
            double mem = 0.1 + Math.random() * 5.0;
            memoryData.add(new String[]{
                proc,
                String.format("%4d", rss),
                String.format("%4d", vsz),
                String.format("%.1f", mem)
            });
        }
    }
    
    private void generateNetworkData() {
        String[] headers = {"Interface", "RX (MB/s)", "TX (MB/s)", "Status"};
        networkData.add(headers);
        String[] ifaces = {"eth0", "wlan0", "docker0", "lo"};
        for (String iface : ifaces) {
            double rx = Math.random() * 100;
            double tx = Math.random() * 50;
            String status = iface.equals("lo") ? "UP" : (Math.random() > 0.2 ? "UP" : "DOWN");
            networkData.add(new String[]{
                iface,
                String.format("%7.1f", rx),
                String.format("%7.1f", tx),
                status
            });
        }
    }
    
    private void generateProcessData() {
        String[] headers = {"PID", "USER", "STATE", "COMMAND"};
        processData.add(headers);
        String[][] procs = {
            {"1234", "root", "S", "/sbin/init"},
            {"2345", "www-data", "S", "nginx: worker"},
            {"3456", "postgres", "S", "postgres: main"},
            {"4567", "redis", "S", "redis-server"},
            {"5678", "app", "R", "java -jar app.jar"},
            {"6789", "app", "S", "java -jar worker.jar"},
            {"7890", "deploy", "S", "docker-compose up"},
            {"8901", "root", "S", "systemd-journald"}
        };
        for (String[] p : procs) {
            processData.add(p);
        }
    }
    
    @Override
    public Model init() {
        return this;
    }
    
    @Override
    public Model update(Message msg) {
        if (msg instanceof DashboardMessage) {
            DashboardMessage dm = (DashboardMessage) msg;
            switch (dm.type()) {
                case TICK:
                    updateCPUData();
                    return this;
                case QUIT:
                    return this;
                case RESIZE:
                    return this;
            }
        }
        return this;
    }
    
    private void updateCPUData() {
        for (int i = 1; i < cpuData.size(); i++) {
            String[] row = cpuData.get(i);
            int usage = Math.max(5, Math.min(99, Integer.parseInt(row[1]) + (int)(Math.random() * 20 - 10)));
            int temp = Math.max(30, Math.min(90, Integer.parseInt(row[2]) + (int)(Math.random() * 6 - 3)));
            row[1] = String.format("%3d", usage);
            row[2] = String.format("%3d", temp);
        }
    }
    
    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        
        // Clear screen effect
        sb.append("\033[2J\033[H");
        
        // Title bar
        String titleBar = " " + APP_TITLE;
        String subtitle = " Use Tab to switch sections | Q to quit";
        sb.append(titleBar);
        sb.append("\n");
        sb.append(subtitle);
        sb.append("\n");
        sb.append("-".repeat(Math.min(width, 80)));
        sb.append("\n");
        
        // Section tabs
        String[] sections = {"CPU", "MEMORY", "NETWORK", "PROCESSES"};
        for (int i = 0; i < sections.length; i++) {
            if (i == selectedSection) {
                sb.append("> ").append(sections[i]).append("  ");
            } else {
                sb.append("  ").append(sections[i]).append("    ");
            }
        }
        sb.append("\n");
        sb.append("-".repeat(Math.min(width, 80)));
        sb.append("\n");
        
        // Render selected section
        switch (selectedSection) {
            case 0:
                renderCPU(sb);
                break;
            case 1:
                renderMemory(sb);
                break;
            case 2:
                renderNetwork(sb);
                break;
            case 3:
                renderProcesses(sb);
                break;
        }
        
        // Footer
        sb.append("\n");
        sb.append("-".repeat(Math.min(width, 80)));
        sb.append("\n");
        sb.append(" Dashboard v1.0 | Auto-refreshing every tick\n");
        
        return sb.toString();
    }
    
    private void renderCPU(StringBuilder sb) {
        sb.append(" CPU USAGE\n\n");
        renderTable(sb, cpuData);
    }
    
    private void renderMemory(StringBuilder sb) {
        sb.append(" MEMORY USAGE\n\n");
        renderTable(sb, memoryData);
    }
    
    private void renderNetwork(StringBuilder sb) {
        sb.append(" NETWORK STATUS\n\n");
        renderTable(sb, networkData);
    }
    
    private void renderProcesses(StringBuilder sb) {
        sb.append(" RUNNING PROCESSES\n\n");
        renderTable(sb, processData);
    }
    
    private void renderTable(StringBuilder sb, List<String[]> data) {
        if (data.isEmpty()) return;
        
        // Calculate column widths
        int[] colWidths = new int[data.get(0).length];
        for (String[] row : data) {
            for (int j = 0; j < row.length && j < colWidths.length; j++) {
                colWidths[j] = Math.max(colWidths[j], row[j].length());
            }
        }
        
        int totalWidth = 0;
        for (int w : colWidths) totalWidth += w + 4;
        totalWidth = Math.min(totalWidth, Math.min(width, 80));
        
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            
            // Header separator
            if (i == 1) {
                sb.append("  ");
                for (int j = 0; j < row.length; j++) {
                    sb.append("-".repeat(Math.max(2, colWidths[j])));
                    if (j < row.length - 1) sb.append("  ");
                }
                sb.append("\n");
            }
            
            sb.append("  ");
            for (int j = 0; j < row.length; j++) {
                String cell = row[j];
                if (i == 0) {
                    sb.append("[ ").append(cell);
                } else {
                    sb.append("  ").append(cell);
                }
                // Padding
                int pad = Math.max(0, colWidths[j] - cell.length() + 2);
                sb.append(" ".repeat(pad));
            }
            sb.append("\n");
        }
    }
    
    // ==================== MAIN ====================
    public static void main(String[] args) {
        DashboardApp dashboard = new DashboardApp();
        
        Program program = new Program(dashboard)
            .withAltScreen()
            .withMouseAllMotion();
        
        try {
            program.run();
        } catch (Exception e) {
            System.err.println("Error running dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
