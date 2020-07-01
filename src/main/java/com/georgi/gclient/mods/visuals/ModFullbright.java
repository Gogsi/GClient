package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;

public class ModFullbright extends ModBase {

    double defaultGamma;
    final double fullGama = 16.0;

    public ModFullbright() {
        super("Fullbright", "Full bright", "Increases the brightness of the game", "Visuals", GLFW_KEY_Y);
    }

    @Override
    public void onEnabled(){
        defaultGamma = mc.gameSettings.gamma;
    }

    @Override
    public void onDisabled(){
        mc.gameSettings.gamma = defaultGamma;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        updateMC();
        if (!verifyLocal()) return;

        if(isEnabled) mc.gameSettings.gamma = 16f;
    }
}
