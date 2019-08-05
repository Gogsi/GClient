package com.georgi.gclient.mods.movement;

import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModFly extends ModBase {
    private double flySpeed = 0.05f;

    public ModFly() {
        super("Fly", "Fly", "Allows the player to fly", "Movement", GLFW_KEY_M);

        GuiSlider speed = new GuiSlider("Fly speed", "The top flying speed", 0.1f, 1.0f, (float)flySpeed * 10.0f){
            @Override
            public void onValueChanged() {
                flySpeed = this.value / 10.0f;
            }
        };
        settings.add(speed);
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if(isEnabled || player.abilities.isCreativeMode){
            player.abilities.allowFlying = true;
            player.abilities.setFlySpeed(flySpeed);
        }else{
            player.abilities.allowFlying = false;

        }
    }
}
