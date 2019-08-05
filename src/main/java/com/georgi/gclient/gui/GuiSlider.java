package com.georgi.gclient.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public abstract class GuiSlider extends GuiElement {
    private final int TEXT_COLOR = 0xFFEEEEEE;
    private final int BG_COLOR = 0xFF555555;
    private final int ON_COLOR = 0xFF18A818;

    private float minValue;
    private float maxValue;
    public float value;

    private boolean isDragging = false;

    public GuiSlider(String name, String description, float minValue, float maxValue, float value){
        super(name, description);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;
    }

    public GuiSlider(String name, String description, int x1, int y1, int width, int height, float minValue, float maxValue, float value){
        super(name, description);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;

        this.x1 = x1;
        this.x2 = x1 + width;
        this.y1 = y1;
        this.y2 = y1 + height;
    }


    @Override
    public void render(Gui gui, FontRenderer font, int mouseX, int mouseY) {
        if(isDragging){
            float newValuePercent = (float) (mouseX - x1) / (x2 - x1);

            if(newValuePercent < 0.0f) newValuePercent = 0.0f;
            if(newValuePercent > 1.0f) newValuePercent = 1.0f;

            this.value = newValuePercent * (maxValue-minValue) + minValue;

            onValueChanged();
        }

        Gui.drawRect(x1, y1, x2, y2, BG_COLOR);

        int centerX = x1 + (x2 - x1) / 2;
        int centerY = y1 + (y2 - y1) / 2;

        float valuePercent = (value - minValue) / (maxValue - minValue);
        int valueX = (int)(valuePercent * (x2-x1)) + x1;

        Gui.drawRect(x1, y1, valueX, y2, ON_COLOR);

        gui.drawCenteredString(font, name + ": " + String.format("%.2f", value), centerX, centerY - 5, TEXT_COLOR);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2){
            isDragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(isDragging){
            isDragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseBtn, double mouseDX, double mouseDY) {
        return false;
    }


    public abstract void onValueChanged();
}
