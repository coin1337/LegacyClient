package me.dev.legacy.features.modules.client;

import java.util.Iterator;
import me.dev.legacy.Client;
import me.dev.legacy.event.events.Render2DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.MathUtil;
import me.dev.legacy.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class HudComponents extends Module {
   private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
   private static final double HALF_PI = 1.5707963267948966D;
   public Setting<Boolean> inventory = this.register(new Setting("Inventory", false));
   public Setting<Integer> invX = this.register(new Setting("InvX", 564, 0, 1000, (v) -> {
      return (Boolean)this.inventory.getValue();
   }));
   public Setting<Integer> invY = this.register(new Setting("InvY", 467, 0, 1000, (v) -> {
      return (Boolean)this.inventory.getValue();
   }));
   public Setting<Integer> fineinvX = this.register(new Setting("InvFineX", 0, (v) -> {
      return (Boolean)this.inventory.getValue();
   }));
   public Setting<Integer> fineinvY = this.register(new Setting("InvFineY", 0, (v) -> {
      return (Boolean)this.inventory.getValue();
   }));
   public Setting<Boolean> renderXCarry = this.register(new Setting("RenderXCarry", false, (v) -> {
      return (Boolean)this.inventory.getValue();
   }));
   public Setting<Integer> invH = this.register(new Setting("InvH", 3, (v) -> {
      return (Boolean)this.inventory.getValue();
   }));
   public Setting<Boolean> holeHud = this.register(new Setting("HoleHUD", false));
   public Setting<Integer> holeX = this.register(new Setting("HoleX", 279, 0, 1000, (v) -> {
      return (Boolean)this.holeHud.getValue();
   }));
   public Setting<Integer> holeY = this.register(new Setting("HoleY", 485, 0, 1000, (v) -> {
      return (Boolean)this.holeHud.getValue();
   }));
   public Setting<HudComponents.Compass> compass;
   public Setting<Integer> compassX;
   public Setting<Integer> compassY;
   public Setting<Integer> scale;
   public Setting<Boolean> playerViewer;
   public Setting<Integer> playerViewerX;
   public Setting<Integer> playerViewerY;
   public Setting<Float> playerScale;

   public HudComponents() {
      super("Components", "Hud Components", Module.Category.CLIENT, false, false, true);
      this.compass = this.register(new Setting("Compass", HudComponents.Compass.NONE));
      this.compassX = this.register(new Setting("CompX", 472, 0, 1000, (v) -> {
         return this.compass.getValue() != HudComponents.Compass.NONE;
      }));
      this.compassY = this.register(new Setting("CompY", 424, 0, 1000, (v) -> {
         return this.compass.getValue() != HudComponents.Compass.NONE;
      }));
      this.scale = this.register(new Setting("Scale", 3, 0, 10, (v) -> {
         return this.compass.getValue() != HudComponents.Compass.NONE;
      }));
      this.playerViewer = this.register(new Setting("PlayerViewer", false));
      this.playerViewerX = this.register(new Setting("PlayerX", 752, 0, 1000, (v) -> {
         return (Boolean)this.playerViewer.getValue();
      }));
      this.playerViewerY = this.register(new Setting("PlayerY", 497, 0, 1000, (v) -> {
         return (Boolean)this.playerViewer.getValue();
      }));
      this.playerScale = this.register(new Setting("PlayerScale", 1.0F, 0.1F, 2.0F, (v) -> {
         return (Boolean)this.playerViewer.getValue();
      }));
   }

   public void onRender2D(Render2DEvent event) {
      if (!fullNullCheck()) {
         if ((Boolean)this.playerViewer.getValue()) {
            this.drawPlayer();
         }

         if (this.compass.getValue() != HudComponents.Compass.NONE) {
            this.drawCompass();
         }

         if ((Boolean)this.holeHud.getValue()) {
            this.drawOverlay(event.partialTicks);
         }

         if ((Boolean)this.inventory.getValue()) {
            this.renderInventory();
         }

      }
   }

   public static EntityPlayer getClosestEnemy() {
      EntityPlayer closestPlayer = null;
      Iterator var1 = mc.field_71441_e.field_73010_i.iterator();

      while(var1.hasNext()) {
         EntityPlayer player = (EntityPlayer)var1.next();
         if (player != mc.field_71439_g && !Client.friendManager.isFriend(player)) {
            if (closestPlayer == null) {
               closestPlayer = player;
            } else if (mc.field_71439_g.func_70068_e(player) < mc.field_71439_g.func_70068_e(closestPlayer)) {
               closestPlayer = player;
            }
         }
      }

      return closestPlayer;
   }

   public void drawCompass() {
      ScaledResolution sr = new ScaledResolution(mc);
      if (this.compass.getValue() == HudComponents.Compass.LINE) {
         float playerYaw = mc.field_71439_g.field_70177_z;
         float rotationYaw = MathUtil.wrap(playerYaw);
         RenderUtil.drawRect((float)(Integer)this.compassX.getValue(), (float)(Integer)this.compassY.getValue(), (float)((Integer)this.compassX.getValue() + 100), (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight()), 1963986960);
         RenderUtil.glScissor((float)(Integer)this.compassX.getValue(), (float)(Integer)this.compassY.getValue(), (float)((Integer)this.compassX.getValue() + 100), (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight()), sr);
         GL11.glEnable(3089);
         float zeroZeroYaw = MathUtil.wrap((float)(Math.atan2(0.0D - mc.field_71439_g.field_70161_v, 0.0D - mc.field_71439_g.field_70165_t) * 180.0D / 3.141592653589793D) - 90.0F);
         RenderUtil.drawLine((float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + zeroZeroYaw, (float)((Integer)this.compassY.getValue() + 2), (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + zeroZeroYaw, (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0F, -61424);
         RenderUtil.drawLine((float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + 45.0F, (float)((Integer)this.compassY.getValue() + 2), (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + 45.0F, (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
         RenderUtil.drawLine((float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F - 45.0F, (float)((Integer)this.compassY.getValue() + 2), (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F - 45.0F, (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
         RenderUtil.drawLine((float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + 135.0F, (float)((Integer)this.compassY.getValue() + 2), (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + 135.0F, (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
         RenderUtil.drawLine((float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F - 135.0F, (float)((Integer)this.compassY.getValue() + 2), (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F - 135.0F, (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight() - 2), 2.0F, -1);
         this.renderer.drawStringWithShadow("n", (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + 180.0F - (float)this.renderer.getStringWidth("n") / 2.0F, (float)(Integer)this.compassY.getValue(), -1);
         this.renderer.drawStringWithShadow("n", (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F - 180.0F - (float)this.renderer.getStringWidth("n") / 2.0F, (float)(Integer)this.compassY.getValue(), -1);
         this.renderer.drawStringWithShadow("e", (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F - 90.0F - (float)this.renderer.getStringWidth("e") / 2.0F, (float)(Integer)this.compassY.getValue(), -1);
         this.renderer.drawStringWithShadow("s", (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F - (float)this.renderer.getStringWidth("s") / 2.0F, (float)(Integer)this.compassY.getValue(), -1);
         this.renderer.drawStringWithShadow("w", (float)(Integer)this.compassX.getValue() - rotationYaw + 50.0F + 90.0F - (float)this.renderer.getStringWidth("w") / 2.0F, (float)(Integer)this.compassY.getValue(), -1);
         RenderUtil.drawLine((float)((Integer)this.compassX.getValue() + 50), (float)((Integer)this.compassY.getValue() + 1), (float)((Integer)this.compassX.getValue() + 50), (float)((Integer)this.compassY.getValue() + this.renderer.getFontHeight() - 1), 2.0F, -7303024);
         GL11.glDisable(3089);
      } else {
         double centerX = (double)(Integer)this.compassX.getValue();
         double centerY = (double)(Integer)this.compassY.getValue();
         HudComponents.Direction[] var6 = HudComponents.Direction.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            HudComponents.Direction dir = var6[var8];
            double rad = getPosOnCompass(dir);
            this.renderer.drawStringWithShadow(dir.name(), (float)(centerX + this.getX(rad)), (float)(centerY + this.getY(rad)), dir == HudComponents.Direction.N ? -65536 : -1);
         }
      }

   }

   public void drawPlayer(EntityPlayer player, int x, int y) {
      EntityPlayer ent = player;
      GlStateManager.func_179094_E();
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      RenderHelper.func_74519_b();
      GlStateManager.func_179141_d();
      GlStateManager.func_179103_j(7424);
      GlStateManager.func_179141_d();
      GlStateManager.func_179126_j();
      GlStateManager.func_179114_b(0.0F, 0.0F, 5.0F, 0.0F);
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)((Integer)this.playerViewerX.getValue() + 25), (float)((Integer)this.playerViewerY.getValue() + 25), 50.0F);
      GlStateManager.func_179152_a(-50.0F * (Float)this.playerScale.getValue(), 50.0F * (Float)this.playerScale.getValue(), 50.0F * (Float)this.playerScale.getValue());
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.func_74519_b();
      GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-((float)Math.atan((double)((float)(Integer)this.playerViewerY.getValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      RenderManager rendermanager = mc.func_175598_ae();
      rendermanager.func_178631_a(180.0F);
      rendermanager.func_178633_a(false);

      try {
         rendermanager.func_188391_a(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
      } catch (Exception var7) {
      }

      rendermanager.func_178633_a(true);
      GlStateManager.func_179121_F();
      RenderHelper.func_74518_a();
      GlStateManager.func_179101_C();
      GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
      GlStateManager.func_179090_x();
      GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
      GlStateManager.func_179143_c(515);
      GlStateManager.func_179117_G();
      GlStateManager.func_179097_i();
      GlStateManager.func_179121_F();
   }

   public void drawPlayer() {
      EntityPlayer ent = mc.field_71439_g;
      GlStateManager.func_179094_E();
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      RenderHelper.func_74519_b();
      GlStateManager.func_179141_d();
      GlStateManager.func_179103_j(7424);
      GlStateManager.func_179141_d();
      GlStateManager.func_179126_j();
      GlStateManager.func_179114_b(0.0F, 0.0F, 5.0F, 0.0F);
      GlStateManager.func_179142_g();
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)((Integer)this.playerViewerX.getValue() + 25), (float)((Integer)this.playerViewerY.getValue() + 25), 50.0F);
      GlStateManager.func_179152_a(-50.0F * (Float)this.playerScale.getValue(), 50.0F * (Float)this.playerScale.getValue(), 50.0F * (Float)this.playerScale.getValue());
      GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.func_74519_b();
      GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-((float)Math.atan((double)((float)(Integer)this.playerViewerY.getValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
      RenderManager rendermanager = mc.func_175598_ae();
      rendermanager.func_178631_a(180.0F);
      rendermanager.func_178633_a(false);

      try {
         rendermanager.func_188391_a(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
      } catch (Exception var4) {
      }

      rendermanager.func_178633_a(true);
      GlStateManager.func_179121_F();
      RenderHelper.func_74518_a();
      GlStateManager.func_179101_C();
      GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
      GlStateManager.func_179090_x();
      GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
      GlStateManager.func_179143_c(515);
      GlStateManager.func_179117_G();
      GlStateManager.func_179097_i();
      GlStateManager.func_179121_F();
   }

   private double getX(double rad) {
      return Math.sin(rad) * (double)((Integer)this.scale.getValue() * 10);
   }

   private double getY(double rad) {
      double epicPitch = (double)MathHelper.func_76131_a(mc.field_71439_g.field_70125_A + 30.0F, -90.0F, 90.0F);
      double pitchRadians = Math.toRadians(epicPitch);
      return Math.cos(rad) * Math.sin(pitchRadians) * (double)((Integer)this.scale.getValue() * 10);
   }

   private static double getPosOnCompass(HudComponents.Direction dir) {
      double yaw = Math.toRadians((double)MathHelper.func_76142_g(mc.field_71439_g.field_70177_z));
      int index = dir.ordinal();
      return yaw + (double)index * 1.5707963267948966D;
   }

   public void drawOverlay(float partialTicks) {
      float yaw = 0.0F;
      int dir = MathHelper.func_76128_c((double)(mc.field_71439_g.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
      switch(dir) {
      case 1:
         yaw = 90.0F;
         break;
      case 2:
         yaw = -180.0F;
         break;
      case 3:
         yaw = -90.0F;
      }

      BlockPos northPos = this.traceToBlock(partialTicks, yaw);
      Block north = this.getBlock(northPos);
      if (north != null && north != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(northPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)((Integer)this.holeX.getValue() + 16), (float)(Integer)this.holeY.getValue(), (float)((Integer)this.holeX.getValue() + 32), (float)((Integer)this.holeY.getValue() + 16), 1627324416);
         }

         this.drawBlock(north, (float)((Integer)this.holeX.getValue() + 16), (float)(Integer)this.holeY.getValue());
      }

      BlockPos southPos = this.traceToBlock(partialTicks, yaw - 180.0F);
      Block south = this.getBlock(southPos);
      if (south != null && south != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(southPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)((Integer)this.holeX.getValue() + 16), (float)((Integer)this.holeY.getValue() + 32), (float)((Integer)this.holeX.getValue() + 32), (float)((Integer)this.holeY.getValue() + 48), 1627324416);
         }

         this.drawBlock(south, (float)((Integer)this.holeX.getValue() + 16), (float)((Integer)this.holeY.getValue() + 32));
      }

      BlockPos eastPos = this.traceToBlock(partialTicks, yaw + 90.0F);
      Block east = this.getBlock(eastPos);
      if (east != null && east != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(eastPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)((Integer)this.holeX.getValue() + 32), (float)((Integer)this.holeY.getValue() + 16), (float)((Integer)this.holeX.getValue() + 48), (float)((Integer)this.holeY.getValue() + 32), 1627324416);
         }

         this.drawBlock(east, (float)((Integer)this.holeX.getValue() + 32), (float)((Integer)this.holeY.getValue() + 16));
      }

      BlockPos westPos = this.traceToBlock(partialTicks, yaw - 90.0F);
      Block west = this.getBlock(westPos);
      if (west != null && west != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(westPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)(Integer)this.holeX.getValue(), (float)((Integer)this.holeY.getValue() + 16), (float)((Integer)this.holeX.getValue() + 16), (float)((Integer)this.holeY.getValue() + 32), 1627324416);
         }

         this.drawBlock(west, (float)(Integer)this.holeX.getValue(), (float)((Integer)this.holeY.getValue() + 16));
      }

   }

   public void drawOverlay(float partialTicks, Entity player, int x, int y) {
      float yaw = 0.0F;
      int dir = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
      switch(dir) {
      case 1:
         yaw = 90.0F;
         break;
      case 2:
         yaw = -180.0F;
         break;
      case 3:
         yaw = -90.0F;
      }

      BlockPos northPos = this.traceToBlock(partialTicks, yaw, player);
      Block north = this.getBlock(northPos);
      if (north != null && north != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(northPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)(x + 16), (float)y, (float)(x + 32), (float)(y + 16), 1627324416);
         }

         this.drawBlock(north, (float)(x + 16), (float)y);
      }

      BlockPos southPos = this.traceToBlock(partialTicks, yaw - 180.0F, player);
      Block south = this.getBlock(southPos);
      if (south != null && south != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(southPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)(x + 16), (float)(y + 32), (float)(x + 32), (float)(y + 48), 1627324416);
         }

         this.drawBlock(south, (float)(x + 16), (float)(y + 32));
      }

      BlockPos eastPos = this.traceToBlock(partialTicks, yaw + 90.0F, player);
      Block east = this.getBlock(eastPos);
      if (east != null && east != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(eastPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)(x + 32), (float)(y + 16), (float)(x + 48), (float)(y + 32), 1627324416);
         }

         this.drawBlock(east, (float)(x + 32), (float)(y + 16));
      }

      BlockPos westPos = this.traceToBlock(partialTicks, yaw - 90.0F, player);
      Block west = this.getBlock(westPos);
      if (west != null && west != Blocks.field_150350_a) {
         int damage = this.getBlockDamage(westPos);
         if (damage != 0) {
            RenderUtil.drawRect((float)x, (float)(y + 16), (float)(x + 16), (float)(y + 32), 1627324416);
         }

         this.drawBlock(west, (float)x, (float)(y + 16));
      }

   }

   private int getBlockDamage(BlockPos pos) {
      Iterator var2 = mc.field_71438_f.field_72738_E.values().iterator();

      DestroyBlockProgress destBlockProgress;
      do {
         if (!var2.hasNext()) {
            return 0;
         }

         destBlockProgress = (DestroyBlockProgress)var2.next();
      } while(destBlockProgress.func_180246_b().func_177958_n() != pos.func_177958_n() || destBlockProgress.func_180246_b().func_177956_o() != pos.func_177956_o() || destBlockProgress.func_180246_b().func_177952_p() != pos.func_177952_p());

      return destBlockProgress.func_73106_e();
   }

   private BlockPos traceToBlock(float partialTicks, float yaw) {
      Vec3d pos = EntityUtil.interpolateEntity(mc.field_71439_g, partialTicks);
      Vec3d dir = MathUtil.direction(yaw);
      return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
   }

   private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
      Vec3d pos = EntityUtil.interpolateEntity(player, partialTicks);
      Vec3d dir = MathUtil.direction(yaw);
      return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
   }

   private Block getBlock(BlockPos pos) {
      Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
      return block != Blocks.field_150357_h && block != Blocks.field_150343_Z ? Blocks.field_150350_a : block;
   }

   private void drawBlock(Block block, float x, float y) {
      ItemStack stack = new ItemStack(block);
      GlStateManager.func_179094_E();
      GlStateManager.func_179147_l();
      GlStateManager.func_179120_a(770, 771, 1, 0);
      RenderHelper.func_74520_c();
      GlStateManager.func_179109_b(x, y, 0.0F);
      mc.func_175599_af().field_77023_b = 501.0F;
      mc.func_175599_af().func_180450_b(stack, 0, 0);
      mc.func_175599_af().field_77023_b = 0.0F;
      RenderHelper.func_74518_a();
      GlStateManager.func_179084_k();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179121_F();
   }

   public void renderInventory() {
      this.boxrender((Integer)this.invX.getValue() + (Integer)this.fineinvX.getValue(), (Integer)this.invY.getValue() + (Integer)this.fineinvY.getValue());
      this.itemrender(mc.field_71439_g.field_71071_by.field_70462_a, (Integer)this.invX.getValue() + (Integer)this.fineinvX.getValue(), (Integer)this.invY.getValue() + (Integer)this.fineinvY.getValue());
   }

   private static void preboxrender() {
      GL11.glPushMatrix();
      GlStateManager.func_179094_E();
      GlStateManager.func_179118_c();
      GlStateManager.func_179086_m(256);
      GlStateManager.func_179147_l();
      GlStateManager.func_179131_c(255.0F, 255.0F, 255.0F, 255.0F);
   }

   private static void postboxrender() {
      GlStateManager.func_179084_k();
      GlStateManager.func_179097_i();
      GlStateManager.func_179140_f();
      GlStateManager.func_179126_j();
      GlStateManager.func_179141_d();
      GlStateManager.func_179121_F();
      GL11.glPopMatrix();
   }

   private static void preitemrender() {
      GL11.glPushMatrix();
      GL11.glDepthMask(true);
      GlStateManager.func_179086_m(256);
      GlStateManager.func_179097_i();
      GlStateManager.func_179126_j();
      RenderHelper.func_74519_b();
      GlStateManager.func_179152_a(1.0F, 1.0F, 0.01F);
   }

   private static void postitemrender() {
      GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F);
      RenderHelper.func_74518_a();
      GlStateManager.func_179141_d();
      GlStateManager.func_179084_k();
      GlStateManager.func_179140_f();
      GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
      GlStateManager.func_179097_i();
      GlStateManager.func_179126_j();
      GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
      GL11.glPopMatrix();
   }

   private void boxrender(int x, int y) {
      preboxrender();
      mc.field_71446_o.func_110577_a(box);
      RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
      RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + (Integer)this.invH.getValue(), 500);
      RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
      postboxrender();
   }

   private void itemrender(NonNullList<ItemStack> items, int x, int y) {
      int i;
      int iX;
      for(i = 0; i < items.size() - 9; ++i) {
         iX = x + i % 9 * 18 + 8;
         int iY = y + i / 9 * 18 + 18;
         ItemStack itemStack = (ItemStack)items.get(i + 9);
         preitemrender();
         mc.func_175599_af().field_77023_b = 501.0F;
         RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
         RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, iY, (String)null);
         mc.func_175599_af().field_77023_b = 0.0F;
         postitemrender();
      }

      if ((Boolean)this.renderXCarry.getValue()) {
         for(i = 1; i < 5; ++i) {
            iX = x + (i + 4) % 9 * 18 + 8;
            ItemStack itemStack = ((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(i)).func_75211_c();
            if (itemStack != null && !itemStack.field_190928_g) {
               preitemrender();
               mc.func_175599_af().field_77023_b = 501.0F;
               RenderUtil.itemRender.func_180450_b(itemStack, iX, y + 1);
               RenderUtil.itemRender.func_180453_a(mc.field_71466_p, itemStack, iX, y + 1, (String)null);
               mc.func_175599_af().field_77023_b = 0.0F;
               postitemrender();
            }
         }
      }

   }

   public static void drawCompleteImage(int posX, int posY, int width, int height) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)posX, (float)posY, 0.0F);
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex3f(0.0F, 0.0F, 0.0F);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex3f(0.0F, (float)height, 0.0F);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex3f((float)width, (float)height, 0.0F);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex3f((float)width, 0.0F, 0.0F);
      GL11.glEnd();
      GL11.glPopMatrix();
   }

   public static enum Compass {
      NONE,
      CIRCLE,
      LINE;
   }

   private static enum Direction {
      N,
      W,
      S,
      E;
   }
}
