package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastDrop extends Module {
   private final Setting<Float> speed = this.register(new Setting("Speed", 0.0F, 0.0F, 20.0F));

   public FastDrop() {
      super("FastDrop", "Fast drop to ffa.", Module.Category.MOVEMENT, false, false, false);
   }

   @SubscribeEvent
   public void onUpdate() {
      if (mc.field_71439_g != null && mc.field_71441_e != null && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab()) {
         if (mc.field_71439_g.field_70122_E) {
            EntityPlayerSP var10000 = mc.field_71439_g;
            var10000.field_70181_x -= (double)((Float)this.speed.getValue() / 10.0F);
         }

      }
   }
}
