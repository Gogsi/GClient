package com.georgi.gclient.mods.movement;

import com.georgi.gclient.gui.GuiCheckbox;
import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.*;

public class ModSpeed extends ModBase {
    private float modAcceleration = 1.5f;
    private float maxSpeed = 1.00F;

    boolean autoSprint = true;

    public ModSpeed() {
        super("Speed", "Speed", "Increases speed", "Movement", GLFW_KEY_H);
        GuiCheckbox sprint = new GuiCheckbox("Auto sprint", null, autoSprint) {
            @Override
            public void onToggled() {
                autoSprint = value;
            }
        };
        settings.add(sprint);

        GuiSlider acceleration = new GuiSlider("Acceleration factor", "How fast you reach max speed", 1.0f, 5.0f, modAcceleration){
            @Override
            public void onValueChanged() {
                modAcceleration = this.value;
            }
        };
        settings.add(acceleration);

        GuiSlider speed = new GuiSlider("Maximum speed", "The top speed", 1.0f, 20.0f, maxSpeed * 10.0f){
            @Override
            public void onValueChanged() {
                maxSpeed = this.value / 10.0f;
            }
        };
        settings.add(speed);
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if(isEnabled && player.onGround && Math.abs(player.moveForward) + Math.abs(player.moveStrafing) > 0.01){
            if(autoSprint || !player.isSprinting()) player.setSprinting(true);

            player.motionX *= modAcceleration;
            player.motionZ *= modAcceleration;

            double speedMagnitude = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);

            if(speedMagnitude > maxSpeed){
                player.motionX *= maxSpeed / speedMagnitude;
                player.motionZ *= maxSpeed / speedMagnitude;
            }

        }

    }
}
