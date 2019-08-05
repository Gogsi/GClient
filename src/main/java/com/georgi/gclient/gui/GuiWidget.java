package com.georgi.gclient.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.List;

public class GuiWidget extends GuiElement {

    private final int TEXT_COLOR = 0xFFEEEEEE;
    private final int BG_COLOR = 0xFF444444;

    private List<GuiElement> children;

    private boolean isDragging = false;
    private int dragX = 0;
    private int dragY = 0;

    public GuiWidget(String name) {
        super(name);
        children = new ArrayList<>();
    }

    public GuiWidget(String name, int x1, int y1, int width, int height){
        super(name);
        children = new ArrayList<>();

        this.x1 = x1;
        this.x2 = x1 + width;
        this.y1 = y1;
        this.y2 = y1 + height;
    }

    @Override
    public void render(Gui gui, FontRenderer font, int mouseX, int mouseY) {
        if(isDragging){
            this.setSize(x1 + (mouseX - dragX), y1 + (mouseY - dragY), getWidth(), getHeight());
            for(GuiElement c : children){
                c.setSize(c.x1 + (mouseX - dragX), c.y1 + (mouseY - dragY), c.getWidth(), c.getHeight());
            }
            dragX = mouseX;
            dragY = mouseY;
        }

        Gui.drawRect(x1, y1, x2, y2, BG_COLOR);

        int centerX = x1 + (x2-x1)/2;
        int centerY = y1 + (y2-y1)/2;

        gui.drawCenteredString(font, name + " Options", centerX, y1 + 5, TEXT_COLOR);

        for(GuiElement c : children){
            c.render(gui, font, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(button == 0 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y1 + 20){
            isDragging = true;
            dragX = (int)mouseX;
            dragY = (int)mouseY;
        }

        for(GuiElement c : children){
           if(c.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button){
        if(isDragging) {
            isDragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseBtn, double mouseDX, double mouseDY) {
        return false;
    }

    public void addChild(GuiElement el){
        int newX1 = el.getX1() + x1;
        int newY1 = el.getY1() + y1;

        el.setSize(newX1, newY1, el.getWidth(), el.getHeight());

        children.add(el);
    }
}
