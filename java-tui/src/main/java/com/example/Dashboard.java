package com.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.TerminalSession;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.terminal.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.ansi.AnsiTerminal;

public class Dashboard {

    public static void main(String[] args) {
        try {
            // Create terminal session
            Terminal terminal = AnsiTerminal.createTerminal();
            TerminalSession terminalSession = TerminalFacade.createTerminalSession(terminal, "Dashboard");
            
            // Create screen
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.doResizeIfRequired();

            // Get text graphics for drawing
            TextGraphics textGraphics = screen.newTextGraphics();

            // Draw "Hello World!" message with decorations
            textGraphics.setCharacter(25, 10, 'H');
            textGraphics.setCharacter(25, 11, 'e');
            textGraphics.setCharacter(25, 12, 'l');
            textGraphics.setCharacter(25, 13, 'l');
            textGraphics.setCharacter(25, 14, 'o');
            textGraphics.putString(26, 14, ", World!", SGR.BOLD);
            
            // Draw horizontal line
            for (int x = 10; x < 50; x++) {
                textGraphics.setCharacter(x, 16, '=');
            }

            // Draw status information
            textGraphics.putString(15, 18, "Lanterna Java TUI Demo", SGR.BOLD);
            textGraphics.putString(15, 20, "Press any key to exit", SGR.DIM);

            // Event loop
            while (true) {
                KeyStroke key = screen.readInput();
                if (key != null && key.getKeyType() == KeyType.EOF) {
                    break;
                }
                if (key != null && key.getKeyType() != KeyType.EOF) {
                    break;
                }
                Thread.sleep(100);
                screen.doResizeIfRequired();
            }

            // Cleanup
            screen.stopScreen();
            terminal.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
