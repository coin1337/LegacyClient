package me.dev.legacy.features.modules.misc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.SoundEvents;

public class GhastNotifier extends Module {
   private Set<Entity> ghasts = new HashSet();
   public Setting<Boolean> Chat = this.register(new Setting("Chat", true));
   public Setting<Boolean> Sound = this.register(new Setting("Sound", true));

   public GhastNotifier() {
      super("GhastNotifier", "Helps you find ghasts", Module.Category.MISC, true, false, false);
   }

   public void onEnable() {
      this.ghasts.clear();
   }

   public void onUpdate() {
      Iterator var1 = mc.field_71441_e.func_72910_y().iterator();

      while(var1.hasNext()) {
         Entity entity = (Entity)var1.next();
         if (entity instanceof EntityGhast && !this.ghasts.contains(entity)) {
            if ((Boolean)this.Chat.getValue()) {
               Command.sendMessage("Ghast Detected at: " + entity.func_180425_c().func_177958_n() + "x, " + entity.func_180425_c().func_177956_o() + "y, " + entity.func_180425_c().func_177952_p() + "z.");
            }

            this.ghasts.add(entity);
            if ((Boolean)this.Sound.getValue()) {
               mc.field_71439_g.func_184185_a(SoundEvents.field_187680_c, 1.0F, 1.0F);
            }
         }
      }

   }
}
