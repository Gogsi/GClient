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

    //Static so it persists when closing the menu
    public List<GuiWidget> categoryWidgets;

    //Static so it persists when closing the menu
    private static Map<String, GuiWidget> openModSettings;

    public GClientOptionsGui(GClient mod) {
        super();
        this.mod = mod;
        categoryWidgets = new ArrayList<>();
        if (openModSettings == null)
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

        int movementCnt = 0;
        int visualsCnt = 0;
        int combatCnt = 0;
        int otherCnt = 0;

        GuiWidget movementWidget = new GuiWidget("Movement", movementX, movementY, 100, 10, false) {
            @Override
            public void onClose() {
            }
        };

        GuiWidget visualsWidget = new GuiWidget("Visuals", visualsX, visualsY, 100, 10, false) {
            @Override
            public void onClose() {
            }
        };

        GuiWidget combatWidget = new GuiWidget("Combat", combatX, combatY, 100, 10, false) {
            @Override
            public void onClose() {
            }
        };

        GuiWidget otherWidget = new GuiWidget("Other", otherX, otherY, 100, 10, false) {
            @Override
            public void onClose() {
            }
        };

        for (ModBase hack : mod.mods) {
            int y;
            GuiWidget widgetCategory;
            switch (hack.category) {
                case "Visuals":
                    y = (++visualsCnt) * buttonHeight;
                    widgetCategory = visualsWidget;
                    break;
                case "Movement":
                    y = (++movementCnt) * buttonHeight;
                    widgetCategory = movementWidget;
                    break;
                case "Combat":
                    y = (++combatCnt) * buttonHeight;
                    widgetCategory = combatWidget;
                    break;
                case "Other":
                default:
                    y = (++otherCnt) * buttonHeight;
                    widgetCategory = otherWidget;

            }
            widgetCategory.addChild(new GuiCheckbox(hack.displayName, hack.description, 0, y, buttonWidth, buttonHeight, hack.isEnabled) {
                @Override
                public void onToggled() {
                    hack.toggle();
                }
            });
            if(hack.settings.size() > 0) {
                widgetCategory.addChild(new GuiTextButton(">", "View mod options", buttonWidth, y, buttonHeight / 2, buttonHeight) {
                    @Override
                    public void onToggled() {
                        if (openModSettings.containsKey(hack.name)) {
                            openModSettings.remove(hack.name);
                            return;
                        }
                        addWidget(hack);
                    }
                });
            }
        }

        movementWidget.y2 = movementWidget.y1 + (movementCnt * buttonHeight);
        combatWidget.y2 = combatWidget.y1 + (combatCnt * buttonHeight);
        visualsWidget.y2 = visualsWidget.y1 + (visualsCnt * buttonHeight);
        otherWidget.y2 = otherWidget.y1 + (otherCnt * buttonHeight);

        categoryWidgets.add(movementWidget);
        categoryWidgets.add(visualsWidget);
        categoryWidgets.add(combatWidget);
        categoryWidgets.add(otherWidget);
    }

    private void addWidget(ModBase hack) {
        int widgetHeight = 10 + hack.settings.size() * 20;
        int x = width / 3 + (openModSettings.values().size() * 20);
        int y = height / 3 + (openModSettings.values().size() * 20);

        GuiWidget widget = new GuiWidget(hack.displayName, x, y, 160, widgetHeight, true) {
            @Override
            public void onClose() {
                openModSettings.remove(hack.name);
            }
        };

        int settingY = 20;
        for (GuiElement el : hack.settings) {
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


        for (GuiWidget w : categoryWidgets) {
            w.render(this, font, mouseX, mouseY);
        }
        for (GuiWidget w : openModSettings.values()) {
            w.render(this, font, mouseX, mouseY);
        }


        String popup = null;
        for (GuiWidget w : categoryWidgets) {
            for (GuiElement el : w.children) {
                if (isHoveringOver(el, mouseX, mouseY)) popup = el.description;
            }
        }
        for (GuiWidget w : openModSettings.values()) {
            for (GuiElement el : w.children) {
                if (isHoveringOver(el, mouseX, mouseY)) popup = el.description;
            }
        }

        if (popup != null) drawHoveringText(popup, mouseX, mouseY);
    }

    public boolean isHoveringOver(GuiElement el, int mouseX, int mouseY) {
        return mouseX >= el.x1 && mouseX <= el.x2 && mouseY >= el.y1 && mouseY <= el.y2 && el.description != null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiWidget w : categoryWidgets) {
            if (w.mouseClicked(mouseX, mouseY, button)) return true;
        }
        for (GuiWidget w : openModSettings.values()) {
            if (w.mouseClicked(mouseX, mouseY, button)) return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiWidget w : categoryWidgets) {
            if (w.mouseReleased(mouseX, mouseY, button)) return true;
        }
        for (GuiWidget w : openModSettings.values()) {
            if (w.mouseReleased(mouseX, mouseY, button)) return true;
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
