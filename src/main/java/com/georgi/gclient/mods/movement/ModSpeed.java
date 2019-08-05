package com.georgi.gclient.mods.movement;

import com.georgi.gclient.ModSettings;
import com.georgi.gclient.gui.GuiCheckbox;
import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModSpeed extends ModBase {

    private final float normalSpeed = 0.1f;
    final float maxSpeed = 1.00F; //  *24??
    boolean autoSprint = true;

    public ModSpeed() {
        super("Speed", "Speed", "Increases speed", "Movement", GLFW_KEY_H);
        GuiCheckbox sprint = new GuiCheckbox("Automatically sprint", true) {
            @Override
            public void onToggled() {
                autoSprint = value;
            }
        };
        settings.add(sprint);
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if(isEnabled && player.onGround && Math.abs(player.moveForward) + Math.abs(player.moveStrafing) > 0.01){
            if(autoSprint) player.setSprinting(true);

            player.motionX *= ModSettings.SPEED_MULT;
            player.motionZ *= ModSettings.SPEED_MULT;

            double speedMagnitude = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);

            if(speedMagnitude > maxSpeed){
                player.motionX *= maxSpeed / speedMagnitude;
                player.motionZ *= maxSpeed / speedMagnitude;
            }

        }

    }
}
