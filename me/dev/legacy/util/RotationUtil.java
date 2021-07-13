package me.dev.legacy.util;

import me.dev.legacy.features.modules.client.ClickGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil implements Util {
   public static Vec3d getEyesPos() {
      return new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
   }

   public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
      double dirx = me.field_70165_t - px;
      double diry = me.field_70163_u - py;
      double dirz = me.field_70161_v - pz;
      double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
      double pitch = Math.asin(diry / len);
      double yaw = Math.atan2(dirz / len, dirx / len);
      pitch = pitch * 180.0D / 3.141592653589793D;
      yaw = yaw * 180.0D / 3.141592653589793D;
      return new double[]{yaw += 90.0D, pitch};
   }

   public static float[] getLegitRotations(Vec3d vec) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.field_72450_a - eyesPos.field_72450_a;
      double diffY = vec.field_72448_b - eyesPos.field_72448_b;
      double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z), mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - mc.field_71439_g.field_70125_A)};
   }

   public static void faceYawAndPitch(float yaw, float pitch) {
      mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(yaw, pitch, mc.field_71439_g.field_70122_E));
   }

   public static void faceVector(Vec3d vec, boolean normalizeAngle) {
      float[] rotations = getLegitRotations(vec);
      mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(rotations[0], normalizeAngle ? (float)MathHelper.func_180184_b((int)rotations[1], 360) : rotations[1], mc.field_71439_g.field_70122_E));
   }

   public static void faceEntity(Entity entity) {
      float[] angle = MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
      faceYawAndPitch(angle[0], angle[1]);
   }

   public static float[] getAngle(Entity entity) {
      return MathUtil.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
   }

   public static int getDirection4D() {
      return MathHelper.func_76128_c((double)(mc.field_71439_g.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
   }

   public static boolean isInFov(BlockPos pos) {
      return pos != null && (mc.field_71439_g.func_174818_b(pos) < 4.0D || isInFov(new Vec3d(pos), mc.field_71439_g.func_174791_d()));
   }

   public static boolean isInFov(Entity entity) {
      return entity != null && (mc.field_71439_g.func_70068_e(entity) < 4.0D || isInFov(entity.func_174791_d(), mc.field_71439_g.func_174791_d()));
   }

   public static boolean isInFov(Vec3d vec3d, Vec3d other) {
      if (mc.field_71439_g.field_70125_A > 30.0F) {
         if (other.field_72448_b > mc.field_71439_g.field_70163_u) {
            return true;
         }
      } else if (mc.field_71439_g.field_70125_A < -30.0F && other.field_72448_b < mc.field_71439_g.field_70163_u) {
         return true;
      }

      float angle = MathUtil.calcAngleNoY(vec3d, other)[0] - transformYaw();
      if (angle < -270.0F) {
         return true;
      } else {
         float fov = ((Boolean)ClickGui.getInstance().customFov.getValue() ? (Float)ClickGui.getInstance().fov.getValue() : mc.field_71474_y.field_74334_X) / 2.0F;
         return angle < fov + 10.0F && angle > -fov - 10.0F;
      }
   }

   public static float transformYaw() {
      float yaw = mc.field_71439_g.field_70177_z % 360.0F;
      if (mc.field_71439_g.field_70177_z > 0.0F && yaw > 180.0F) {
         yaw = -180.0F + (yaw - 180.0F);
      }

      return yaw;
   }

   public static String getDirection4D(boolean northRed) {
      int dirnumber = getDirection4D();
      if (dirnumber == 0) {
         return "South (+Z)";
      } else if (dirnumber == 1) {
         return "West (-X)";
      } else if (dirnumber == 2) {
         return (northRed ? "Â§c" : "") + "North (-Z)";
      } else {
         return dirnumber == 3 ? "East (+X)" : "Loading...";
      }
   }
}
