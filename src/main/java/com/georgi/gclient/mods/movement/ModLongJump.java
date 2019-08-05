package com.georgi.gclient.mods.movement;

import com.georgi.gclient.ModSettings;
import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModLongJump extends ModBase {
    public ModLongJump() {
        super("LJ", "Long Jump", "Increases the jump distance", "Movement", GLFW_KEY_K);
    }


    @SubscribeEvent
    @SuppressWarnings("Duplicates")
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if (isEnabled) {
            player.jumpMovementFactor = ModSettings.LONG_JUMP_INTENSITY;
        } else {
            player.jumpMovementFactor = 0.02F;
        }
    }
}
