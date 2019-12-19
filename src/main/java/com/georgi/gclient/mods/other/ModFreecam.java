package com.georgi.gclient.mods.other;

import com.georgi.gclient.GClientUtils;
import com.georgi.gclient.entity.EntityFreecam;
import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_U;

public class ModFreecam extends ModBase {
    EntityFreecam freecam;
    private float freecamSpeed = 0.33F;

    public ModFreecam() {
        super("Freecam", "Freecam", "Allows the player to move his camera without moving the player", "Other", GLFW_KEY_U);

        GuiSlider speed = new GuiSlider("Maximum speed", "The top speed", 0.0f, 1.0f, freecamSpeed ){
            @Override
            public void onValueChanged() {
                freecamSpeed = this.value;
                if(freecam != null){
                    freecam.freecamSpeed = freecamSpeed;
                }
            }
        };
        settings.add(speed);
    }

    @Override
    public void onEnabled(){
        AxisAlignedBB box = mc.player.getBoundingBox();

        freecam = new EntityFreecam(mc.world);
        freecam.freecamSpeed = freecamSpeed;
        freecam.copyLocationAndAnglesFrom(mc.player);
        freecam.setBoundingBox(
                new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
        freecam.inventory.copyInventory(player.inventory);

        mc.world.addEntity(freecam.getEntityId(), freecam);
        mc.setRenderViewEntity(freecam);

    }

    @Override
    public void onDisabled() {
        mc.world.removeEntityFromWorld(freecam.getEntityId());
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
            freecam.renderYawOffset = player.renderYawOffset;
            freecam.rotationYawHead = player.rotationYawHead;

            freecam.prevRotationPitch = player.prevRotationPitch;
            freecam.prevRotationYaw = player.prevRotationYaw;
            freecam.prevRenderYawOffset = player.prevRenderYawOffset;
            freecam.prevRotationYawHead = player.prevRotationYawHead;

            freecam.cameraYaw = player.cameraYaw;
            freecam.prevCameraYaw = player.prevCameraYaw;

            mc.setRenderViewEntity(freecam);
        }
    }
}
