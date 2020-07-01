package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.mods.ModBase;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

public class ModXray extends ModBase {

    public ModXray() {
        super("Xray", "X-Ray", "Allows you to see ores through walls", "Visuals",  GLFW_KEY_X);
    }

    @Override
    public void onEnabled()
    {
        this.mc.worldRenderer.loadRenderers(); // Reload chunks
    }

    @Override
    public void onDisabled()
    {
        this.mc.worldRenderer.loadRenderers(); // Reload chunks
    }

}
