package com.dashboard;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class DashboardApp {

    public static void main(String[] args) throws IOException {
        // Capa de terminal y pantalla
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Panel con el saludo
        Panel panel = new Panel();
        Label label = new Label("¡Hola, mundo!\nDashboard con Lanterna 3.1.5 y Java 11.\nListo y funcionando.");
        panel.addComponent(label);

        // Ventana principal
        BasicWindow window = new BasicWindow("Lanterna - Hello World");
        window.setComponent(panel);

        // GUI y bucle modal
        MultiWindowTextGUI gui = new MultiWindowTextGUI(
                screen,
                new DefaultWindowManager(),
                new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);

        screen.stopScreen();
    }
}
