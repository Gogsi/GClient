package com.georgi.gclient.mods.combat;

import com.georgi.gclient.GClientUtils;
import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

public class ModBowAimbot extends ModBase {
    private boolean isDrawingBow = false;
    private float targetRange = 120.0f;

    public ModBowAimbot() {
        super("BowAimbot", "Bow Aimbot", "Automatically aims for the nearest target when using a bow", "Combat", GLFW_KEY_T);
        GuiSlider range = new GuiSlider("Target range", "The maximum distance for the aimbot", 1.0f, 120.0f, targetRange){
            @Override
            public void onValueChanged() {
                targetRange = this.value;
            }
        };
        settings.add(range);
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if(!(player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof BowItem) && isDrawingBow == true){
            isDrawingBow = false;
        }

        if(isEnabled && isDrawingBow){
            LivingEntity nearestEntity = GClientUtils.findNearestTarget(mc, targetRange);
            if(nearestEntity == null) return;

            mc.player.lookAt(EntityAnchorArgument.Type.EYES, nearestEntity.getEyePosition(1.0f));

            Vec3d distance = nearestEntity.getEyePosition(1.0f).subtract(mc.player.getEyePosition(1.0f));
            double dx = Math.sqrt(distance.x * distance.x + distance.z * distance.z); // get the horizontal reach to target
            double dy = distance.y; // get the vertical height to target

            final double gravity = 20.0; // g = 0.05m/(tick^2) = 0.05m/(0.05s)^2 = 20 m/s^2
            final double arrowVelocity = 60.5; // velocity = 60.5m/s https://gaming.stackexchange.com/questions/348832/minecraft-arrow-speed-initial-velocity

            // Formula from https://en.wikipedia.org/wiki/Projectile_motion#Angle_%7F'%22%60UNIQ--postMath-0000003A-QINU%60%22'%7F_required_to_hit_coordinate_(x,y)
            double velocitySquared = arrowVelocity * arrowVelocity;
            double velocity4th = velocitySquared * velocitySquared;

            double f1 = velocity4th - gravity * (gravity * dx * dx + 2*dy*velocitySquared);
            if(f1 < 0) { // Angle is imaginary; Cannot reach target
                GClientUtils.actionBarMsg("Cannot reach target");
                return;
            }
            double sqrt = (float)Math.sqrt(f1);
            double pitchAngleRad = Math.atan((velocitySquared - sqrt) / (gravity * dx));

            double pitchAngle =  Math.toDegrees(pitchAngleRad);

            if(pitchAngle > 90.0f) pitchAngle = 90.0f; // Cap the angle
            if(pitchAngle < -90.0f) pitchAngle = -90.0f;

            player.rotationPitch = - (float)pitchAngle; // Minecraft pitch is negative when pointing up
        }
    }

    @SubscribeEvent
    public void onArrowNock(ArrowNockEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        isDrawingBow = true;
    }

    @SubscribeEvent
    public void onArrowLoose(ArrowLooseEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        isDrawingBow = false;
    }
}
