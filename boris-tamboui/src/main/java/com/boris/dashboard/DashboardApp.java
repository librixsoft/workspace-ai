package com.boris.dashboard;

import static dev.tamboui.toolkit.Toolkit.markupText;

/**
 * Main entry point for the TamboUI Dashboard application.
 * Uses the Toolkit layer for declarative component-based UI.
 */
public class DashboardApp {

    public static void main(String[] args) throws Exception {
        // Create and run the dashboard
        DashboardView dashboard = new DashboardView();
        dashboard.render();
    }
}
