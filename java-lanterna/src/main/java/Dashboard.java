package com.example;

import com.googlecode.lanterna.Terminal;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.ui.WindowBasedTextGUI;
import com.googlecode.lanterna.ui.component.BasicWindow;
import com.googlecode.lanterna.ui.component.BasicWindow;
import com.googlecode.lanterna.ui.component.Label;
import com.googlecode.lanterna.ui.component.Button;
import java.io.IOException;

public class Dashboard {
    public static void main(String[] args) throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        Terminal terminal = factory.createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        WindowBasedTextGUI gui = new WindowBasedTextGUI(screen);
        BasicWindow window = new BasicWindow("Hello");
        Label label = new Label("Hello, world!");
        window.setComponent(label);
        gui.addWindowAndWait(window);
    }
}
