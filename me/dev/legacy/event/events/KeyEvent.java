package me.dev.legacy.event.events;

import me.dev.legacy.event.EventStage;

public class KeyEvent extends EventStage {
   public boolean info;
   public boolean pressed;

   public KeyEvent(int stage) {
      super(stage);
      this.info = this.info;
      this.pressed = this.pressed;
   }
}
