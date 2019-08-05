package com.georgi.gclient.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiCheckbox extends GuiElement {
    protected boolean value;

    private final int TEXT_COLOR = 0xFFEEEEEE;
    private final int BG_COLOR = 0xFF555555;
    private final int OFF_COLOR = 0xFF111111;
    private final int ON_COLOR = 0xFF22FF22;

    protected GuiCheckbox(String name, boolean value) {
        super(name);
        this.value = value;
    }

    protected GuiCheckbox(String name, int x1, int y1, int width, int height, boolean value) {
        super(name);
        this.value = value;
        this.x1 = x1;
        this.x2 = x1 + width;
        this.y1 = y1;
        this.y2 = y1 + height;
    }

    private int checkX, checkY;

    @Override
    public void render(Gui gui, FontRenderer font, int mouseX, int mouseY) {
        if (x1 == 0 && y1 == 0 && x2 == 0 && y2 == 0) {
            System.out.println("Checkbox size 0");
            return;
        }

        Gui.drawRect(x1, y1, x2, y2, BG_COLOR);

        int centerX = x1 + (x2 - x1) / 2;
        int centerY = y1 + (y2 - y1) / 2;

        gui.drawCenteredString(font, name, centerX, centerY - 5, TEXT_COLOR);

        checkX = x1 + 5;
        checkY = centerY - 5;

        Gui.drawRect(checkX, checkY, checkX + 10, checkY + 10, OFF_COLOR);

        if (value) {
            Gui.drawRect(checkX + 2, checkY + 2, checkX + 8, checkY + 8, ON_COLOR);
        }


        if (mouseX >= x1 && mouseX <= x2 && mouseY >= 1 && mouseY <= y2) {
            //drawHoveringText(tooltip, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= checkX && mouseX <= checkX + 10
                && mouseY >= checkY && mouseY <= checkY + 10) {

            value = !value;
            onToggled();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseBtn, double mouseDX, double mouseDY) {
        return false;
    }

    public abstract void onToggled();


}
