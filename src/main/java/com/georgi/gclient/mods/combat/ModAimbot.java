package com.georgi.gclient.mods.combat;

import com.georgi.gclient.GClientUtils;
import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
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
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;

public class ModAimbot extends ModBase {
    private boolean isDrawingBow = false;
    private float targetRange = 3.0f;

    public ModAimbot() {
        super("Aimbot", "Aimbot", "Automatically aims at the nearest target", "Combat", GLFW_MOUSE_BUTTON_3);
        GuiSlider range = new GuiSlider("Target range", "The maximum distance for the aimbot", 0.1f, 4.0f, targetRange){
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

        if(isEnabled){
            EntityLiving nearestEntity = GClientUtils.findNearestTarget(mc, targetRange);
            if(nearestEntity == null) return;

            mc.player.lookAt(EntityAnchorArgument.Type.EYES, nearestEntity.getEyePosition(1.0f));
        }
    }


}
