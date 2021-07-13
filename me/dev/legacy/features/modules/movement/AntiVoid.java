package me.dev.legacy.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class AntiVoid extends Module {
   public Setting<AntiVoid.Mode> mode;
   public Setting<Boolean> display;

   public AntiVoid() {
      super("AntiVoid", "Glitches you up from void.", Module.Category.MOVEMENT, false, false, false);
      this.mode = this.register(new Setting("Mode", AntiVoid.Mode.BOUNCE));
      this.display = this.register(new Setting("Display", true));
   }

   public void onUpdate() {
      double yLevel = mc.field_71439_g.field_70163_u;
      if (yLevel <= 0.5D) {
         Command.sendMessage(ChatFormatting.RED + "Player " + ChatFormatting.GREEN + mc.field_71439_g.func_70005_c_() + ChatFormatting.RED + " is in the void!");
         if (((AntiVoid.Mode)this.mode.getValue()).equals(AntiVoid.Mode.BOUNCE)) {
            mc.field_71439_g.field_70701_bs = 10.0F;
            mc.field_71439_g.func_70664_aZ();
         }

         if (((AntiVoid.Mode)this.mode.getValue()).equals(AntiVoid.Mode.LAUNCH)) {
            mc.field_71439_g.field_70701_bs = 100.0F;
            mc.field_71439_g.func_70664_aZ();
         }
      } else {
         mc.field_71439_g.field_70701_bs = 0.0F;
      }

   }

   public void onDisable() {
      mc.field_71439_g.field_70701_bs = 0.0F;
   }

   public String getDisplayInfo() {
      if ((Boolean)this.display.getValue()) {
         if (((AntiVoid.Mode)this.mode.getValue()).equals(AntiVoid.Mode.BOUNCE)) {
            return "Bounce";
         }

         if (((AntiVoid.Mode)this.mode.getValue()).equals(AntiVoid.Mode.LAUNCH)) {
            return "Launch";
         }
      }

      return null;
   }

   public static enum Mode {
      BOUNCE,
      LAUNCH;
   }
}
