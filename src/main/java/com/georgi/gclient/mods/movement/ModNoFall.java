package com.georgi.gclient.mods.movement;


import com.georgi.gclient.GClient;
import com.georgi.gclient.mods.ModBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_N;

public class ModNoFall extends ModBase {
    GClient mod;

    public ModNoFall(GClient mod) {
        super("NoFall", "No Fall", "Disables fall damage", "Movement", GLFW_KEY_N);
        this.mod = mod;
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        updateMC();
        if (!verifyLocalPlayer(event.getEntity())) return;

        if (isEnabled) {
            if(player.fallDistance > 2.0f || mod.fly.isEnabled){
                player.connection.sendPacket(new CPacketPlayer(true)); //Tell the server we are on the ground before we are high enough to take dmg.
            }

        }
    }
}
