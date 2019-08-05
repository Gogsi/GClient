package com.georgi.gclient.mods.movement;

import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModHighJump extends ModBase {

    public float jumpHeight = 0.5f;

    public ModHighJump() {
        super("HJ", "High Jump", "Increases the jump height", "Movement", GLFW_KEY_J);

        GuiSlider height = new GuiSlider("Jump Height", null, 0.1f, 1.5f, jumpHeight){
            @Override
            public void onValueChanged() {
                jumpHeight = this.value;
            }
        };
        settings.add(height);
    }


    @SubscribeEvent
    @SuppressWarnings("Duplicates")
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if (isEnabled) {
            player.addVelocity(0, jumpHeight, 0);
        }
    }
}
