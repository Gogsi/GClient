package com.georgi.gclient.mods.visuals;

import com.georgi.gclient.mods.ModBase;
import com.georgi.gclient.gui.GuiCheckbox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_BRACKET;

public class ModGlow extends ModBase {

    private boolean showPassive = true;
    private boolean showHostile = true;
    private boolean showPlayers = true;

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
    @SuppressWarnings("Duplicates")
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocal()) return;

        Entity e = event.getEntity();

        if(isEnabled){
            if (e instanceof IMob && !showHostile) { e.setGlowing(false); return; } // IMob - hostiles + animals
            if (e instanceof IAnimal && !(e instanceof IMob) && !showPassive) { e.setGlowing(false); return; } //IMob is also IAnimal
            if (e instanceof EntityPlayer && !showPlayers) { e.setGlowing(false); return; }

            e.setGlowing(true);
        }else{
            e.setGlowing(false);
        }
    }
}
