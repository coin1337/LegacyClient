package me.dev.legacy.features.modules.misc;

import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.features.modules.Module;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BuildHeight extends Module {
   public BuildHeight() {
      super("BuildHeight", "Allows you to place at build height", Module.Category.MISC, true, false, false);
   }

   @SubscribeEvent
   public void onPacketSend(PacketEvent.Send event) {
      CPacketPlayerTryUseItemOnBlock packet;
      if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).func_187023_a().func_177956_o() >= 255 && packet.func_187024_b() == EnumFacing.UP) {
         packet.field_149579_d = EnumFacing.DOWN;
      }

   }
}
