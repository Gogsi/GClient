package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.gui.GuiCheckbox;
import com.georgi.gclient.mods.ModBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_BRACKET;

public class ModGlow extends ModBase {

    private boolean showPassive = true;
    private boolean showHostile = true;
    private boolean showPlayers = true;

    private Framebuffer entityOutlineFramebuffer;
    /** Stores the shader group for the entity_outline shader */
    private ShaderGroup entityOutlineShader;
    private EntityRendererManager renderManager;

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
        for(Entity e : mc.world.getAllEntities()){
            if(e instanceof LivingEntity && e != renderViewEntity) {
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
        Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();


        //Rerender every entity after enabling outline mode and placing it in the appropriate team for color.
        for(Entity entity : list) {
            //Get entity position
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;

            //
            EntityRenderer<Entity> render = this.renderManager.getRenderer(entity);

            if(render != null) {
                ScorePlayerTeam oldTeam = (ScorePlayerTeam) entity.getTeam();

                if (entity instanceof IMob) {
                    if (!showHostile) continue;
                    mc.world.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), hostileTeam);
                } else if (entity instanceof AnimalEntity) {
                    if (!showPassive) continue;
                    mc.world.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), passiveTeam);
                } else if (entity instanceof PlayerEntity) {
                    if (!showPlayers) continue;
                    mc.world.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), playerTeam);
                } else{
                    continue;
                }

                GlStateManager.disableDepthTest();
                GlStateManager.depthMask(false);
                GlStateManager.disableBlend();

                render.setRenderOutlines(true);
                render.doRender(entity, d0 - projectedView.x, d1 - projectedView.y, d2 - projectedView.z, f, partialTicks);
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
