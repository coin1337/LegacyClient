package me.dev.legacy.event.events;

import me.dev.legacy.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEvent extends EventStage {
   private final net.minecraft.network.Packet<?> packet;

   public PacketEvent(int stage, net.minecraft.network.Packet<?> packet) {
      super(stage);
      this.packet = packet;
   }

   public <T extends net.minecraft.network.Packet<?>> T getPacket() {
      return this.packet;
   }

   @Cancelable
   public static class Receive extends PacketEvent {
      public Receive(int stage, net.minecraft.network.Packet<?> packet) {
         super(stage, packet);
      }
   }

   @Cancelable
   public static class Send extends PacketEvent {
      public Send(int stage, net.minecraft.network.Packet<?> packet) {
         super(stage, packet);
      }
   }
}
