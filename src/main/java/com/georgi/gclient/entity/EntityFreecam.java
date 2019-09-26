package com.georgi.gclient.entity;

import com.georgi.gclient.GClientUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityFreecam extends PlayerEntity {
    public double freecamSpeed = 0.33f;

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
        this.noClip = true;

        Vec3d motion = getMotion();

        Minecraft mc = Minecraft.getInstance();
        handleMoveInputs(mc);

        if(Math.abs(moveForward) + Math.abs(moveStrafing) > 0.01) {
            setMotion(0, 0.0d, 0);

            this.moveRelative((float) freecamSpeed, new Vec3d((double) this.moveStrafing, 0.0d, (double) this.moveForward));
        } else {
            setMotion(0, 0.0d, 0);
        }

        motion = getMotion();

        if (mc.gameSettings.keyBindJump.isKeyDown())
            setMotion(motion.x ,freecamSpeed, motion.z );


        if (mc.gameSettings.keyBindSneak.isKeyDown())
            setMotion(motion.x , -freecamSpeed, motion.z );

        super.livingTick();
    }

    private void handleMoveInputs(Minecraft mc) {
        if (mc.gameSettings.keyBindForward.isKeyDown())
        {
            this.moveForward = 1.0f;
        }
        else if (mc.gameSettings.keyBindBack.isKeyDown())
        {
            this.moveForward = -1.0f;
        }
        else{
            this.moveForward = 0.0f;
        }

        if (mc.gameSettings.keyBindLeft.isKeyDown())
        {
            this.moveStrafing = 1.0f;
        }
        else if (mc.gameSettings.keyBindRight.isKeyDown())
        {
            this.moveStrafing = -1.0f;
        }
        else{
            this.moveStrafing = 0.0f;
        }
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
