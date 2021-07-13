package me.dev.legacy.features.modules.movement;

import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.event.events.PushEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
   public Setting<Boolean> noPush = this.register(new Setting("NoPush", true));
   public Setting<Float> horizontal = this.register(new Setting("Horizontal", 0.0F, 0.0F, 100.0F));
   public Setting<Float> vertical = this.register(new Setting("Vertical", 0.0F, 0.0F, 100.0F));
   public Setting<Boolean> explosions = this.register(new Setting("Explosions", true));
   public Setting<Boolean> bobbers = this.register(new Setting("Bobbers", true));
   public Setting<Boolean> water = this.register(new Setting("Water", false));
   public Setting<Boolean> blocks = this.register(new Setting("Blocks", false));
   public Setting<Boolean> ice = this.register(new Setting("Ice", false));
   private static Velocity INSTANCE = new Velocity();

   public Velocity() {
      super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
      this.setInstance();
   }

   private void setInstance() {
      INSTANCE = this;
   }

   public static Velocity getINSTANCE() {
      if (INSTANCE == null) {
         INSTANCE = new Velocity();
      }

      return INSTANCE;
   }

   public void onUpdate() {
   }

   public void onDisable() {
   }

   @SubscribeEvent
   public void onPacketReceived(PacketEvent.Receive event) {
      if (event.getStage() == 0 && mc.field_71439_g != null) {
         if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity velocity = (SPacketEntityVelocity)event.getPacket();
            if (velocity.func_149412_c() == mc.field_71439_g.field_145783_c) {
               if ((Float)this.horizontal.getValue() == 0.0F && (Float)this.vertical.getValue() == 0.0F) {
                  event.setCanceled(true);
                  return;
               }

               velocity.field_149415_b *= (Integer)this.horizontal.getValue();
               velocity.field_149416_c *= (Integer)this.vertical.getValue();
               velocity.field_149414_d *= (Integer)this.horizontal.getValue();
            }
         }

         if (event.getPacket() instanceof SPacketEntityStatus && (Boolean)this.bobbers.getValue()) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.func_149160_c() == 31) {
               Entity entity = packet.func_149161_a(mc.field_71441_e);
               if (entity instanceof EntityFishHook) {
                  EntityFishHook fishHook = (EntityFishHook)entity;
                  if (fishHook.field_146043_c == mc.field_71439_g) {
                     event.setCanceled(true);
                  }
               }
            }
         }

         if ((Boolean)this.explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
            if ((Float)this.horizontal.getValue() == 0.0F && (Float)this.vertical.getValue() == 0.0F) {
               event.setCanceled(true);
               return;
            }

            SPacketExplosion sPacketExplosion;
            SPacketExplosion velocity2 = sPacketExplosion = (SPacketExplosion)event.getPacket();
            sPacketExplosion.field_149152_f *= (Float)this.horizontal.getValue();
            velocity2.field_149153_g *= (Float)this.vertical.getValue();
            velocity2.field_149159_h *= (Float)this.horizontal.getValue();
         }
      }

   }

   @SubscribeEvent
   public void onPush(PushEvent event) {
      if (event.getStage() == 0 && (Boolean)this.noPush.getValue() && event.entity.equals(mc.field_71439_g)) {
         if ((Float)this.horizontal.getValue() == 0.0F && (Float)this.vertical.getValue() == 0.0F) {
            event.setCanceled(true);
            return;
         }

         event.x = -event.x * (double)(Float)this.horizontal.getValue();
         event.y = -event.y * (double)(Float)this.vertical.getValue();
         event.z = -event.z * (double)(Float)this.horizontal.getValue();
      } else if (event.getStage() == 1 && (Boolean)this.blocks.getValue()) {
         event.setCanceled(true);
      } else if (event.getStage() == 2 && (Boolean)this.water.getValue() && mc.field_71439_g != null && mc.field_71439_g.equals(event.entity)) {
         event.setCanceled(true);
      }

   }
}
