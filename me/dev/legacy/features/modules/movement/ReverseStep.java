package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.modules.Module;

public class ReverseStep extends Module {
   private static ReverseStep INSTANCE = new ReverseStep();

   public ReverseStep() {
      super("ReverseStep", "ReverseStep.", Module.Category.MOVEMENT, true, false, false);
      this.setInstance();
   }

   public static ReverseStep getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new ReverseStep();
      }

      return INSTANCE;
   }

   private void setInstance() {
      INSTANCE = this;
   }

   public void onUpdate() {
      if (mc.field_71439_g != null && mc.field_71441_e != null && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab()) {
         if (mc.field_71439_g.field_70122_E) {
            --mc.field_71439_g.field_70181_x;
         }

      }
   }
}
