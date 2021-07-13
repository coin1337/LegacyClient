package me.dev.legacy.manager;

import me.dev.legacy.features.Feature;

public class TimerManager extends Feature {
   private float timer = 1.0F;

   public void unload() {
      this.timer = 1.0F;
      mc.field_71428_T.field_194149_e = 50.0F;
   }

   public void update() {
      mc.field_71428_T.field_194149_e = 50.0F / (this.timer <= 0.0F ? 0.1F : this.timer);
   }

   public void setTimer(float timer) {
      if (timer > 0.0F) {
         this.timer = timer;
      }

   }

   public float getTimer() {
      return this.timer;
   }

   public void reset() {
      this.timer = 1.0F;
   }
}
