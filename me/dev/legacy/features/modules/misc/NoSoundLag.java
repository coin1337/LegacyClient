package me.dev.legacy.features.modules.misc;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.modules.combat.AutoCrystal;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSoundLag extends Module {
   private static final Set<SoundEvent> BLACKLIST;
   private static NoSoundLag instance;
   public Setting<Boolean> crystals = this.register(new Setting("Crystals", true));
   public Setting<Boolean> armor = this.register(new Setting("Armor", true));
   public Setting<Float> soundRange = this.register(new Setting("SoundRange", 12.0F, 0.0F, 12.0F));

   public NoSoundLag() {
      super("NoSoundLag", "Prevents Lag through sound spam.", Module.Category.MISC, true, false, false);
      instance = this;
   }

   public static NoSoundLag getInstance() {
      if (instance == null) {
         instance = new NoSoundLag();
      }

      return instance;
   }

   public static void removeEntities(SPacketSoundEffect packet, float range) {
      BlockPos pos = new BlockPos(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
      ArrayList<Entity> toRemove = new ArrayList();
      Iterator var4 = mc.field_71441_e.field_72996_f.iterator();

      Entity entity;
      while(var4.hasNext()) {
         entity = (Entity)var4.next();
         if (entity instanceof EntityEnderCrystal && entity.func_174818_b(pos) <= MathUtil.square((double)range)) {
            toRemove.add(entity);
         }
      }

      var4 = toRemove.iterator();

      while(var4.hasNext()) {
         entity = (Entity)var4.next();
         entity.func_70106_y();
      }

   }

   @SubscribeEvent
   public void onPacketReceived(PacketEvent.Receive event) {
      if (event != null && event.getPacket() != null && mc.field_71439_g != null && mc.field_71441_e != null && event.getPacket() instanceof SPacketSoundEffect) {
         SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
         if ((Boolean)this.crystals.getValue() && packet.func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB && (AutoCrystal.getInstance().isOff() || !(Boolean)AutoCrystal.getInstance().sound.getValue() && AutoCrystal.getInstance().threadMode.getValue() != AutoCrystal.ThreadMode.SOUND)) {
            removeEntities(packet, (Float)this.soundRange.getValue());
         }

         if (BLACKLIST.contains(packet.func_186978_a()) && (Boolean)this.armor.getValue()) {
            event.setCanceled(true);
         }
      }

   }

   static {
      BLACKLIST = Sets.newHashSet(new SoundEvent[]{SoundEvents.field_187719_p, SoundEvents.field_191258_p, SoundEvents.field_187716_o, SoundEvents.field_187725_r, SoundEvents.field_187722_q, SoundEvents.field_187713_n, SoundEvents.field_187728_s});
   }
}
