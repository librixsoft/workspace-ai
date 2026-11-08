package com.dashboard;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class DataTableComponent extends AbstractComponent<DataTableComponent> {

    private final String title;
    private final String[] headers;
    private final String[][] data;
    private final TextColor[] headerColors;
    private final TextColor[] rowColors;
    private final int[] colWidths;
    private final TextColor titleColor;
    private final TextColor borderColor;
    private final TextColor highlightColor;

    public DataTableComponent(String title, String[] headers, String[][] data,
                              TextColor[] headerColors, TextColor[] rowColors) {
        this.title = title;
        this.headers = headers;
        this.data = data;
        this.headerColors = headerColors;
        this.rowColors = rowColors;
        this.titleColor = new TextColor.RGB(0x88, 0x88, 0x88);
        this.borderColor = new TextColor.RGB(0x44, 0x44, 0x44);
        this.highlightColor = new TextColor.RGB(0x00, 0xcc, 0xcc);
        this.colWidths = calculateColWidths();
        setPreferredSize(new TerminalSize(calculateWidth(), Math.min(data.length + 6, 16)));
    }

    private int[] calculateColWidths() {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = Math.max(headers[i].length(), 8);
        }
        for (String[] row : data) {
            for (int i = 0; i < row.length && i < headers.length; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }
        return widths;
    }

    private int calculateWidth() {
        int w = 2;
        for (int i = 0; i < colWidths.length; i++) {
            w += colWidths[i] + 2;
        }
        return Math.max(w, 30);
    }

    @Override
    protected ComponentRenderer<DataTableComponent> createDefaultRenderer() {
        return new ComponentRenderer<DataTableComponent>() {
            @Override
            public TerminalSize getPreferredSize(DataTableComponent component) {
                return component.getPreferredSize();
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, DataTableComponent component) {
                component.drawTable(graphics);
            }
        };
    }

    private void drawTable(TextGUIGraphics g) {
        int w = g.getSize().getColumns();
        int h = g.getSize().getRows();
        if (w < 10 || h < 5) return;

        int titleY = 0;
        int borderY = 1;
        int headerY = 2;
        int dataStartY = 3;

        g.putString(1, titleY, " " + title + " ");
        g.setForegroundColor(borderColor);
        g.drawLine(0, borderY, w - 1, borderY, '-');

        int x = 1;
        g.setForegroundColor(borderColor);
        g.setCharacter(x, headerY, '+');
        x++;
        for (int i = 0; i < headers.length; i++) {
            for (int c = 0; c < colWidths[i]; c++) {
                if (x + c < w - 1) g.setCharacter(x + c, headerY, '-');
            }
            g.setForegroundColor(borderColor);
            if (x + colWidths[i] < w) g.setCharacter(x + colWidths[i], headerY, '+');
            x += colWidths[i] + 1;
        }

        g.setForegroundColor(new TextColor.RGB(0xaa, 0xaa, 0x00));
        x = 1;
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            int pad = colWidths[i] - header.length();
            g.putString(x, headerY, header);
            if (pad > 0) {
                g.putString(x + header.length(), headerY, " ".repeat(Math.min(pad, w - x - header.length())));
            }
            x += colWidths[i] + 2;
        }

        g.setForegroundColor(borderColor);
        for (int i = 0; i < headers.length; i++) {
            int colX = 1;
            for (int j = 0; j < i; j++) colX += colWidths[j] + 2;
            if (dataStartY < h) {
                for (int row = dataStartY; row < Math.min(dataStartY + data.length, h - 1); row++) {
                    g.setCharacter(colX, row, '|');
                }
            }
        }

        int rightColX = 1;
        for (int i = 0; i < headers.length; i++) {
            rightColX += colWidths[i] + 1;
        }
        for (int row = dataStartY; row < Math.min(dataStartY + data.length, h - 1); row++) {
            g.setCharacter(rightColX, row, '|');
        }

        for (int r = 0; r < data.length && (dataStartY + r) < h - 1; r++) {
            int rowY = dataStartY + r;
            x = 1;
            TextColor rowColor = rowColors != null && r < rowColors.length ? rowColors[r] : new TextColor.RGB(0xcc, 0xcc, 0xcc);
            g.setForegroundColor(rowColor);
            for (int i = 0; i < data[r].length && i < headers.length; i++) {
                String cell = data[r][i];
                int pad = colWidths[i] - cell.length();
                g.putString(x, rowY, cell);
                if (pad > 0) {
                    g.putString(x + cell.length(), rowY, " ".repeat(Math.min(pad, w - x - cell.length())));
                }
                x += colWidths[i] + 2;
            }
        }

        int bottomY = Math.min(dataStartY + data.length, h - 1);
        g.setForegroundColor(borderColor);
        x = 1;
        g.setCharacter(x, bottomY, '+');
        x++;
        for (int i = 0; i < headers.length; i++) {
            for (int c = 0; c < colWidths[i]; c++) {
                if (x + c < w - 1) g.setCharacter(x + c, bottomY, '-');
            }
            if (x + colWidths[i] < w) g.setCharacter(x + colWidths[i], bottomY, '+');
            x += colWidths[i] + 1;
        }
    }
}
