package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

public class ModXray extends ModBase {

    double defaultGamma;

    public ModXray() {
        super("Xray", "X-Ray", "Allows you to see ores through walls", "Visuals",  GLFW_KEY_X);
    }

    @Override
    public void onEnabled()
    {
        this.mc.worldRenderer.loadRenderers(); // Reload chunks
        defaultGamma = mc.gameSettings.gamma;
    }

    @Override
    public void onDisabled()
    {
        this.mc.worldRenderer.loadRenderers(); // Reload chunks
        mc.gameSettings.gamma = defaultGamma;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        updateMC();
        if (!verifyLocal()) return;

        if(isEnabled) mc.gameSettings.gamma = 16f;
    }
}
