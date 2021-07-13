package me.dev.legacy.features.modules.combat;

import java.util.Iterator;
import me.dev.legacy.Client;
import me.dev.legacy.event.events.UpdateWalkingPlayerEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.DamageUtil;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.MathUtil;
import me.dev.legacy.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Aura extends Module {
   public static Entity target;
   private final Timer timer = new Timer();
   public Setting<Float> range = this.register(new Setting("Range", 6.0F, 0.1F, 7.0F));
   public Setting<Boolean> delay = this.register(new Setting("HitDelay", true));
   public Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
   public Setting<Boolean> onlySharp = this.register(new Setting("SwordOnly", true));
   public Setting<Float> raytrace = this.register(new Setting("Raytrace", 6.0F, 0.1F, 7.0F, "Wall Range."));
   public Setting<Boolean> players = this.register(new Setting("Players", true));
   public Setting<Boolean> mobs = this.register(new Setting("Mobs", false));
   public Setting<Boolean> animals = this.register(new Setting("Animals", false));
   public Setting<Boolean> vehicles = this.register(new Setting("Entities", false));
   public Setting<Boolean> projectiles = this.register(new Setting("Projectiles", false));
   public Setting<Boolean> tps = this.register(new Setting("TpsSync", true));
   public Setting<Boolean> packet = this.register(new Setting("Packet", false));

   public Aura() {
      super("Aura", "Kills aura.", Module.Category.COMBAT, true, false, false);
   }

   public void onTick() {
      if (!(Boolean)this.rotate.getValue()) {
         this.doKillaura();
      }

   }

   @SubscribeEvent
   public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
      if (event.getStage() == 0 && (Boolean)this.rotate.getValue()) {
         this.doKillaura();
      }

   }

   private void doKillaura() {
      if ((Boolean)this.onlySharp.getValue() && !EntityUtil.holdingWeapon(mc.field_71439_g)) {
         target = null;
      } else {
         int wait = !(Boolean)this.delay.getValue() ? 0 : (int)((float)DamageUtil.getCooldownByWeapon(mc.field_71439_g) * ((Boolean)this.tps.getValue() ? Client.serverManager.getTpsFactor() : 1.0F));
         if (this.timer.passedMs((long)wait)) {
            target = this.getTarget();
            if (target != null) {
               if ((Boolean)this.rotate.getValue()) {
                  Client.rotationManager.lookAtEntity(target);
               }

               EntityUtil.attackEntity(target, (Boolean)this.packet.getValue(), true);
               this.timer.reset();
            }
         }
      }
   }

   private Entity getTarget() {
      Entity target = null;
      double distance = (double)(Float)this.range.getValue();
      double maxHealth = 36.0D;
      Iterator var6 = mc.field_71441_e.field_73010_i.iterator();

      while(var6.hasNext()) {
         Entity entity = (Entity)var6.next();
         if (((Boolean)this.players.getValue() && entity instanceof EntityPlayer || (Boolean)this.animals.getValue() && EntityUtil.isPassive(entity) || (Boolean)this.mobs.getValue() && EntityUtil.isMobAggressive(entity) || (Boolean)this.vehicles.getValue() && EntityUtil.isVehicle(entity) || (Boolean)this.projectiles.getValue() && EntityUtil.isProjectile(entity)) && (!(entity instanceof EntityLivingBase) || !EntityUtil.isntValid(entity, distance)) && (mc.field_71439_g.func_70685_l(entity) || EntityUtil.canEntityFeetBeSeen(entity) || !(mc.field_71439_g.func_70068_e(entity) > MathUtil.square((double)(Float)this.raytrace.getValue())))) {
            if (target == null) {
               target = entity;
               distance = mc.field_71439_g.func_70068_e(entity);
               maxHealth = (double)EntityUtil.getHealth(entity);
            } else {
               if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                  target = entity;
                  break;
               }

               if (mc.field_71439_g.func_70068_e(entity) < distance) {
                  target = entity;
                  distance = mc.field_71439_g.func_70068_e(entity);
                  maxHealth = (double)EntityUtil.getHealth(entity);
               }

               if ((double)EntityUtil.getHealth(entity) < maxHealth) {
                  target = entity;
                  distance = mc.field_71439_g.func_70068_e(entity);
                  maxHealth = (double)EntityUtil.getHealth(entity);
               }
            }
         }
      }

      return target;
   }

   public String getDisplayInfo() {
      return target instanceof EntityPlayer ? target.func_70005_c_() : null;
   }
}
