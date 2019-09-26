package com.georgi.gclient.mods;

import com.georgi.gclient.GClientUtils;
import com.georgi.gclient.gui.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;

public abstract class ModBase {
    protected Minecraft mc;
    protected ClientPlayerEntity player;

    public String name;
    public String displayName;
    public String description;
    public String category;

    public ArrayList<GuiElement> settings;

    public boolean isEnabled = false;
    public KeyBinding toggleKey;

    public ModBase(String name, String displayName, String description, String category, int defaultKey) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.category = category;

        settings = new ArrayList<>();

        toggleKey = new KeyBinding("key.enable" + name, defaultKey, "key.categories.gclient");
        ClientRegistry.registerKeyBinding(toggleKey);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public final void toggle() {
        isEnabled = !isEnabled;
        GClientUtils.actionBarMsg(displayName + " " + (isEnabled ? "enabled" : "disabled"));

        updateMC();

        if (isEnabled) onEnabled();
        else onDisabled();
    }

    public void onEnabled(){}

    public void onDisabled(){}

    protected final void updateMC(){
        mc = Minecraft.getInstance();
        player = mc.player;
    }

    /**
     * Verifies that the event is fired on the local client (in case of local server).
     *
     * @return Whether the event is fired on the local client.
     */
    protected final boolean verifyLocal() {
        if (mc == null || mc.world == null || !mc.world.isRemote()) return false;
        return true;
    }

    /**
     * Verifies that the player is us.
     *
     * @return Whether the player is us.
     */
    protected final boolean verifyLocalPlayer(Entity e) {
        if(player == null || e == null) return false;
        return player.equals(e);
    }
}
