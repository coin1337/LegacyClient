package me.dev.legacy.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Packet extends Event {
   private Object packet;
   private Packet.Type type;

   public Packet(Object packet, Packet.Type type) {
      this.packet = packet;
      this.type = type;
   }

   public void setPacket(Object packet) {
      this.packet = packet;
   }

   public void setType(Packet.Type type) {
      this.type = type;
   }

   public Object getPacket() {
      return this.packet;
   }

   public Packet.Type getType() {
      return this.type;
   }

   public static enum Type {
      INCOMING,
      OUTGOING;
   }
}
