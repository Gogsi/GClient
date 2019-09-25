package com.georgi.gclient.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiWidget extends GuiElement {

    private final int TEXT_COLOR = 0xFFEEEEEE;
    private final int BG_COLOR = 0xFF444444;
    private final int OFF_COLOR = 0xFFA81818;

    public List<GuiElement> children;

    private boolean isDragging = false;
    private boolean isClosable = false;
    private int dragX = 0;
    private int dragY = 0;

    private int closeX1, closeY1;

    public GuiWidget(String name, int x1, int y1, int width, int height, boolean closable){
        super(name, null);
        children = new ArrayList<>();
        this.isClosable = closable;

        this.x1 = x1;
        this.x2 = x1 + width;
        this.y1 = y1;
        this.y2 = y1 + height;

        closeX1 = x2 - 15;
        closeY1 = y1 + 5;
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

            closeX1 = x2 - 15;
            closeY1 = y1 + 5;
        }

        Gui.drawRect(x1, y1, x2, y2, BG_COLOR);

        if(isClosable)
            Gui.drawRect(closeX1, closeY1, closeX1 + 10, closeY1 + 10, OFF_COLOR);

        int centerX = x1 + (x2-x1)/2;
        int centerY = y1 + (y2-y1)/2;

        gui.drawCenteredString(font, name + " Options", centerX, y1 + 5, TEXT_COLOR);

        for(GuiElement c : children){
            c.render(gui, font, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(isClosable && button == 0 && mouseX >= closeX1 && mouseX <= closeX1 + 10 && mouseY >= closeY1 && mouseY <= closeY1 + 10)
        {
            onClose();
            return true;
        }

        if(button == 0 && mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y1 + 20){
            isDragging = true;
            dragX = (int)mouseX;
            dragY = (int)mouseY;

            return true;
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
        for(GuiElement c : children){
            if(c.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseBtn, double mouseDX, double mouseDY) {
        for(GuiElement c : children){
            if(c.mouseDragged(mouseX, mouseY, mouseBtn, mouseDX, mouseDY)) return true;
        }
        return false;
    }

    public void addChild(GuiElement el){
        int newX1 = el.getX1() + x1;
        int newY1 = el.getY1() + y1;

        el.setSize(newX1, newY1, el.getWidth(), el.getHeight());

        children.add(el);
    }

    public abstract void onClose();
}
