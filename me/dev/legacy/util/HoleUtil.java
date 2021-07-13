package me.dev.legacy.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class HoleUtil {
   public static final List<BlockPos> holeBlocks = Arrays.asList(new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1));
   private static Minecraft mc = Minecraft.func_71410_x();
   public static final Vec3d[] cityOffsets = new Vec3d[]{new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D)};

   public static boolean isInHole() {
      Vec3d playerPos = CombatUtil.interpolateEntity(mc.field_71439_g);
      BlockPos blockpos = new BlockPos(playerPos.field_72450_a, playerPos.field_72448_b, playerPos.field_72449_c);
      int size = 0;
      Iterator var3 = holeBlocks.iterator();

      while(var3.hasNext()) {
         BlockPos bPos = (BlockPos)var3.next();
         if (CombatUtil.isHard(mc.field_71441_e.func_180495_p(blockpos.func_177971_a(bPos)).func_177230_c())) {
            ++size;
         }
      }

      return size == 5;
   }
}
