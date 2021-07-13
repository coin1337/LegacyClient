package me.dev.legacy.features.modules.render;

import java.awt.Color;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkyColor extends Module {
   private Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
   private Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
   private Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
   private Setting<Boolean> rainbow = this.register(new Setting("Rainbow", true));
   private static SkyColor INSTANCE = new SkyColor();

   public SkyColor() {
      super("SkyColor", "Changes the color of the fog", Module.Category.RENDER, false, false, false);
   }

   private void setInstance() {
      INSTANCE = this;
   }

   public static SkyColor getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new SkyColor();
      }

      return INSTANCE;
   }

   @SubscribeEvent
   public void fogColors(FogColors event) {
      event.setRed((float)(Integer)this.red.getValue() / 255.0F);
      event.setGreen((float)(Integer)this.green.getValue() / 255.0F);
      event.setBlue((float)(Integer)this.blue.getValue() / 255.0F);
   }

   @SubscribeEvent
   public void fog_density(FogDensity event) {
      event.setDensity(0.0F);
      event.setCanceled(true);
   }

   public void onEnable() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   public void onDisable() {
      MinecraftForge.EVENT_BUS.unregister(this);
   }

   public void onUpdate() {
      if ((Boolean)this.rainbow.getValue()) {
         this.doRainbow();
      }

   }

   public void doRainbow() {
      float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0F};
      int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8F, 0.8F);
      this.red.setValue(color_rgb_o >> 16 & 255);
      this.green.setValue(color_rgb_o >> 8 & 255);
      this.blue.setValue(color_rgb_o & 255);
   }
}
