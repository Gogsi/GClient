package com.georgi.gclient.gui;

import com.georgi.gclient.GClientUtils;
import com.sun.java.accessibility.util.java.awt.TextComponentTranslator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GClientHUD extends IngameGui {
    private int width;
    private int height;
    private Minecraft mc;

    private double speed = 0.0;

    public GClientHUD(Minecraft mc) {
        super(mc);
        MinecraftForge.EVENT_BUS.register(this);
        this.mc = mc;
        width = mc.mainWindow.getScaledWidth();
        height = mc.mainWindow.getScaledHeight();
    }

    @SubscribeEvent
    public void speedCheck(TickEvent.WorldTickEvent event) {
        mc = Minecraft.getInstance();
        if (mc.world == null || !mc.world.isRemote()) {
            return;
        }

        if (event.phase == TickEvent.Phase.END && mc != null && mc.player != null) {
            double motionX = mc.player.posX - mc.player.prevPosX;
            double motionZ = mc.player.posZ - mc.player.prevPosZ;

            double distance = Math.sqrt(motionX * motionX + motionZ * motionZ);
            speed = distance * 20;
        }
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        if (mc.world == null || !mc.world.isRemote()) {
            return;
        }

        width = mc.mainWindow.getScaledWidth();
        height = mc.mainWindow.getScaledHeight();

        GClientUtils.drawScaledCenteredString(mc.fontRenderer, "GClient 1.0.0", 5 * width / 6, 5 * height / 6, 0xFFFFFFFF, 0.7f);

        //Draw position
        String posString = "X/Y/Z: " + String.format("%.2f", mc.player.posX)
                + "/" + String.format("%.2f", mc.player.posY)
                + "/" + String.format("%.2f", mc.player.posZ);

        GClientUtils.drawScaledCenteredString(mc.fontRenderer, posString, 5 * width / 6, 5 * height / 6 + 10, 0xFFFFFFFF, 0.7f);

        //Draw speed
        String velString = "Speed: " + String.format("%.2f", speed * 3.6)
                + "km/h (" + String.format("%.2f", speed)
                + "m/s)";
        GClientUtils.drawScaledCenteredString(mc.fontRenderer, velString, 5 * width / 6, 5 * height / 6 + 20, 0xFFFFFFFF, 0.7f);

        //Draw held item info (stack size or durability)
        ItemStack heldItem = mc.player.getHeldEquipment().iterator().next();
        if(!heldItem.isEmpty()) {
            String heldItemString = I18n.format(heldItem.getTranslationKey()) + " ";

            if (heldItem.isDamageable()) {
                int durability = heldItem.getMaxDamage() - heldItem.getDamage();
                heldItemString += durability + "/" + heldItem.getMaxDamage();
            } else if (heldItem.isStackable()) {
                int stack = heldItem.getCount();
                heldItemString += stack + "/" + heldItem.getMaxStackSize();
            }

            GClientUtils.drawScaledCenteredString(mc.fontRenderer, heldItemString, 5 * width / 6, 5 * height / 6 + 30, 0xFFFFFFFF, 0.7f);
        }

    }
}
