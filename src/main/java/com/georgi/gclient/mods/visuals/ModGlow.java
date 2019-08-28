package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.GClientUtils;
import com.georgi.gclient.mods.ModBase;
import com.georgi.gclient.gui.GuiCheckbox;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.biome.Biome.LOGGER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_BRACKET;

public class ModGlow extends ModBase {

    private boolean showPassive = true;
    private boolean showHostile = true;
    private boolean showPlayers = true;

    private Framebuffer entityOutlineFramebuffer;
    /** Stores the shader group for the entity_outline shader */
    private ShaderGroup entityOutlineShader;
    private RenderManager renderManager;

    ScorePlayerTeam passiveTeam;
    ScorePlayerTeam hostileTeam;
    ScorePlayerTeam playerTeam;

    TextFormatting passiveColor = TextFormatting.AQUA;
    TextFormatting hostileColor = TextFormatting.DARK_RED;
    TextFormatting playerColor = TextFormatting.GOLD;

    public ModGlow() {
        super("Glow", "Glow", "Allows you to see entities through wall", "Visuals",  GLFW_KEY_LEFT_BRACKET);

        GuiCheckbox passive = new GuiCheckbox("Render passive", "Render animals, villagers and other passive mobs", showPassive) {
            @Override
            public void onToggled() {
                showPassive = value;
            }
        };
        settings.add(passive);
        GuiCheckbox hostile = new GuiCheckbox("Render hostile", "Render zombies, skeletons and other hostiles",  showHostile) {
            @Override
            public void onToggled() {
                showHostile = value;
            }
        };
        settings.add(hostile);
        GuiCheckbox players = new GuiCheckbox("Render players", "Render other players", showPlayers) {
            @Override
            public void onToggled() {
                showPlayers = value;
            }
        };
        settings.add(players);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!isEnabled) return;

        updateMC();

        if(passiveTeam == null || hostileTeam == null || playerTeam == null){
            passiveTeam = new ScorePlayerTeam(mc.world.getScoreboard(), "__passiveTeam");
            passiveTeam.setColor(passiveColor);

            hostileTeam = new ScorePlayerTeam(mc.world.getScoreboard(), "__hostileTeam");
            hostileTeam.setColor(hostileColor);

            playerTeam = new ScorePlayerTeam(mc.world.getScoreboard(), "__playerTeam");
            playerTeam.setColor(playerColor);

        }

        this.renderManager = mc.getRenderManager();
        Entity renderViewEntity = this.mc.getRenderViewEntity();

        List<Entity> list = new ArrayList<>();
        for(Entity e : mc.world.loadedEntityList){
            if(e instanceof EntityLiving && e != renderViewEntity) {
                list.add(e);
            }
        }

        renderGlow(list, event.getPartialTicks());
    }

    @SuppressWarnings("Duplicates")
    public void renderGlow(List<Entity> list, float partialTicks){
        GlStateManager.pushMatrix();

        Entity renderViewEntity = this.mc.getRenderViewEntity();

        //Get Camera position
        double dx = renderViewEntity.lastTickPosX + (renderViewEntity.posX - renderViewEntity.lastTickPosX) * (double)partialTicks;
        double dy = renderViewEntity.lastTickPosY + (renderViewEntity.posY - renderViewEntity.lastTickPosY) * (double)partialTicks;
        double dz = renderViewEntity.lastTickPosZ + (renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * (double)partialTicks;

        //Rerender every entity after enabling outline mode and placing it in the appropriate team for color.
        for(Entity entity : list) {
            //Get entity position
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;

            //
            Render<Entity> render = this.renderManager.getEntityRenderObject(entity);

            if(render != null) {
                ScorePlayerTeam oldTeam = (ScorePlayerTeam) entity.getTeam();

                if (entity instanceof IMob) {
                    if (!showHostile) continue;
                    mc.world.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), hostileTeam);
                } else if (entity instanceof IAnimal) {
                    if (!showPassive) continue;
                    mc.world.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), passiveTeam);
                } else if (entity instanceof EntityPlayer) {
                    if (!showPlayers) continue;
                    mc.world.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), playerTeam);
                } else{
                    continue;
                }

                GlStateManager.disableDepthTest();
                GlStateManager.depthMask(false);
                GlStateManager.disableBlend();

                render.setRenderOutlines(true);
                render.doRender(entity, d0 - dx, d1 - dy, d2 - dz, f, partialTicks);
                render.setRenderOutlines(false);

                GlStateManager.depthMask(true);
                GlStateManager.enableDepthTest();
                GlStateManager.enableBlend();

                mc.world.getScoreboard().removePlayerFromTeams(entity.getScoreboardName());
                if (oldTeam != null) mc.world.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), oldTeam);
            }

        }

        //Reset stuff - seems to break ChestESP without it
        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();
        GlStateManager.enableBlend();

        GlStateManager.popMatrix();
    }

}
