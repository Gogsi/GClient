package com.georgi.gclient;

import com.georgi.gclient.gui.GClientHUD;
import com.georgi.gclient.gui.GClientOptionsGui;
import com.georgi.gclient.mods.*;

import com.georgi.gclient.mods.combat.ModBowAimbot;
import com.georgi.gclient.mods.movement.*;
import com.georgi.gclient.mods.other.ModFreecam;
import com.georgi.gclient.mods.visuals.ModChestESP;
import com.georgi.gclient.mods.visuals.ModFullbright;
import com.georgi.gclient.mods.visuals.ModGlow;
import net.minecraft.client.Minecraft;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSLASH;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("gclient")
public class GClient {
    private GClientHUD hud;
    private KeyBinding enableUI;

    public ModLongJump longJump;
    public ModHighJump highJump;
    public ModFly fly;
    public ModSpeed speed;
    public ModGlow glow;
    public ModNoFall noFall;
    public ModFreecam freecam;
    public ModChestESP chestESP;
    public ModFullbright fullbright;
    public ModBowAimbot bowAimbot;

    public ArrayList<ModBase> mods;

    public GClient() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        enableUI = new KeyBinding("key.enableUI", GLFW_KEY_BACKSLASH, "key.categories.gclient");
        ClientRegistry.registerKeyBinding(enableUI);

        longJump = new ModLongJump();
        highJump = new ModHighJump();
        fly = new ModFly();
        speed = new ModSpeed();
        glow = new ModGlow();
        noFall = new ModNoFall(this);
        freecam = new ModFreecam();
        chestESP = new ModChestESP();
        fullbright = new ModFullbright();
        bowAimbot = new ModBowAimbot();

        mods = new ArrayList<>();
        mods.addAll(Arrays.asList(longJump, highJump, fly, speed, glow, noFall, freecam, chestESP, fullbright, bowAimbot));
    }

    @SubscribeEvent
    public void onDrawHud(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.world == null) return;

        if (!mc.world.isRemote()) return;

        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;

        // Looks ugly if f3 is also open
        if (mc.gameSettings.showDebugInfo) return;

        if (hud == null) hud = new GClientHUD(mc);
        hud.render();
    }

    @SubscribeEvent
    @SuppressWarnings("Duplicates")
    public void onKeyInput(InputEvent.KeyInputEvent event){
        if(enableUI.isPressed()){
            Minecraft.getInstance().displayGuiScreen(new GClientOptionsGui(this));
        }

        for(ModBase hack: mods){
            if(hack.toggleKey.isPressed()){
                hack.toggle();
            }
        }
    }
}
