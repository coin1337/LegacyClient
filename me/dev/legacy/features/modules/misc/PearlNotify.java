package me.dev.legacy.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.dev.legacy.Client;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;

public class PearlNotify extends Module {
   private final HashMap<EntityPlayer, UUID> list = new HashMap();
   private Entity enderPearl;
   private boolean flag;

   public PearlNotify() {
      super("PearlResolver", "Notify pearl throws.", Module.Category.MISC, true, false, false);
   }

   public void onEnable() {
      this.flag = true;
   }

   public void onUpdate() {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         this.enderPearl = null;
         Iterator var1 = mc.field_71441_e.field_72996_f.iterator();

         while(var1.hasNext()) {
            Entity e = (Entity)var1.next();
            if (e instanceof EntityEnderPearl) {
               this.enderPearl = e;
               break;
            }
         }

         if (this.enderPearl == null) {
            this.flag = true;
         } else {
            EntityPlayer closestPlayer = null;
            Iterator var5 = mc.field_71441_e.field_73010_i.iterator();

            while(var5.hasNext()) {
               EntityPlayer entity = (EntityPlayer)var5.next();
               if (closestPlayer == null) {
                  closestPlayer = entity;
               } else if (!(closestPlayer.func_70032_d(this.enderPearl) <= entity.func_70032_d(this.enderPearl))) {
                  closestPlayer = entity;
               }
            }

            if (closestPlayer == mc.field_71439_g) {
               this.flag = false;
            }

            if (closestPlayer != null && this.flag) {
               String faceing = this.enderPearl.func_174811_aO().toString();
               if (faceing.equals("west")) {
                  faceing = "east";
               } else if (faceing.equals("east")) {
                  faceing = "west";
               }

               Command.sendSilentMessage(Client.friendManager.isFriend(closestPlayer.func_70005_c_()) ? ChatFormatting.RED + closestPlayer.func_70005_c_() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!" : ChatFormatting.RED + closestPlayer.func_70005_c_() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!");
               this.flag = false;
            }

         }
      }
   }
}
