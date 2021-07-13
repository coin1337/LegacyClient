package me.dev.legacy.mixin.mixins;

import me.dev.legacy.Client;
import me.dev.legacy.features.modules.client.FontMod;
import me.dev.legacy.features.modules.client.MediaModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FontRenderer.class})
public abstract class MixinFontRenderer {
   @Shadow
   protected abstract int func_180455_b(String var1, float var2, float var3, int var4, boolean var5);

   @Shadow
   protected abstract void func_78255_a(String var1, boolean var2);

   @Redirect(
      method = {"renderString(Ljava/lang/String;FFIZ)I"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"
)
   )
   public void renderStringAtPosHook(FontRenderer renderer, String text, boolean shadow) {
      if (MediaModule.getInstance().isOn()) {
         this.func_78255_a(text.replace(Minecraft.func_71410_x().func_110432_I().func_111285_a(), MediaModule.getInstance().NameString.getValueAsString()), shadow);
      } else {
         this.func_78255_a(text, shadow);
      }

   }

   @Inject(
      method = {"drawString(Ljava/lang/String;FFIZ)I"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderStringHook(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> info) {
      if (FontMod.getInstance().isOn() && (Boolean)((FontMod)Client.moduleManager.getModuleT(FontMod.class)).customAll.getValue() && Client.textManager != null) {
         float result = Client.textManager.drawString(text, x, y, color, dropShadow);
         info.setReturnValue((int)result);
      }

   }
}
