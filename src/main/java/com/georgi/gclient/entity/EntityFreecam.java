package com.georgi.gclient.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityFreecam extends EntityPlayer {
    public MovementInput movementInput;


    public EntityFreecam(World worldIn) {
        super(worldIn, new GameProfile(UUID.randomUUID(), "Freecam"));

        this.abilities.allowFlying = true;
        this.abilities.allowEdit = false;
        this.abilities.disableDamage = true;

        this.noClip = true;
    }

    @Override
    public void tick(){
        super.tick();
    }

    @Override
    public void livingTick(){
        motionY = 0;
        this.noClip = true;

        this.moveStrafing = movementInput.moveStrafe;
        this.moveForward = movementInput.moveForward;

        Minecraft mc = Minecraft.getInstance();

        if(mc.gameSettings.keyBindJump.isKeyDown())
            motionY += 0.1;

        if(mc.gameSettings.keyBindSneak.isKeyDown())
            motionY -= 0.1;

        super.livingTick();
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Override
    public boolean isServerWorld() {
        return true;
    }
}
