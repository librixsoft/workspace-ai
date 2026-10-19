package com.gemma.lanterna;

import com.googlecode.github.mabe02.lanterna.Terminal;
import com.googlecode.github.mabe02.lanterna.screen.Screen;
import com.googlecode.github.mabe02.lanterna.terminal.TerminalSize;
import com.googlecode.github.mabe02.lanterna.widgets.TextComponent;
import com.googlecode.github.mabe02.lanterna.widgets.Widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard {

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        
        // Configurar la pantalla de Lanterna
        Screen screen = terminal.getScreen();
        
        // 1. Crear contenedores de widgets
        List<Widget> dashboardWidgets = new ArrayList<>();

        // Título principal
        TextComponent title = new TextComponent("=== GEMMA AI DASHBOARD V1.0 ===");
        title.setFontSize(1);
        dashboardWidgets.add(title);

        // Sección de Estado
        TextComponent status = new TextComponent("Estado del Sistema: OPERATIVO");
        status.setFontSize(0);
        dashboardWidgets.add(status);
        
        // Separador
        TextComponent separator = new TextComponent("-------------------------------------------");
        separator.setFontSize(0);
        dashboardWidgets.add(separator);

        // Widgets de información simulada
        dashboardWidgets.add(new TextComponent("Usuarios Conectados: 42"));
        dashboardWidgets.add(new TextComponent("Tiempo Restante: 02:30:15"));
        dashboardWidgets.add(new TextComponent("Uso de CPU: 15.2%"));
        
        // Simulación de un panel de métricas (usando texto simple por simplicidad)
        dashboardWidgets.add(new TextComponent("\n--- MÉTODOS DE MONITORIZACIÓN ---\n"));
        dashboardWidgets.add(new TextComponent("1. Procesamiento de Languaje Natural (PLN): OK"));
        dashboardWidgets.add(new TextComponent("2. Gestión de Bases de Datos (DB): OK"));
        dashboardWidgets.add(new TextComponent("3. Conectividad de Red: OK"));


        // 2. Simular el layout y dibujar
        try {
            // Limpiar la pantalla antes de dibujar
            screen.clear();

            // Nota: Lanterna es complejo para un layout perfecto sin un motor avanzado. 
            // Aquí se dibujan los componentes secuencialmente para simular un dashboard.
            int y = 0;
            for (Widget widget : dashboardWidgets) {
                // Esto es una simplificación extrema para el ejemplo
                if (widget instanceof TextComponent) {
                    String text = ((TextComponent) widget).getText();
                    screen.putString(0, y, text);
                    y++;
                }
                if (y >= 15) {
                    // Si llegamos a fondo de pantalla, rompemos
                    break;
                }
            }
            
            // Forzar el refresco de la pantalla
            screen.refresh();

        } catch (IOException e) {
            System.err.println("Error al dibujar el dashboard: " + e.getMessage());
        }
    }
}