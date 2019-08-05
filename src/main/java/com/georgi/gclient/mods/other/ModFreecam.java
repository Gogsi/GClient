package com.georgi.gclient.mods.other;

import com.georgi.gclient.entity.EntityFreecam;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModFreecam extends ModBase {
    EntityFreecam freecam;

    public ModFreecam() {
        super("Freecam", "Freecam", "Allows the player to move his camera without moving the player", "Other", GLFW_KEY_U);
    }

    @Override
    public void onEnabled(){
        AxisAlignedBB box = mc.player.getBoundingBox();

        freecam = new EntityFreecam(mc.world);
        freecam.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
        freecam.setBoundingBox(
                new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
        freecam.inventory = player.inventory;
        freecam.inventoryContainer = player.inventoryContainer;

        mc.world.spawnEntity(freecam);
        mc.setRenderViewEntity(freecam);
    }

    @Override
    public void onDisabled() {
        mc.world.removeEntity(freecam);
        mc.setRenderViewEntity(mc.player);
        freecam = null;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event){
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.player)) return;

        if(isEnabled && freecam != null){
            freecam.rotationPitch = player.rotationPitch;
            freecam.rotationYaw = player.rotationYaw;
            freecam.rotationYawHead = player.rotationYawHead;
            freecam.movementInput = player.movementInput;

            mc.setRenderViewEntity(freecam);
        }
    }
}
