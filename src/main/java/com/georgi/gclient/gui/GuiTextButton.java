package com.georgi.gclient.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public abstract class GuiTextButton extends GuiElement {

    private final int TEXT_COLOR = 0xFFEEEEEE;
    private final int BG_COLOR = 0xFF555555;

    protected GuiTextButton(String name, String description) {
        super(name, description);
    }

    protected GuiTextButton(String name, String description, int x1, int y1, int width, int height){
        super(name, description);

        this.x1 = x1;
        this.x2 = x1 + width;
        this.y1 = y1;
        this.y2 = y1 + height;
    }

    @Override
    public void render(Gui gui, FontRenderer font, int mouseX, int mouseY) {
        Gui.drawRect(x1, y1, x2, y2, BG_COLOR);

        int centerX = x1 + (x2-x1)/2;
        int centerY = y1 + (y2-y1)/2;

        gui.drawCenteredString(font, name, centerX, centerY - 5, TEXT_COLOR);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(mouseX >= x1 && mouseX <= x2
            && mouseY >= y1 && mouseY <= y2){

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
