package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.mods.ModBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
        double dx = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.getPartialTicks();
        double dy = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.getPartialTicks();
        double dz = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.getPartialTicks();

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().setTranslation(-dx, -dy, -dz);

        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.lineWidth(Math.max(5.0F, (float)this.mc.mainWindow.getFramebufferWidth() / 1920.0F * 5.0F));
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepthTest();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(1.0F, 1.0F, 0.999F);

        for (AxisAlignedBB pos : chestCpy) {
            event.getContext().drawBoundingBox(pos.minX, pos.minY, pos.minZ, pos.maxX, pos.maxY, pos.maxZ, 0.8f, 0.2f, 0.2f, 1.0f);
        }

        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepthTest();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

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

        if(mc == null || mc.world == null || mc.world.loadedEntityList == null) return;

        for (TileEntity e : mc.world.loadedTileEntityList) {
            if(e instanceof TileEntityChest){
                TileEntityChest c = (TileEntityChest) e;
                tempList.add(new AxisAlignedBB((c.getPos())));
            }

            if(e instanceof TileEntityDispenser || e instanceof TileEntityEnderChest
               || e instanceof TileEntityFurnace || e instanceof TileEntityHopper){
                tempList.add(new AxisAlignedBB((e.getPos())));
            }

        }

        for(Entity e : mc.world.loadedEntityList){
            if(e instanceof EntityMinecartChest) {
                tempList.add(e.getRenderBoundingBox());
            }
        }

        chestList = new ArrayList<>(tempList);
    }
}
