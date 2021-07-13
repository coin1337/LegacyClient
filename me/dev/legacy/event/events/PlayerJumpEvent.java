package me.dev.legacy.event.events;

import me.dev.legacy.event.EventStage;

public class PlayerJumpEvent extends EventStage {
   public double motionX;
   public double motionY;

   public PlayerJumpEvent(double motionX, double motionY) {
      this.motionX = motionX;
      this.motionY = motionY;
   }
}
