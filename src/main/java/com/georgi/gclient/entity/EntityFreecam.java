package com.georgi.gclient.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityFreecam extends PlayerEntity {
    public MovementInput movementInput;
    public double freecamSpeed = 10.0f;
    public double freecamAcceleration = 1.5f;

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
        Vec3d motion = getMotion();
        this.noClip = true;

        this.moveStrafing = movementInput.moveStrafe;
        this.moveForward = movementInput.moveForward;

        if(Math.abs(moveForward) + Math.abs(moveStrafing) > 0.01) {

            setMotion(motion.x * freecamAcceleration, motion.y, motion.z * freecamAcceleration);

            double speedMagnitude = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

            if (speedMagnitude > freecamSpeed) {
                setMotion(motion.x * freecamSpeed / speedMagnitude, motion.y, motion.z * freecamSpeed / speedMagnitude);
            }
        } else {
            setMotion(0,motion.y, 0);
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.gameSettings.keyBindJump.isKeyDown())
            setMotion(motion.x ,motion.y + freecamSpeed / 3.0, motion.z );


        if (mc.gameSettings.keyBindSneak.isKeyDown())
            setMotion(motion.x ,motion.y - freecamSpeed / 3.0, motion.z );

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
