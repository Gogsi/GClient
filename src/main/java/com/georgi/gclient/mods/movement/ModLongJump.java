package com.georgi.gclient.mods.movement;

import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModLongJump extends ModBase {

    public float jumpLength = 0.8f;

    public ModLongJump() {
        super("LJ", "Long Jump", "Increases the jump distance", "Movement", GLFW_KEY_K);

        GuiSlider length = new GuiSlider("Jump Length", null, 0.01f, 1.5f, jumpLength){
            @Override
            public void onValueChanged() {
                jumpLength = this.value;
            }
        };
        settings.add(length);
    }


    @SubscribeEvent
    @SuppressWarnings("Duplicates")
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if (isEnabled) {
            player.jumpMovementFactor = jumpLength;
        } else {
            player.jumpMovementFactor = 0.02F;
        }
    }
}
