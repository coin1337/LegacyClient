package me.dev.legacy.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import me.dev.legacy.features.Feature;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopManager extends Feature {
   private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap();
   private Set<EntityPlayer> toAnnounce = new HashSet();

   public void onUpdate() {
      Iterator var1 = this.toAnnounce.iterator();

      while(var1.hasNext()) {
         EntityPlayer player = (EntityPlayer)var1.next();
         if (player != null) {
            int playerNumber = 0;
            char[] var4 = player.func_70005_c_().toCharArray();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               char character = var4[var6];
               playerNumber += character;
               playerNumber *= 10;
            }

            this.toAnnounce.remove(player);
            break;
         }
      }

   }

   public void onLogout() {
   }

   public void init() {
   }

   public void onTotemPop(EntityPlayer player) {
      this.popTotem(player);
      if (!player.equals(mc.field_71439_g)) {
         this.toAnnounce.add(player);
      }

   }

   public void onDeath(EntityPlayer player) {
      int playerNumber = 0;
      char[] var3 = player.func_70005_c_().toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char character = var3[var5];
         playerNumber += character;
         playerNumber *= 10;
      }

      this.toAnnounce.remove(player);
   }

   public void onLogout(EntityPlayer player, boolean clearOnLogout) {
      if (clearOnLogout) {
         this.resetPops(player);
      }

   }

   public void onOwnLogout(boolean clearOnLogout) {
      if (clearOnLogout) {
         this.clearList();
      }

   }

   public void clearList() {
      this.poplist = new ConcurrentHashMap();
   }

   public void resetPops(EntityPlayer player) {
      this.setTotemPops(player, 0);
   }

   public void popTotem(EntityPlayer player) {
      this.poplist.merge(player, 1, Integer::sum);
   }

   public void setTotemPops(EntityPlayer player, int amount) {
      this.poplist.put(player, amount);
   }

   public int getTotemPops(EntityPlayer player) {
      Integer pops = (Integer)this.poplist.get(player);
      return pops == null ? 0 : pops;
   }

   public String getTotemPopString(EntityPlayer player) {
      return "§f" + (this.getTotemPops(player) <= 0 ? "" : "-" + this.getTotemPops(player) + " ");
   }
}
