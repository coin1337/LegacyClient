package me.dev.legacy.features.modules.movement;

import me.dev.legacy.event.events.KeyEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown extends Module {
   public Setting<Boolean> guiMove = this.register(new Setting("GuiMove", true));
   public Setting<Boolean> noSlow = this.register(new Setting("NoSlow", true));
   public Setting<Boolean> soulSand = this.register(new Setting("SoulSand", true));
   private static NoSlowDown INSTANCE = new NoSlowDown();
   private static KeyBinding[] keys;

   public NoSlowDown() {
      super("NoSlowDown", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
      this.setInstance();
   }

   private void setInstance() {
      INSTANCE = this;
   }

   public static NoSlowDown getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new NoSlowDown();
      }

      return INSTANCE;
   }

   public void onUpdate() {
      if ((Boolean)this.guiMove.getValue()) {
         KeyBinding[] var1;
         int var2;
         int var3;
         KeyBinding bind;
         if (!(mc.field_71462_r instanceof GuiOptions) && !(mc.field_71462_r instanceof GuiVideoSettings) && !(mc.field_71462_r instanceof GuiScreenOptionsSounds) && !(mc.field_71462_r instanceof GuiContainer) && !(mc.field_71462_r instanceof GuiIngameMenu)) {
            if (mc.field_71462_r == null) {
               var1 = keys;
               var2 = var1.length;

               for(var3 = 0; var3 < var2; ++var3) {
                  bind = var1[var3];
                  if (!Keyboard.isKeyDown(bind.func_151463_i())) {
                     KeyBinding.func_74510_a(bind.func_151463_i(), false);
                  }
               }
            }
         } else {
            var1 = keys;
            var2 = var1.length;

            for(var3 = 0; var3 < var2; ++var3) {
               bind = var1[var3];
               KeyBinding.func_74510_a(bind.func_151463_i(), Keyboard.isKeyDown(bind.func_151463_i()));
            }
         }
      }

   }

   @SubscribeEvent
   public void onInput(InputUpdateEvent event) {
      if ((Boolean)this.noSlow.getValue() && mc.field_71439_g.func_184587_cr() && !mc.field_71439_g.func_184218_aH()) {
         MovementInput movementInput = event.getMovementInput();
         movementInput.field_78902_a *= 5.0F;
         MovementInput movementInput2 = event.getMovementInput();
         movementInput2.field_192832_b *= 5.0F;
      }

   }

   @SubscribeEvent
   public void onKeyEvent(KeyEvent event) {
      if ((Boolean)this.guiMove.getValue() && event.getStage() == 0 && !(mc.field_71462_r instanceof GuiChat)) {
         event.info = event.pressed;
      }

   }

   static {
      keys = new KeyBinding[]{mc.field_71474_y.field_74351_w, mc.field_71474_y.field_74368_y, mc.field_71474_y.field_74370_x, mc.field_71474_y.field_74366_z, mc.field_71474_y.field_74314_A, mc.field_71474_y.field_151444_V};
   }
}
