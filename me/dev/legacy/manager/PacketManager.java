package me.dev.legacy.manager;

import java.util.ArrayList;
import java.util.List;
import me.dev.legacy.features.Feature;
import net.minecraft.network.Packet;

public class PacketManager extends Feature {
   private final List<Packet<?>> noEventPackets = new ArrayList();

   public void sendPacketNoEvent(Packet<?> packet) {
      if (packet != null && !nullCheck()) {
         this.noEventPackets.add(packet);
         mc.field_71439_g.field_71174_a.func_147297_a(packet);
      }

   }

   public boolean shouldSendPacket(Packet<?> packet) {
      if (this.noEventPackets.contains(packet)) {
         this.noEventPackets.remove(packet);
         return false;
      } else {
         return true;
      }
   }
}
