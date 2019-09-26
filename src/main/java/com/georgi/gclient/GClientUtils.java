package com.georgi.gclient;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.command.arguments.EntitySelectorParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.util.math.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class GClientUtils {
    public static void drawScaledCenteredString(FontRenderer fontRendererIn, String text, float x, float y, int color, float scale){
        final float originalScale = (float)Math.pow(scale, -1);

        x = (x / scale) - (fontRendererIn.getStringWidth(text) / 2.0f);
        y = y / scale;

        GlStateManager.scalef(scale,scale,scale);
        fontRendererIn.drawStringWithShadow(text, x, y, color);
        GlStateManager.scalef(originalScale,originalScale,originalScale);

    }

    public static void drawScaledString(FontRenderer fontRendererIn, String text, float x, float y, int color, float scale){
        final float originalScale = (float)Math.pow(scale, -1);

        x = x / scale;
        y = y / scale;

        GlStateManager.scalef(scale,scale,scale);
        fontRendererIn.drawStringWithShadow(text, x, y, color);
        GlStateManager.scalef(originalScale,originalScale,originalScale);

    }

    public final static void actionBarMsg(String msg) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.world.isRemote()) return;
        mc.player.sendStatusMessage(new StringTextComponent(msg), true);
    }

    /**
     * Adapted from EntityAiNearestAttackableTarget
     * @return
     */
    public static LivingEntity findNearestTarget(Minecraft mc, float targetRange) {
        List<LivingEntity> list = findTargets(mc, targetRange);
        if (list.isEmpty()) {
            return null;
        } else {
            list.sort((o1, o2) -> {
                double d1, d2;
                d1 = mc.player.getPositionVec().distanceTo(o1.getPositionVector());
                d2 = mc.player.getPositionVec().distanceTo(o2.getPositionVector());
                return Double.compare(d1, d2);
            });
            return list.get(0);
        }
    }

    /**
     * Adapted from EntityAiNearestAttackableTarget
     * @return
     */
    public static List<LivingEntity> findTargets(Minecraft mc, float targetRange) {
        BlockPos pos1 = mc.player.getPosition().subtract(new Vec3i(targetRange, targetRange, targetRange));
        BlockPos pos2 = mc.player.getPosition().add(new Vec3i(targetRange, targetRange, targetRange));

        AxisAlignedBB aabb = new AxisAlignedBB(pos1, pos2); // crude filtering on distance, the selector filters more precisely

        List<LivingEntity> list = mc.world.getEntitiesWithinAABB(LivingEntity.class, aabb);
        List<LivingEntity> res = new ArrayList<>();
        for (LivingEntity entity : list) {
            if(!entity.equals(mc.player) && entity.isAlive() && entity.attackable() && canSee(mc.world,mc.player,entity)
                    && mc.player.getPositionVec().distanceTo(entity.getPositionVector()) <= targetRange)
                res.add(entity);
        }

        return res;
    }
    /**
     * Adapted from EntityLiving
     * @param e1
     * @param e2
     * @return
     */
    public static boolean canSee(World w, LivingEntity e1, LivingEntity e2) {
        return e1.canEntityBeSeen(e2);
    }
}
