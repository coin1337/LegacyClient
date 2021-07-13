package me.dev.legacy.features.modules.render;

import java.awt.Color;
import me.dev.legacy.event.events.Render2DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class HitMarkers extends Module {
   public final ResourceLocation image = new ResourceLocation("hitmarker.png");
   private int renderTicks = 100;
   public Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
   public Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
   public Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
   public Setting<Integer> alpha = this.register(new Setting("Alpha", 255, 0, 255));
   public Setting<Integer> thickness = this.register(new Setting("Thickness", 2, 1, 6));
   public Setting<Double> time = this.register(new Setting("Time", 20.0D, 1.0D, 50.0D));

   public HitMarkers() {
      super("HitMarkers", "hitmarker thingys", Module.Category.RENDER, false, false, false);
   }

   public void onRender2D(Render2DEvent event) {
      if ((double)this.renderTicks < (Double)this.time.getValue()) {
         new ScaledResolution(mc);
         this.drawHitMarkers();
      }

   }

   public void onEnable() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   public void onDisable() {
      MinecraftForge.EVENT_BUS.unregister(this);
   }

   @SubscribeEvent
   public void onAttackEntity(AttackEntityEvent event) {
      if (event.getEntity().equals(mc.field_71439_g)) {
         this.renderTicks = 0;
      }
   }

   @SubscribeEvent
   public void onTickClientTick(TickEvent event) {
      ++this.renderTicks;
   }

   public void drawHitMarkers() {
      ScaledResolution resolution = new ScaledResolution(mc);
      RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0F - 4.0F, (float)resolution.func_78328_b() / 2.0F - 4.0F, (float)resolution.func_78326_a() / 2.0F - 8.0F, (float)resolution.func_78328_b() / 2.0F - 8.0F, (float)(Integer)this.thickness.getValue(), (new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue())).getRGB());
      RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0F + 4.0F, (float)resolution.func_78328_b() / 2.0F - 4.0F, (float)resolution.func_78326_a() / 2.0F + 8.0F, (float)resolution.func_78328_b() / 2.0F - 8.0F, (float)(Integer)this.thickness.getValue(), (new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue())).getRGB());
      RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0F - 4.0F, (float)resolution.func_78328_b() / 2.0F + 4.0F, (float)resolution.func_78326_a() / 2.0F - 8.0F, (float)resolution.func_78328_b() / 2.0F + 8.0F, (float)(Integer)this.thickness.getValue(), (new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue())).getRGB());
      RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0F + 4.0F, (float)resolution.func_78328_b() / 2.0F + 4.0F, (float)resolution.func_78326_a() / 2.0F + 8.0F, (float)resolution.func_78328_b() / 2.0F + 8.0F, (float)(Integer)this.thickness.getValue(), (new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue())).getRGB());
   }
}
