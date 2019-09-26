package com.georgi.gclient.mods.other;

import com.georgi.gclient.entity.EntityFreecam;
import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_U;

public class ModFreecam extends ModBase {
    EntityFreecam freecam;
    private float freecamAcceleration = 1.5f;
    private float freecamSpeed = 1.00F;

    public ModFreecam() {
        super("Freecam", "Freecam", "Allows the player to move his camera without moving the player", "Other", GLFW_KEY_U);
        GuiSlider acceleration = new GuiSlider("Acceleration factor", "How fast you reach max speed", 1.0f, 5.0f, freecamAcceleration){
            @Override
            public void onValueChanged() {
                freecamSpeed = this.value;
                if(freecam != null){
                    freecam.freecamSpeed = freecamSpeed;
                }
            }
        };
        settings.add(acceleration);

        GuiSlider speed = new GuiSlider("Maximum speed", "The top speed", 1.0f, 10.0f, freecamSpeed * 5.0f){
            @Override
            public void onValueChanged() {
                freecamSpeed = this.value / 5.0f;
                if(freecam != null){
                    freecam.freecamAcceleration = freecamAcceleration;
                }
            }
        };
        settings.add(speed);
    }

    @Override
    public void onEnabled(){
        AxisAlignedBB box = mc.player.getBoundingBox();

        freecam = new EntityFreecam(mc.world);
        freecam.freecamAcceleration = freecamAcceleration;
        freecam.freecamSpeed = freecamSpeed;
        freecam.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
        freecam.setBoundingBox(
                new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));

        mc.world.addEntity(freecam);
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
            freecam.rotationYawHead = player.rotationYawHead;
            freecam.movementInput = player.movementInput;

            mc.setRenderViewEntity(freecam);
        }
    }
}
