package com.georgi.gclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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
        mc.player.sendStatusMessage(new TextComponentString(msg), true);
    }

    /**
     * Adapted from EntityAiNearestAttackableTarget
     * @return
     */
    public static EntityLiving findNearestTarget(Minecraft mc, float targetRange) {
        List<EntityLiving> list = findTargets(mc, targetRange);
        if (list.isEmpty()) {
            return null;
        } else {
            Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(mc.player));
            return list.get(0);
        }
    }

    /**
     * Adapted from EntityAiNearestAttackableTarget
     * @return
     */
    public static List<EntityLiving> findTargets(Minecraft mc, float targetRange) {
        BlockPos pos1 = mc.player.getPosition().subtract(new Vec3i(targetRange, targetRange, targetRange));
        BlockPos pos2 = mc.player.getPosition().add(new Vec3i(targetRange, targetRange, targetRange));

        AxisAlignedBB aabb = new AxisAlignedBB(pos1, pos2); // crude filtering on distance, the selector filters more precisely

        Predicate<? super EntityLiving> targetEntitySelector = (e) -> {
            if (e == null) {
                return false;
            } else {
                return (EntitySelectors.NOT_SPECTATING.test(e)
                        && EntitySelectors.IS_ALIVE.test(e) && canSee(mc.world, mc.player, e)
                        && mc.player.getPositionVector().distanceTo(e.getPositionVector()) <= targetRange);
            }
        };

        List<EntityLiving> list = mc.world.getEntitiesWithinAABB(EntityLiving.class, aabb, targetEntitySelector);
        return list;
    }
    /**
     * Adapted from EntityLiving
     * @param e1
     * @param e2
     * @return
     */
    public static boolean canSee(World w, Entity e1, Entity e2) {
        return w.rayTraceBlocks(new Vec3d(e1.posX, e1.posY + (double)e1.getEyeHeight(), e1.posZ), new Vec3d(e2.posX, e2.posY + (double)e2.getEyeHeight(), e2.posZ), RayTraceFluidMode.NEVER, true, false) == null;
    }
}
