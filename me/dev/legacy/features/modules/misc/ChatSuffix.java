package me.dev.legacy.features.modules.misc;

import me.dev.legacy.features.modules.Module;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix extends Module {
   public ChatSuffix() {
      super("ChatSuffix", "Appends your message", Module.Category.MISC, true, false, false);
   }

   public void onEnable() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onChat(ClientChatEvent event) {
      String SyndiHaxSuffix = " ʟᴇɢᴀᴄʏ";
      if (!event.getMessage().startsWith("/") && !event.getMessage().startsWith(".") && !event.getMessage().startsWith(",") && !event.getMessage().startsWith("-") && !event.getMessage().startsWith("$") && !event.getMessage().startsWith("*")) {
         event.setMessage(event.getMessage() + SyndiHaxSuffix);
      }
   }

   public void onDisable() {
      MinecraftForge.EVENT_BUS.unregister(this);
   }
}
