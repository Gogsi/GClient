package com.georgi.gclient.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.Screen;

public abstract class GuiCheckbox extends GuiElement {
    protected boolean value;

    private final int TEXT_COLOR = 0xFFEEEEEE;
    private final int BG_COLOR = 0xFF555555;
    private final int OFF_COLOR = 0xFF111111;
    private final int ON_COLOR = 0xFF18A818;

    protected GuiCheckbox(String name, String description, boolean value) {
        super(name, description);
        this.value = value;
    }

    protected GuiCheckbox(String name, String description, int x1, int y1, int width, int height, boolean value) {
        super(name, description);
        this.value = value;
        this.x1 = x1;
        this.x2 = x1 + width;
        this.y1 = y1;
        this.y2 = y1 + height;
    }

    private int checkX, checkY;

    @Override
    public void render(Screen gui, FontRenderer font, int mouseX, int mouseY) {
        IngameGui.fill(x1, y1, x2, y2, BG_COLOR);

        int centerX = x1 + (x2 - x1) / 2;
        int centerY = y1 + (y2 - y1) / 2;

        gui.drawCenteredString(font, name, centerX, centerY - 5, TEXT_COLOR);

        checkX = x1 + 5;
        checkY = centerY - 5;

        IngameGui.fill(checkX, checkY, checkX + 10, checkY + 10, OFF_COLOR);

        if (value) {
            IngameGui.fill(checkX + 2, checkY + 2, checkX + 8, checkY + 8, ON_COLOR);
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
