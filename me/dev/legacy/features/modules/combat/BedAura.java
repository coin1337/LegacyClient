package me.dev.legacy.features.modules.combat;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.manager.RotationManager;
import net.minecraft.util.EnumHand;

public class BedAura extends Module {
   public final Setting<Boolean> settingAntiNaked = this.register(new Setting("Anti-Naked", true));
   public final Setting<Boolean> settingAutoSwitch = this.register(new Setting("Auto-Switch", true));
   public final Setting<Boolean> settingAutoOffhand = this.register(new Setting("Auto-Offhand", false));
   public final Setting<Float> settingRange = this.register(new Setting("Range", 6.0F, 1.0F, 13.0F));
   public final Setting<Enum> settingTargetMode;
   public final Setting<Boolean> settingSuicide;
   public final Setting<Integer> settingSelfDamage;
   public final Setting<Float> settingMinimumDamageTarget;
   public final Setting<Boolean> settingPlace;
   public final Setting<Boolean> settingPlaceSync;
   public final Setting<Boolean> settingPlaceWorldTick;
   public final Setting<Boolean> settingAirPlace;
   public final Setting<Float> settingPlaceRange;
   public final Setting<Integer> settingPlaceDelay;
   public final Setting<Enum> settingPlaceRotate;
   public final Setting<Boolean> settingClick;
   public final Setting<Boolean> settingClickPredict;
   public final Setting<Boolean> settingClickSync;
   public final Setting<Boolean> settingClickOnlyWhenEquippedBed;
   public final Setting<Boolean> settingClickWorldTick;
   public final Setting<Enum> settingClickHand;
   public final Setting<Float> settingClickRange;
   public final Setting<Integer> settingClickDelay;
   public final Setting<Enum> settingClickRotate;
   public final Setting<Boolean> settingRenderSwing;
   public final Setting<Boolean> settingRGB;
   public final Setting<Integer> settingRed;
   public final Setting<Integer> settingGreen;
   public final Setting<Integer> settingBlue;
   public final Setting<Integer> settingAlpha;
   public final Setting<Float> settingOutlineLineSize;
   public final Setting<Integer> settingOutlineAlpha;

   public BedAura() {
      super("Aura", "Kills aura.", Module.Category.COMBAT, true, false, false);
      this.settingTargetMode = this.register(new Setting("Target Mode", BedAura.TargetMode.UNSAFE));
      this.settingSuicide = this.register(new Setting("Suicide", false));
      this.settingSelfDamage = this.register(new Setting("Self Damage", 8, 1, 36));
      this.settingMinimumDamageTarget = this.register(new Setting("Min. Target Dmg.", 2.0F, 1.0F, 36.0F));
      this.settingPlace = this.register(new Setting("Place", true));
      this.settingPlaceSync = this.register(new Setting("Place Sync", true));
      this.settingPlaceWorldTick = this.register(new Setting("Place World Tick", false));
      this.settingAirPlace = this.register(new Setting("Air Place", false));
      this.settingPlaceRange = this.register(new Setting("Place Range", 4.0F, 1.0F, 6.0F));
      this.settingPlaceDelay = this.register(new Setting("Place Delay", 50, 0, 100));
      this.settingPlaceRotate = this.register(new Setting("Place Rotate", RotationManager.Rotation.SEND));
      this.settingClick = this.register(new Setting("Click", true));
      this.settingClickPredict = this.register(new Setting("Click Predict", false));
      this.settingClickSync = this.register(new Setting("Click Sync", true));
      this.settingClickOnlyWhenEquippedBed = this.register(new Setting("Click With Bed", false));
      this.settingClickWorldTick = this.register(new Setting("Click World Tick", false));
      this.settingClickHand = this.register(new Setting("Click Hand", BedAura.ClickHand.AUTO));
      this.settingClickRange = this.register(new Setting("Click Range", 4.0F, 1.0F, 6.0F));
      this.settingClickDelay = this.register(new Setting("Click Delay", 50, 0, 100));
      this.settingClickRotate = this.register(new Setting("Click Rotate", RotationManager.Rotation.SEND));
      this.settingRenderSwing = this.register(new Setting("Render Swing", true));
      this.settingRGB = this.register(new Setting("RGB", false));
      this.settingRed = this.register(new Setting("Red", 255, 0, 255));
      this.settingGreen = this.register(new Setting("Green", 0, 0, 255));
      this.settingBlue = this.register(new Setting("Blue", 255, 0, 255));
      this.settingAlpha = this.register(new Setting("Alpha", 255, 0, 255));
      this.settingOutlineLineSize = this.register(new Setting("Outline Line Size", 1.0F, 1.0F, 3.0F));
      this.settingOutlineAlpha = this.register(new Setting("Outline Alpha", 255, 0, 255));
   }

   public static enum ClickHand {
      AUTO((EnumHand)null),
      OFF(EnumHand.OFF_HAND),
      MAIN(EnumHand.MAIN_HAND);

      EnumHand hand;

      private ClickHand(EnumHand hand) {
         this.hand = hand;
      }

      public EnumHand getHand() {
         return this.hand;
      }
   }

   public static enum TargetMode {
      UNSAFE,
      CLOSET;
   }
}
