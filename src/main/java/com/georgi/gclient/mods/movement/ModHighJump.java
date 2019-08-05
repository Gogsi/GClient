package com.georgi.gclient.mods.movement;

import com.georgi.gclient.ModSettings;
import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModHighJump extends ModBase {
    public ModHighJump() {
        super("HJ", "High Jump", "Increases the jump height", "Movement", GLFW_KEY_J);
    }


    @SubscribeEvent
    @SuppressWarnings("Duplicates")
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if (isEnabled) {
            player.addVelocity(0, ModSettings.HIGH_JUMP_INTENSITY, 0);
        }
    }
}
