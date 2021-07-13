package me.dev.legacy.features.modules.player;

import me.dev.legacy.event.events.Packet;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Swing extends Module {
   private Setting<Swing.Hand> hand;

   public Swing() {
      super("Swing", "Changes the hand you swing with", Module.Category.RENDER, false, false, false);
      this.hand = this.register(new Setting("Hand", Swing.Hand.OFFHAND));
   }

   public void onUpdate() {
      if (mc.field_71441_e != null) {
         if (((Swing.Hand)this.hand.getValue()).equals(Swing.Hand.OFFHAND)) {
            mc.field_71439_g.field_184622_au = EnumHand.OFF_HAND;
         }

         if (((Swing.Hand)this.hand.getValue()).equals(Swing.Hand.MAINHAND)) {
            mc.field_71439_g.field_184622_au = EnumHand.MAIN_HAND;
         }

      }
   }

   @SubscribeEvent
   public void onPacket(Packet event) {
      if (!nullCheck() && event.getType() != Packet.Type.INCOMING) {
         if (event.getPacket() instanceof CPacketAnimation) {
            event.setCanceled(true);
         }

      }
   }

   public static enum Hand {
      OFFHAND,
      MAINHAND,
      PACKETSWING;
   }
}
