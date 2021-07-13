package me.dev.legacy.features.modules.movement;

import com.google.common.eventbus.Subscribe;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class NoFall extends Module {
   private final Setting<Integer> fallDist = this.register(new Setting("FallDist", 4, 3, 40, (v) -> {
      return this.page.getValue() == NoFall.Page.Old;
   }));
   private final Setting<Integer> fallDist2 = this.register(new Setting("FallDist 2", 15, 10, 40, (v) -> {
      return this.page.getValue() == NoFall.Page.Predict;
   }));
   private final Setting<NoFall.Page> page;

   public NoFall() {
      super("NoFall", "NoFall.", Module.Category.MOVEMENT, true, false, false);
      this.page = this.register(new Setting("Page", NoFall.Page.Predict));
   }

   @Subscribe
   public void onTick(TickEvent event) {
      if (mc.field_71441_e != null) {
         if (this.page.getValue() == NoFall.Page.Predict) {
            Vec3d vec = new Vec3d(mc.field_71439_g.field_70142_S + (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70142_S) * (double)mc.func_184121_ak(), mc.field_71439_g.field_70137_T + (mc.field_71439_g.field_70163_u - mc.field_71439_g.field_70137_T) * (double)mc.func_184121_ak(), mc.field_71439_g.field_70136_U + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70136_U) * (double)mc.func_184121_ak());
            BlockPos pos = new BlockPos(vec.field_72450_a, vec.field_72448_b - 2.0D, vec.field_72449_c);
            BlockPos[] posList = new BlockPos[]{pos.func_177978_c(), pos.func_177968_d(), pos.func_177974_f(), pos.func_177976_e(), pos.func_177977_b(), pos.func_177977_b()};
            BlockPos[] var5 = posList;
            int var6 = posList.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               BlockPos blockPos = var5[var7];
               Block block = mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
               if (mc.field_71439_g.field_71093_bK == 1) {
                  if (mc.field_71439_g.field_70143_R > (float)(Integer)this.fallDist2.getValue()) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new Position(0.0D, 64.0D, 0.0D, false));
                     mc.field_71439_g.field_70143_R = (float)((Integer)this.fallDist.getValue() + 1);
                  }

                  if (mc.field_71439_g.field_70143_R > (float)(Integer)this.fallDist.getValue() && block != Blocks.field_150350_a) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new Position(0.0D, 64.0D, 0.0D, false));
                     mc.field_71439_g.field_70143_R = 0.0F;
                  }
               } else {
                  if (mc.field_71439_g.field_70143_R > (float)(Integer)this.fallDist2.getValue()) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, false));
                     mc.field_71439_g.field_70143_R = (float)((Integer)this.fallDist.getValue() + 1);
                  }

                  if (mc.field_71439_g.field_70143_R > (float)(Integer)this.fallDist.getValue() && block != Blocks.field_150350_a) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, false));
                     mc.field_71439_g.field_70143_R = 0.0F;
                  }
               }
            }
         }

         if (this.page.getValue() == NoFall.Page.Old && mc.field_71439_g.field_70143_R > (float)(Integer)this.fallDist.getValue()) {
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, 0.0D, mc.field_71439_g.field_70161_v, false));
            mc.field_71439_g.field_70143_R = 0.0F;
         }

      }
   }

   public static enum Page {
      Predict,
      Old;
   }
}
