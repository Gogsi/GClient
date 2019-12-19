package com.georgi.gclient.mods.combat;

import com.georgi.gclient.GClientUtils;
import com.georgi.gclient.gui.GuiSlider;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_4;

public class ModKillAura extends ModBase {
    private boolean isDrawingBow = false;
    private float targetRange = 3.0f;

    public ModKillAura() {
        super("KillAura", "Kill Aura", "Attacks all targets in range", "Combat", GLFW_MOUSE_BUTTON_4);
        GuiSlider range = new GuiSlider("Target range", "The maximum distance for the aura", 0.1f, 4.0f, targetRange){
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

        if(isEnabled && mc.player.getCooledAttackStrength(0.0f) > 0.999f){
            List<LivingEntity> targets = GClientUtils.findTargets(mc, targetRange);
            if(targets.size() == 0) return;
            for(LivingEntity e : targets){
                mc.playerController.attackEntity(mc.player, e);
            }
        }
    }


}
