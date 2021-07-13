package me.dev.legacy.features.modules.render;

import java.awt.Color;
import me.dev.legacy.event.events.Render3DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.ColorUtil;
import me.dev.legacy.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class BlockHighlight extends Module {
   private final Setting<Float> lineWidth = this.register(new Setting("LineWidth", 1.0F, 0.1F, 5.0F));
   private final Setting<Integer> alpha = this.register(new Setting("Alpha", 255, 0, 255));
   private final Setting<Integer> red = this.register(new Setting("Red", 255, 0, 255));
   private final Setting<Integer> green = this.register(new Setting("Green", 255, 0, 255));
   private final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255));
   private final Setting<Boolean> rainbow = this.register(new Setting("Rainbow", false));
   private final Setting<Integer> rainbowhue = this.register(new Setting("RainbowHue", 255, 0, 255, (v) -> {
      return (Boolean)this.rainbow.getValue();
   }));

   public BlockHighlight() {
      super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
   }

   public void onRender3D(Render3DEvent event) {
      RayTraceResult ray = mc.field_71476_x;
      if (ray != null && ray.field_72313_a == Type.BLOCK) {
         BlockPos blockpos = ray.func_178782_a();
         RenderUtil.drawBlockOutline(blockpos, (Boolean)this.rainbow.getValue() ? ColorUtil.rainbow((Integer)this.rainbowhue.getValue()) : new Color((Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue(), (Integer)this.alpha.getValue()), (Float)this.lineWidth.getValue(), false);
      }

   }
}
