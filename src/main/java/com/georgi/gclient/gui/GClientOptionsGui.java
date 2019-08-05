package com.georgi.gclient.gui;

import com.georgi.gclient.GClient;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GClientOptionsGui extends GuiScreen {

    GClient mod;
    ModBase selectedMod = null;

    List<GuiCheckbox> modButtons;
    List<GuiTextButton> modSettingButtons;
    Map<String, GuiWidget> openModSettings;

    public GClientOptionsGui(GClient mod) {
        super();
        this.mod = mod;
        modButtons = new ArrayList<>();
        modSettingButtons = new ArrayList<>();
        openModSettings = new HashMap<>();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void initGui() {
        super.initGui();
        this.buttons.clear();
        this.children.clear();

        final int buttonWidth = 90;
        final int buttonHeight = 20;

        final int movementX = 5;
        final int visualsX = width / 5 + 5;
        final int combatX = 2 * width / 5 + 5;
        final int otherX = 3 * width / 5 + 5;

        int movementY = 25;
        int visualsY = 25;
        int combatY = 25;
        int otherY = 25;

        for (ModBase hack : mod.mods) {
            int x, y;
            switch (hack.category) {
                case "Visuals":
                    x = visualsX;
                    y = visualsY;
                    visualsY += buttonHeight;
                    break;

                case "Movement":
                    x = movementX;
                    y = movementY;
                    movementY += buttonHeight;
                    break;
                case "Combat":
                    x = combatX;
                    y = combatY;
                    combatY += buttonHeight;
                    break;
                case "Other":
                default:
                    x = otherX;
                    y = otherY;
                    otherY += buttonHeight;
                    break;
            }

            if (hack.settings != null && hack.settings.size() > 0) {
                modSettingButtons.add(new GuiTextButton(">", x + buttonWidth, y, buttonHeight / 2, buttonHeight) {
                    @Override
                    public void onToggled() {
                        if(openModSettings.containsKey(hack.name)){
                            openModSettings.remove(hack.name);
                            return;
                        }
                        addWidget(hack);
                    }
                });

            }

            modButtons.add(new GuiCheckbox(hack.displayName, x, y, buttonWidth, buttonHeight, hack.isEnabled) {
                @Override
                public void onToggled() {
                    hack.toggle();
                }
            });
        }
    }

    private void addWidget(ModBase hack) {
        int widgetHeight = 10 + hack.settings.size() * 20;
        int x = width / 2 + (openModSettings.values().size() * 170);

        GuiWidget widget = new GuiWidget(hack.displayName, x, height/2 - widgetHeight / 2, 160, widgetHeight);

        int settingY = 20;
        for(GuiElement el : hack.settings){
            el.setSize(0, settingY, 160, 20);
            widget.addChild(el);
            settingY += 20;
        }

        openModSettings.put(hack.name, widget);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void render(int mouseX, int mouseY, float partialTicks) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;

        this.drawDefaultBackground();
        drawRect(0, 0, width, 3 * height / 8, Integer.MIN_VALUE);

        super.render(mouseX, mouseY, partialTicks);

        drawCenteredString(font, "GClient 1.0.0", width / 2, 10, 0xFFEEEE22);

        //Draw mod checkboxes
        for (GuiCheckbox btn : modButtons) {
            btn.render(this, font, mouseX, mouseY);
        }

        for(GuiTextButton btn : modSettingButtons){
            btn.render(this, font, mouseX, mouseY);
        }

        for(GuiWidget w : openModSettings.values()){
            w.render(this, font, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiCheckbox mod : modButtons) {
            if (mod.mouseClicked(mouseX, mouseY, button)) return true;
        }

        for (GuiTextButton btn : modSettingButtons) {
            if (btn.mouseClicked(mouseX, mouseY, button)) return true;
        }

        for(GuiWidget w : openModSettings.values()){
            if(w.mouseClicked(mouseX, mouseY, button)) return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for(GuiWidget w : openModSettings.values()){
            if(w.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseBtn, double mouseDX, double mouseDY) {
        /*for(GuiWidget w : openModSettings.values()){
            if(w.mouseDragged(mouseX, mouseY, mouseBtn, mouseDX, mouseDY)) return true;
        }*/

        return super.mouseDragged(mouseX, mouseY, mouseBtn, mouseDX, mouseDY);
    }

    @Override
    public void close() {
        super.close();
    }
}
