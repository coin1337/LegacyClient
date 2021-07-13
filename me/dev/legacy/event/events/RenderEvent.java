package me.dev.legacy.event.events;

import me.dev.legacy.event.EventStage;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;

public class RenderEvent extends EventStage {
   private Vec3d renderPos;
   private Tessellator tessellator;
   private final float partialTicks;

   public void resetTranslation() {
      this.setTranslation(this.renderPos);
   }

   public Vec3d getRenderPos() {
      return this.renderPos;
   }

   public BufferBuilder getBuffer() {
      return this.tessellator.func_178180_c();
   }

   public Tessellator getTessellator() {
      return this.tessellator;
   }

   public RenderEvent(Tessellator paramTessellator, Vec3d paramVec3d, float ticks) {
      this.tessellator = paramTessellator;
      this.renderPos = paramVec3d;
      this.partialTicks = ticks;
   }

   public void setTranslation(Vec3d paramVec3d) {
      this.getBuffer().func_178969_c(-paramVec3d.field_72450_a, -paramVec3d.field_72448_b, -paramVec3d.field_72449_c);
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
