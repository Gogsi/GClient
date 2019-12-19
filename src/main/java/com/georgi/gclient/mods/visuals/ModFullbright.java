package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;

public class ModFullbright extends ModBase {

    double defaultGamma;
    final double fullGama = 16.0;

    public ModFullbright() {
        super("Fullbright", "Full bright", "Increases the brightness of the game", "Visuals", GLFW_KEY_Y);
    }

    @Override
    public void onEnabled(){
        defaultGamma = mc.gameSettings.gammaSetting;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if (!isEnabled || event.side != LogicalSide.CLIENT) return;

        updateMC();
        if (!verifyLocal()) return;

        if(isEnabled) mc.gameSettings.gammaSetting = 16f;
        else mc.gameSettings.gammaSetting = defaultGamma;
    }
}
