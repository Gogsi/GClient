package com.georgi.gclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;

public final class GClientUtils {
    public static void drawScaledCenteredString(FontRenderer fontRendererIn, String text, float x, float y, int color, float scale){
        final float originalScale = (float)Math.pow(scale, -1);

        x = (x / scale) - (fontRendererIn.getStringWidth(text) / 2.0f);
        y = y / scale;

        GlStateManager.scalef(scale,scale,scale);

        fontRendererIn.drawStringWithShadow(text, x, y, color);

        GlStateManager.scalef(originalScale,originalScale,originalScale);

    }

    public static void drawScaledString(FontRenderer fontRendererIn, String text, float x, float y, int color, float scale){
        final float originalScale = (float)Math.pow(scale, -1);

        x = x / scale;
        y = y / scale;

        GlStateManager.scalef(scale,scale,scale);

        fontRendererIn.drawStringWithShadow(text, x, y, color);

        GlStateManager.scalef(originalScale,originalScale,originalScale);

    }

    public final static void actionBarMsg(String msg) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.world.isRemote()) return;
        mc.player.sendStatusMessage(new TextComponentString(msg), true);
    }
}
