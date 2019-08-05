package com.georgi.gclient.mods.movement;

import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModFly extends ModBase {

    public ModFly() {
        super("Fly", "Fly", "Allows the player to fly", "Movement", GLFW_KEY_M);
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if(isEnabled || player.abilities.isCreativeMode){
            player.abilities.allowFlying = true;
        }else{
            player.abilities.allowFlying = false;

        }
    }
}
