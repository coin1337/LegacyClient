package me.dev.legacy.util;

import java.util.Iterator;
import me.dev.legacy.MinecraftInstance;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WorldUtil implements MinecraftInstance {
   public static void placeBlock(BlockPos pos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing enumFacing = var1[var3];
         if (!mc.field_71441_e.func_180495_p(pos.func_177972_a(enumFacing)).func_177230_c().equals(Blocks.field_150350_a) && !isIntercepted(pos)) {
            Vec3d vec = new Vec3d((double)pos.func_177958_n() + 0.5D + (double)enumFacing.func_82601_c() * 0.5D, (double)pos.func_177956_o() + 0.5D + (double)enumFacing.func_96559_d() * 0.5D, (double)pos.func_177952_p() + 0.5D + (double)enumFacing.func_82599_e() * 0.5D);
            float[] old = new float[]{mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A};
            mc.field_71439_g.field_71174_a.func_147297_a(new Rotation((float)Math.toDegrees(Math.atan2(vec.field_72449_c - mc.field_71439_g.field_70161_v, vec.field_72450_a - mc.field_71439_g.field_70165_t)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(vec.field_72448_b - (mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e()), Math.sqrt((vec.field_72450_a - mc.field_71439_g.field_70165_t) * (vec.field_72450_a - mc.field_71439_g.field_70165_t) + (vec.field_72449_c - mc.field_71439_g.field_70161_v) * (vec.field_72449_c - mc.field_71439_g.field_70161_v))))), mc.field_71439_g.field_70122_E));
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
            mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, pos.func_177972_a(enumFacing), enumFacing.func_176734_d(), new Vec3d(pos), EnumHand.MAIN_HAND);
            mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
            mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(old[0], old[1], mc.field_71439_g.field_70122_E));
            return;
         }
      }

   }

   public static void placeBlock(BlockPos pos, int slot) {
      if (slot != -1) {
         int prev = mc.field_71439_g.field_71071_by.field_70461_c;
         mc.field_71439_g.field_71071_by.field_70461_c = slot;
         placeBlock(pos);
         mc.field_71439_g.field_71071_by.field_70461_c = prev;
      }
   }

   public static boolean isIntercepted(BlockPos pos) {
      Iterator var1 = mc.field_71441_e.field_72996_f.iterator();

      Entity entity;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         entity = (Entity)var1.next();
      } while(!(new AxisAlignedBB(pos)).func_72326_a(entity.func_174813_aQ()));

      return true;
   }

   public static BlockPos GetLocalPlayerPosFloored() {
      return new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
   }

   public static boolean canBreak(BlockPos pos) {
      return mc.field_71441_e.func_180495_p(pos).func_177230_c().func_176195_g(mc.field_71441_e.func_180495_p(pos), mc.field_71441_e, pos) != -1.0F;
   }
}
