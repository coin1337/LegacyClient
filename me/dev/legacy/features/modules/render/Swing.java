package me.dev.legacy.features.modules.render;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

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

         if (((Swing.Hand)this.hand.getValue()).equals(Swing.Hand.PACKETSWING) && mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (double)mc.field_71460_t.field_78516_c.field_187470_g >= 0.9D) {
            mc.field_71460_t.field_78516_c.field_187469_f = 1.0F;
            mc.field_71460_t.field_78516_c.field_187467_d = mc.field_71439_g.func_184614_ca();
         }

      }
   }

   public static enum Hand {
      OFFHAND,
      MAINHAND,
      PACKETSWING;
   }
}
