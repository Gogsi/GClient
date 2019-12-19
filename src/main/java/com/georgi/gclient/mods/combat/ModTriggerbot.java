package com.georgi.gclient.mods.combat;

import com.georgi.gclient.mods.ModBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;

public class ModTriggerbot extends ModBase {
    private boolean isDrawingBow = false;
    private float targetRange = 4.0f;

    public ModTriggerbot() {
        super("Triggerbot", "Triggerbot", "Automatically attacks a target in the crosshair", "Combat", GLFW_KEY_H);
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocal() || !verifyLocalPlayer(event.getEntity())) return;

        if(isEnabled){
            if(mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY){
                EntityRayTraceResult entityRes = (EntityRayTraceResult)mc.objectMouseOver;
                if(entityRes.getEntity() instanceof LivingEntity) {
                    if (mc.player.getCooledAttackStrength(0.0f) > 0.999f)
                        mc.playerController.attackEntity(mc.player, entityRes.getEntity());
                }
            }

        }
    }


}
