package me.dev.legacy.features.modules.misc;

import java.util.Random;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class Dupe extends Module {
   private final Random random = new Random();

   public Dupe() {
      super("Dupe", "Ez dupe.", Module.Category.MISC, true, false, false);
   }

   public void onEnable() {
      EntityPlayerSP player = mc.field_71439_g;
      WorldClient world = mc.field_71441_e;
      if (player != null && mc.field_71441_e != null) {
         ItemStack itemStack = player.func_184614_ca();
         if (itemStack.func_190926_b()) {
            Command.sendMessage("You need to hold an item in hand to dupe!");
            this.disable();
         } else {
            int count = this.random.nextInt(31) + 1;

            int total;
            for(total = 0; total <= count; ++total) {
               EntityItem entityItem = player.func_146097_a(itemStack.func_77946_l(), false, true);
               if (entityItem != null) {
                  world.func_73027_a(entityItem.field_145783_c, entityItem);
               }
            }

            total = count * itemStack.func_190916_E();
            player.func_71165_d("I just used the Legacy Client Dupe and got " + total + " " + itemStack.func_82833_r() + " thanks to Legacy dev's!");
            this.disable();
         }
      }
   }
}
