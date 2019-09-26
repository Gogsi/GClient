package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.mods.ModBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;

public class ModChestESP extends ModBase {
    private List<AxisAlignedBB> chestList;

    public ModChestESP() {
        super("ChestESP", "Chest ESP","Allows you to see chests through wall", "Visuals",  GLFW_KEY_O);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        updateMC();
        if(!isEnabled) return; //|| !verifyLocal()) return;

        if (chestList == null) {
            return;
        }

        //Copy this to ensure no access while changing it
        List<AxisAlignedBB> chestCpy = new ArrayList<>(chestList);

        //Get the rendering entity (usually the player)
        Entity entity = Minecraft.getInstance().getRenderViewEntity();

        if(entity == null){
            System.out.println("No Rendering Entity");
            return;
        }

        //Rendering origin is player position. Shifting it to coordinates 0,0,0
        // so we can use the block's coords for the box.

        Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().setTranslation(-projectedView.x, -projectedView.y, - projectedView.z);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.lineWidth(Math.max(5.0F, (float)this.mc.mainWindow.getFramebufferWidth() / 1920.0F * 5.0F));
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepthTest();
        GlStateManager.matrixMode(5889);

        for (AxisAlignedBB pos : chestCpy) {
            WorldRenderer.drawBoundingBox(pos.minX, pos.minY, pos.minZ, pos.maxX, pos.maxY, pos.maxZ, 0.8f, 0.2f, 0.2f, 1.0f);
        }

        GlStateManager.matrixMode(5888);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepthTest();
        GlStateManager.enableTexture();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        //Reset rendering origin
        tessellator.getBuffer().setTranslation(0, 0, 0);
    }

    int updateTick = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if(!isEnabled) return;
        updateMC();

        updateTick ++;
        if(updateTick < 10) return;

        updateTick = 0;
        updatePosList();
    }


    public void updatePosList() {
        updateMC();
        if(!isEnabled) return;

        List<AxisAlignedBB> tempList = new ArrayList<>();

        if(mc == null || mc.world == null || mc.world.loadedTileEntityList == null) return;

        for (TileEntity e : mc.world.loadedTileEntityList) {
            if(e instanceof ChestTileEntity){
                ChestTileEntity c = (ChestTileEntity) e;
                tempList.add(new AxisAlignedBB((c.getPos())));
            }

            if(e instanceof DispenserTileEntity || e instanceof EnderChestTileEntity
               || e instanceof FurnaceTileEntity || e instanceof HopperTileEntity){
                tempList.add(new AxisAlignedBB((e.getPos())));
            }

        }

        for(Entity e : mc.world.getAllEntities()){
            if(e instanceof ChestMinecartEntity) {
                tempList.add(e.getRenderBoundingBox());
            }
        }

        chestList = new ArrayList<>(tempList);
    }
}
