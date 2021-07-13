package me.dev.legacy.util;

import me.dev.legacy.Client;
import net.minecraft.client.Minecraft;

public class FontUtils {
   private static final Minecraft mc = Minecraft.func_71410_x();

   public static float drawStringWithShadow(boolean customFont, String text, int x, int y, int color) {
      return customFont ? Client.fontRenderer.drawStringWithShadow(text, (double)x, (double)y, color) : (float)mc.field_71466_p.func_175063_a(text, (float)x, (float)y, color);
   }

   public static int getStringWidth(boolean customFont, String str) {
      return customFont ? Client.fontRenderer.getStringWidth(str) : mc.field_71466_p.func_78256_a(str);
   }

   public static int getFontHeight(boolean customFont) {
      return customFont ? Client.fontRenderer.getHeight() : mc.field_71466_p.field_78288_b;
   }

   public static float drawKeyStringWithShadow(boolean customFont, String text, int x, int y, int color) {
      return customFont ? Client.fontRenderer.drawStringWithShadow(text, (double)x, (double)y, color) : (float)mc.field_71466_p.func_175063_a(text, (float)x, (float)y, color);
   }
}
