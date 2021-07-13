package me.dev.legacy.features.modules.combat;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;

public class Quiver extends Module {
   private final Setting<Integer> tickDelay = this.register(new Setting("TickDelay", 3, 0, 8));

   public Quiver() {
      super("Quiver", "Rotates and shoots yourself with good potion effects", Module.Category.COMBAT, true, false, false);
   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && mc.field_71439_g.func_184587_cr() && mc.field_71439_g.func_184612_cw() >= (Integer)this.tickDelay.getValue()) {
            mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(mc.field_71439_g.field_71109_bG, -90.0F, mc.field_71439_g.field_70122_E));
            mc.field_71442_b.func_78766_c(mc.field_71439_g);
         }

         List<Integer> arrowSlots = InventoryUtil.getItemInventory(Items.field_185167_i);
         if ((Integer)arrowSlots.get(0) == -1) {
            return;
         }

         int speedSlot = true;
         int strengthSlot = true;
         Iterator var4 = arrowSlots.iterator();

         while(var4.hasNext()) {
            Integer slot = (Integer)var4.next();
            if (PotionUtils.func_185191_c(mc.field_71439_g.field_71071_by.func_70301_a(slot)).getRegistryName().func_110623_a().contains("swiftness")) {
               int var6 = slot;
            } else if (((ResourceLocation)Objects.requireNonNull(PotionUtils.func_185191_c(mc.field_71439_g.field_71071_by.func_70301_a(slot)).getRegistryName())).func_110623_a().contains("strength")) {
               int var7 = slot;
            }
         }
      }

   }

   public void onEnable() {
   }

   private int findBow() {
      return InventoryUtil.getItemHotbar(Items.field_151031_f);
   }
}
