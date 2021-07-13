package me.dev.legacy.util;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.opengl.Display;

public class Title {
   int ticks = 0;
   int bruh = 0;
   int breakTimer = 0;
   String bruh1 = "Legacy | v1.0.0 ";
   boolean qwerty = false;

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      ++this.ticks;
      if (this.ticks % 17 == 0) {
         Display.setTitle(this.bruh1.substring(0, this.bruh1.length() - this.bruh));
         if (this.bruh == this.bruh1.length() && this.breakTimer != 0 || this.bruh == 0 && this.breakTimer != 0) {
            ++this.breakTimer;
            return;
         }

         this.breakTimer = 0;
         if (this.bruh == this.bruh1.length()) {
            this.qwerty = true;
         }

         if (this.qwerty) {
            --this.bruh;
         } else {
            ++this.bruh;
         }

         if (this.bruh == 0) {
            this.qwerty = false;
         }
      }

   }
}
