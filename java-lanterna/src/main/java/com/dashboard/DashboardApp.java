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

public class DashboardApp {

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
        logoPanel.addComponent(new Label(" ◆ ").setForegroundColor(COLOR_CYAN));
        logoPanel.addComponent(new Label(" DASHBOARD ").setForegroundColor(COLOR_CYAN));
        topBar.addComponent(logoPanel);

        Panel navTabs = createGridPanel(4);
        configureGridLayout(navTabs, 0, 1, 1, 0, 0);
        String[] tabs = {"Overview", "Analytics", "Reports", "Monitoring"};
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

        Panel notifPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        notifPanel.addComponent(new Label(" ").setForegroundColor(COLOR_YELLOW));
        notifPanel.addComponent(new Label(" 3 ").setForegroundColor(COLOR_YELLOW));
        notifPanel.addComponent(new Label(" notifications ").setForegroundColor(COLOR_GRAY));
        rightPanel.addComponent(notifPanel);

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
        userPanel.addComponent(new Label(" Admin User ").setForegroundColor(COLOR_MAGENTA));
        userPanel.addComponent(new Label("  ").setForegroundColor(COLOR_GRAY));
        userPanel.addComponent(new Label(" ● ").setForegroundColor(COLOR_GREEN));
        userPanel.addComponent(new Label(" Online ").setForegroundColor(COLOR_GREEN));
        userRow.addComponent(userPanel);

        Panel rolePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        rolePanel.addComponent(new EmptySpace());
        rolePanel.addComponent(new Label(" Role: Administrator ").setForegroundColor(COLOR_GRAY));
        rolePanel.addComponent(new EmptySpace());
        rolePanel.addComponent(new Label(" v2.4.1 ").setForegroundColor(COLOR_GRAY));
        userRow.addComponent(rolePanel);

        Panel envPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        envPanel.addComponent(new EmptySpace());
        envPanel.addComponent(new Label(" PROD ").setForegroundColor(COLOR_RED));
        envPanel.addComponent(new EmptySpace());
        userRow.addComponent(envPanel);

        headerPanel.addComponent(userRow);
        parent.addComponent(headerPanel);
    }

    private static void buildMainContent(Panel parent) {
        Panel mainPanel = createGridPanel(1);
        configureGridLayout(mainPanel, 1, 1, 1, 0, 0);

        Panel topRow = createGridPanel(2);
        configureGridLayout(topRow, 1, 0, 0, 0, 0);

        buildSidebar(topRow);
        buildDashboardContent(topRow);

        mainPanel.addComponent(topRow);

        Panel bottomRow = createGridPanel(1);
        configureGridLayout(bottomRow, 0, 0, 0, 1, 0);
        buildChartsSection(bottomRow);
        buildDataTableSection(bottomRow);
        buildBottomPanels(bottomRow);

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

        String[] menuItems = {"Dashboard", "Analytics", "Projects", "Teams", "Reports", "Settings"};
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
        secTitle.addComponent(new SectionTitle(" SYSTEM STATUS "));
        sidebar.addComponent(secTitle);
        sidebar.addComponent(createEmptySpace(0));

        sidebar.addComponent(new StatusIndicator("Primary Server", StatusIndicator.Status.ONLINE));
        sidebar.addComponent(createEmptySpace(0));
        sidebar.addComponent(new StatusIndicator("Database Cluster", StatusIndicator.Status.ONLINE));
        sidebar.addComponent(createEmptySpace(0));
        sidebar.addComponent(new StatusIndicator("Redis Cache", StatusIndicator.Status.ONLINE));
        sidebar.addComponent(createEmptySpace(0));
        sidebar.addComponent(new StatusIndicator("API Gateway", StatusIndicator.Status.WARNING));
        sidebar.addComponent(createEmptySpace(0));
        sidebar.addComponent(new StatusIndicator("CDN Network", StatusIndicator.Status.ONLINE));

        sidebar.addComponent(createEmptySpace(0));
        Panel divider2 = new Panel(new LinearLayout(Direction.HORIZONTAL));
        divider2.addComponent(new EmptySpace());
        sidebar.addComponent(divider2);
        sidebar.addComponent(createEmptySpace(0));

        Panel secTitle2 = new Panel();
        secTitle2.addComponent(new SectionTitle(" QUICK LINKS "));
        sidebar.addComponent(secTitle2);
        sidebar.addComponent(createEmptySpace(0));

        String[] links = {"Documentation", "Support Ticket", "API Reference", "Changelog"};
        TextColor[] linkColors = {COLOR_CYAN, COLOR_GREEN, COLOR_YELLOW, COLOR_MAGENTA};
        for (int i = 0; i < links.length; i++) {
            sidebar.addComponent(new ActionItem(links[i], linkColors[i]));
            sidebar.addComponent(createEmptySpace(0));
        }

        parent.addComponent(sidebar);
    }

    private static void buildDashboardContent(Panel parent) {
        Panel contentArea = createGridPanel(1);
        configureGridLayout(contentArea, 0, 0, 0, 1, 1);

        Panel kpiTitle = new Panel();
        kpiTitle.addComponent(new SectionTitle(" KEY METRICS "));
        contentArea.addComponent(kpiTitle);
        contentArea.addComponent(createEmptySpace(0));

        Panel kpiPanel = createGridPanel(4);
        configureGridLayout(kpiPanel, 1, 1, 1, 1, 1);

        kpiPanel.addComponent(new KPIValue("TOTAL USERS", "12,847", "▲ +12.5% vs last month", true, COLOR_CYAN));
        kpiPanel.addComponent(new KPIValue("REVENUE", "$48.2K", "▲ +8.3% vs last month", true, COLOR_GREEN));
        kpiPanel.addComponent(new KPIValue("ORDERS", "1,234", "▼ -2.1% vs last month", false, COLOR_YELLOW));
        kpiPanel.addComponent(new KPIValue("CONVERSION", "3.2%", "▲ +0.5% vs last month", true, COLOR_MAGENTA));

        contentArea.addComponent(kpiPanel);
        contentArea.addComponent(createEmptySpace(1));

        Panel resourceTitle = new Panel();
        resourceTitle.addComponent(new SectionTitle(" SYSTEM RESOURCES "));
        contentArea.addComponent(resourceTitle);
        contentArea.addComponent(createEmptySpace(0));

        Panel resourcePanel = createGridPanel(1);
        configureGridLayout(resourcePanel, 0, 1, 1, 1, 1);
        resourcePanel.addComponent(new ResourceBar("CPU USAGE", "72% (6/8 cores)", 72));
        resourcePanel.addComponent(createEmptySpace(0));
        resourcePanel.addComponent(new ResourceBar("MEMORY", "4.2 / 8 GB (52%)", 52));
        resourcePanel.addComponent(createEmptySpace(0));
        resourcePanel.addComponent(new ResourceBar("DISK", "128 / 256 GB (50%)", 50));
        resourcePanel.addComponent(createEmptySpace(0));
        resourcePanel.addComponent(new ResourceBar("NETWORK I/O", "1.2 Gbps (38%)", 38));

        contentArea.addComponent(resourcePanel);
        contentArea.addComponent(createEmptySpace(1));

        Panel notifTitle = new Panel();
        notifTitle.addComponent(new SectionTitle(" NOTIFICATIONS "));
        contentArea.addComponent(notifTitle);
        contentArea.addComponent(createEmptySpace(0));

        Panel notifPanel = createGridPanel(1);
        configureGridLayout(notifPanel, 0, 1, 1, 1, 1);
        notifPanel.addComponent(new NotificationItem("⚠", "High CPU usage detected on node-3", COLOR_YELLOW));
        notifPanel.addComponent(createEmptySpace(0));
        notifPanel.addComponent(new NotificationItem("✓", "Backup completed successfully", COLOR_GREEN));
        notifPanel.addComponent(createEmptySpace(0));
        notifPanel.addComponent(new NotificationItem("!", "SSL certificate expires in 7 days", COLOR_RED));
        notifPanel.addComponent(createEmptySpace(0));
        notifPanel.addComponent(new NotificationItem("→", "New deployment v2.4.1 available", COLOR_CYAN));

        contentArea.addComponent(notifPanel);

        parent.addComponent(contentArea);
    }

    private static void buildChartsSection(Panel parent) {
        Panel chartsPanel = createGridPanel(1);
        configureGridLayout(chartsPanel, 0, 0, 0, 0, 0);

        Panel chartsTitle = new Panel();
        chartsTitle.addComponent(new SectionTitle(" ANALYTICS "));
        chartsPanel.addComponent(chartsTitle);
        chartsPanel.addComponent(createEmptySpace(0));

        Panel chartsGrid = createGridPanel(2);
        configureGridLayout(chartsGrid, 1, 1, 1, 1, 1);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};
        int[] revenue = {12, 19, 15, 25, 22, 30};
        chartsGrid.addComponent(new ChartComponent(ChartComponent.ChartType.BAR_CHART,
                "REVENUE TREND", months, new int[][]{revenue},
                new String[]{""}, new TextColor[]{COLOR_CYAN}));

        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        int[] users = {40, 65, 55, 80, 70, 45, 35};
        chartsGrid.addComponent(new ChartComponent(ChartComponent.ChartType.LINE_CHART,
                "USER ACTIVITY", days, new int[][]{users},
                new String[]{""}, new TextColor[]{COLOR_GREEN}));

        chartsPanel.addComponent(chartsGrid);
        parent.addComponent(chartsPanel);
    }

    private static void buildDataTableSection(Panel parent) {
        Panel tablePanel = createGridPanel(1);
        configureGridLayout(tablePanel, 0, 0, 0, 1, 0);

        Panel tableTitle = new Panel();
        tableTitle.addComponent(new SectionTitle(" RECENT TRANSACTIONS "));
        tablePanel.addComponent(tableTitle);
        tablePanel.addComponent(createEmptySpace(0));

        String[] headers = {"ID", "Customer", "Amount", "Status", "Date"};
        String[][] data = {
                {"#4521", "John Doe", "$299.00", "Completed", "2024-01-15"},
                {"#4520", "Jane Smith", "$150.00", "Pending", "2024-01-15"},
                {"#4519", "Bob Wilson", "$425.50", "Completed", "2024-01-14"},
                {"#4518", "Alice Brown", "$89.99", "Failed", "2024-01-14"},
                {"#4517", "Charlie Davis", "$1,200.00", "Completed", "2024-01-13"}
        };
        TextColor[] headerColors = {COLOR_CYAN, COLOR_WHITE, COLOR_GREEN, COLOR_YELLOW, COLOR_GRAY};
        TextColor[] rowColors = {COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE};

        tablePanel.addComponent(new DataTableComponent(" TRANSACTIONS TABLE ", headers, data, headerColors, rowColors));
        parent.addComponent(tablePanel);
    }

    private static void buildBottomPanels(Panel parent) {
        Panel bottomPanel = createGridPanel(2);
        configureGridLayout(bottomPanel, 1, 1, 1, 1, 1);

        buildActivityFeed(bottomPanel);
        buildQuickActions(bottomPanel);

        parent.addComponent(bottomPanel);
    }

    private static void buildActivityFeed(Panel parent) {
        Panel activityPanel = createGridPanel(1);
        configureGridLayout(activityPanel, 0, 0, 0, 1, 1);

        Panel actTitle = new Panel();
        actTitle.addComponent(new SectionTitle(" RECENT ACTIVITY "));
        activityPanel.addComponent(actTitle);
        activityPanel.addComponent(createEmptySpace(0));

        String[][] activities = {
                {"[10:42]", "User registration - john@example.com"},
                {"[10:38]", "Order #4521 completed"},
                {"[10:35]", "Payment processed - $299.00"},
                {"[10:30]", "New team member added"},
                {"[10:25]", "Report generated successfully"},
                {"[10:20]", "API key rotated"},
                {"[10:15]", "Backup completed"}
        };
        TextColor[] actColors = {COLOR_CYAN, COLOR_GREEN, COLOR_GREEN, COLOR_YELLOW, COLOR_CYAN, COLOR_MAGENTA, COLOR_GRAY};

        for (int i = 0; i < activities.length; i++) {
            activityPanel.addComponent(new ActivityItem(activities[i][0], activities[i][1], actColors[i]));
            activityPanel.addComponent(createEmptySpace(0));
        }

        parent.addComponent(activityPanel);
    }

    private static void buildQuickActions(Panel parent) {
        Panel actionsPanel = createGridPanel(1);
        configureGridLayout(actionsPanel, 0, 0, 0, 1, 1);

        Panel actTitle = new Panel();
        actTitle.addComponent(new SectionTitle(" QUICK ACTIONS "));
        actionsPanel.addComponent(actTitle);
        actionsPanel.addComponent(createEmptySpace(0));

        Panel actionsBox = createGridPanel(1);
        configureGridLayout(actionsBox, 0, 1, 1, 1, 1);

        String[] actions = {"New Project", "Generate Report", "Invite Member", "Export Data", "System Config"};
        TextColor[] actionColors = {COLOR_CYAN, COLOR_GREEN, COLOR_YELLOW, COLOR_MAGENTA, COLOR_ORANGE};

        for (int i = 0; i < actions.length; i++) {
            actionsPanel.addComponent(new ActionItem(" ➤ " + actions[i], actionColors[i]));
            actionsPanel.addComponent(createEmptySpace(0));
        }

        Panel separator = new Panel(new LinearLayout(Direction.HORIZONTAL));
        separator.addComponent(new EmptySpace());
        actionsPanel.addComponent(separator);
        actionsPanel.addComponent(createEmptySpace(0));

        Panel buttonsRow = createGridPanel(2);
        configureGridLayout(buttonsRow, 0, 1, 1, 0, 0);

        Panel primaryBtn = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label primaryBtnLabel = new Label("  CREATE NEW  ");
        primaryBtnLabel.setForegroundColor(COLOR_CYAN);
        primaryBtn.addComponent(primaryBtnLabel);
        buttonsRow.addComponent(primaryBtn);

        buttonsRow.addComponent(new EmptySpace());

        Panel secondaryBtn = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Label secondaryBtnLabel = new Label("  VIEW ALL  ");
        secondaryBtnLabel.setForegroundColor(COLOR_GRAY);
        secondaryBtn.addComponent(secondaryBtnLabel);
        buttonsRow.addComponent(secondaryBtn);

        actionsPanel.addComponent(buttonsRow);
        actionsPanel.addComponent(createEmptySpace(0));

        int[][] donutData = new int[][]{{35, 25, 20, 20}};
        String[] donutLabels = {"Sales", "Marketing", "Dev", "Ops"};
        TextColor[] donutColors = {COLOR_CYAN, COLOR_GREEN, COLOR_YELLOW, COLOR_MAGENTA};
        actionsPanel.addComponent(new ChartComponent(ChartComponent.ChartType.DONUT_CHART,
                "BUDGET ALLOCATION", donutData[0], donutLabels, donutColors));

        parent.addComponent(actionsPanel);
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
        centerInfo.addComponent(new Label("  Lanterna Dashboard v2.4.1 ").setForegroundColor(COLOR_GRAY));
        centerInfo.addComponent(new EmptySpace());
        footerContent.addComponent(centerInfo);

        Panel statusText = new Panel(new LinearLayout(Direction.HORIZONTAL));
        statusText.addComponent(new EmptySpace());
        statusText.addComponent(new Label("  STATUS").setForegroundColor(COLOR_GRAY));
        statusText.addComponent(new EmptySpace());
        statusText.addComponent(new Label("Ready").setForegroundColor(COLOR_GREEN));
        statusText.addComponent(new EmptySpace());
        statusText.addComponent(new Label("●").setForegroundColor(COLOR_GREEN));
        footerContent.addComponent(statusText);

        footerPanel.addComponent(footerContent);
        parent.addComponent(footerPanel);
    }
}
