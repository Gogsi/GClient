package com.georgi.gclient.mods.combat;

import com.georgi.gclient.GClientUtils;
import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

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

        if(!(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow) && isDrawingBow == true){
            isDrawingBow = false;
        }

        if(isEnabled && isDrawingBow){
            EntityLiving nearestEntity = findNearestTarget();
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

    /**
     * Adapted from EntityAiNearestAttackableTarget
     * @return
     */
    private EntityLiving findNearestTarget() {
        BlockPos pos1 = player.getPosition().subtract(new Vec3i(targetRange,targetRange / 2,targetRange));
        BlockPos pos2 = player.getPosition().add(new Vec3i(targetRange,targetRange / 2,targetRange));

        AxisAlignedBB aabb = new AxisAlignedBB(pos1,pos2);

        Predicate<? super EntityLiving> targetEntitySelector = (e) -> {
            if (e == null) {
                return false;
            } else {
                return (EntitySelectors.NOT_SPECTATING.test(e) && EntitySelectors.IS_ALIVE.test(e) && canSee(player, e));
            }
        };

        List<EntityLiving> list = mc.world.getEntitiesWithinAABB(EntityLiving.class, aabb, targetEntitySelector);
        if (list.isEmpty()) {
            return null;
        } else {
            Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(player));
            return list.get(0);
        }
    }

    /**
     * Adapted from EntityLiving
     * @param e1
     * @param e2
     * @return
     */
    public boolean canSee(Entity e1, Entity e2) {
        return mc.world.rayTraceBlocks(new Vec3d(e1.posX, e1.posY + (double)e1.getEyeHeight(), e1.posZ), new Vec3d(e2.posX, e2.posY + (double)e2.getEyeHeight(), e2.posZ), RayTraceFluidMode.NEVER, true, false) == null;
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
