package com.georgi.gclient.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public abstract class GuiElement {
    protected String name;
    protected String description;

    protected int x1 = 0;
    protected int x2 = 0;
    protected int y1 = 0;
    protected int y2 = 0;

    public GuiElement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void render(Gui gui, FontRenderer font, int mouseX, int mouseY);
    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);
    public abstract boolean mouseReleased(double mouseX, double mouseY, int button);
    public abstract boolean mouseDragged(double mouseX, double mouseY, int mouseBtn, double mouseDX, double mouseDY) ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }


    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public int getWidth(){
        return x2 - x1;
    }

    public int getHeight(){
        return y2 - y1;
    }

    public void setSize(int x1, int y1, int width, int height){
        this.x1 = x1;
        this.x2 = x1 + width;
        this.y1 = y1;
        this.y2 = y1 + height;
    }
}
