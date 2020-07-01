package com.georgi.gclient;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConfig implements IMixinConnector {
    @Override
    public void connect() {
        //MixinBootstrap.init();
        System.out.println("GClient Mixins loaded");
       Mixins.addConfiguration("mixins.gclient.json");
    }
    ///Mixin
}
