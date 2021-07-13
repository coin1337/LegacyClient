package me.dev.legacy.util;

import me.dev.legacy.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiUtil {
   public static void drawString(String paramString, float paramFloat1, float paramFloat2, int paramInt) {
      if (Client.moduleManager.getModuleByName("CustomFont").isEnabled()) {
         Client.fontRenderer.drawStringWithShadow(paramString, (double)paramFloat1, (double)paramFloat2, paramInt);
      } else {
         Minecraft.func_71410_x().field_71466_p.func_175063_a(paramString, paramFloat1, paramFloat2, paramInt);
      }

   }

   public static int getStringWidth(String paramString) {
      return Minecraft.func_71410_x().field_71466_p.func_78256_a(paramString);
   }

   public static void drawHorizontalLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
      if (paramInt2 < paramInt1) {
         int i = paramInt1;
         paramInt1 = paramInt2;
         paramInt2 = i;
      }

      drawRect(paramInt1, paramInt3, paramInt2 + 1, paramInt3 + 1, paramInt4);
   }

   public static void drawString(String paramString, int paramInt1, int paramInt2, int paramInt3) {
      if (Client.moduleManager.getModuleByName("CustomFont").isEnabled()) {
         Client.fontRenderer.drawStringWithShadow(paramString, (double)paramInt1, (double)paramInt2, paramInt3);
      } else {
         Minecraft.func_71410_x().field_71466_p.func_175063_a(paramString, (float)paramInt1, (float)paramInt2, paramInt3);
      }

   }

   public static String getCFont() {
      return Client.fontRenderer.getFont().getFamily();
   }

   public static void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
      int i;
      if (paramInt1 < paramInt3) {
         i = paramInt1;
         paramInt1 = paramInt3;
         paramInt3 = i;
      }

      if (paramInt2 < paramInt4) {
         i = paramInt2;
         paramInt2 = paramInt4;
         paramInt4 = i;
      }

      float f1 = (float)(paramInt5 >> 24 & 255) / 255.0F;
      float f2 = (float)(paramInt5 >> 16 & 255) / 255.0F;
      float f3 = (float)(paramInt5 >> 8 & 255) / 255.0F;
      float f4 = (float)(paramInt5 & 255) / 255.0F;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferBuilder = tessellator.func_178180_c();
      GlStateManager.func_179147_l();
      GlStateManager.func_179090_x();
      GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
      GlStateManager.func_179131_c(f2, f3, f4, f1);
      bufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      bufferBuilder.func_181662_b((double)paramInt1, (double)paramInt4, 0.0D).func_181675_d();
      bufferBuilder.func_181662_b((double)paramInt3, (double)paramInt4, 0.0D).func_181675_d();
      bufferBuilder.func_181662_b((double)paramInt3, (double)paramInt2, 0.0D).func_181675_d();
      bufferBuilder.func_181662_b((double)paramInt1, (double)paramInt2, 0.0D).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179098_w();
      GlStateManager.func_179084_k();
   }

   public static int getHeight() {
      return Client.moduleManager.getModuleByName("CustomFont").isEnabled() ? Client.fontRenderer.getHeight() : Client.fontRenderer.getHeight();
   }

   public static void drawVerticalLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
      if (paramInt3 < paramInt2) {
         int i = paramInt2;
         paramInt2 = paramInt3;
         paramInt3 = i;
      }

      drawRect(paramInt1, paramInt2 + 1, paramInt1 + 1, paramInt3, paramInt4);
   }

   public static void drawCenteredString(String paramString, int paramInt1, int paramInt2, int paramInt3) {
      if (Client.moduleManager.getModuleByName("CustomFont").isEnabled()) {
         Client.fontRenderer.drawStringWithShadow(paramString, (double)(paramInt1 - Client.fontRenderer.getStringWidth(paramString) / 2), (double)paramInt2, paramInt3);
      } else {
         Minecraft.func_71410_x().field_71466_p.func_175063_a(paramString, (float)(paramInt1 - Client.fontRenderer.getStringWidth(paramString) / 2), (float)paramInt2, paramInt3);
      }

   }
}
