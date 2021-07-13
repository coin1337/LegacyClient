package me.dev.legacy.features.modules.player;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;

public class FastPlace extends Module {
   private Setting<FastPlace.Item> Items;

   public FastPlace() {
      super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
      this.Items = this.register(new Setting("Items", FastPlace.Item.XP));
   }

   public void onUpdate() {
      if (!fullNullCheck()) {
         if (((FastPlace.Item)this.Items.getValue()).equals(FastPlace.Item.XP)) {
            if (((FastPlace.Item)this.Items.getValue()).equals(FastPlace.Item.XP) && InventoryUtil.holdingItem(ItemExpBottle.class)) {
               mc.field_71467_ac = 0;
            }
         } else {
            mc.field_71467_ac = 0;
         }

      }
   }

   private static enum Item {
      XP,
      ALL;
   }
}
