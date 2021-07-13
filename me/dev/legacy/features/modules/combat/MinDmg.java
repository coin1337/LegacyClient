package me.dev.legacy.features.modules.combat;

import com.google.common.eventbus.Subscribe;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class MinDmg extends Module {
   private static MinDmg INSTANCE = new MinDmg();
   private final Setting<Float> EnableDamage = this.register(new Setting("Enable MinDmg", 4.0F, 1.0F, 36.0F));
   private final Setting<Float> DisableDamage = this.register(new Setting("Disable MinDmg", 4.0F, 1.0F, 36.0F));

   public MinDmg() {
      super("MinDmg", "Set minimal damage for auto crystal.", Module.Category.COMBAT, true, false, false);
      INSTANCE = this;
   }

   public static MinDmg getInstance() {
      return INSTANCE;
   }

   @Subscribe
   public void onEnable() {
      AutoCrystal.getInstance().minDamage.setValue(this.EnableDamage.getValue());
   }

   @Subscribe
   public void onDisable() {
      AutoCrystal.getInstance().minDamage.setValue(this.DisableDamage.getValue());
   }
}
