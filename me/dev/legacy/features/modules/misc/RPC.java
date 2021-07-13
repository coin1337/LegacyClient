package me.dev.legacy.features.modules.misc;

import me.dev.legacy.Discord;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class RPC extends Module {
   public static RPC INSTANCE;
   public Setting<Boolean> showIP = this.register(new Setting("ShowIP", true, "Shows the server IP in your discord presence."));
   public Setting<String> state = this.register(new Setting("State", "Legacy on top", "Sets the state of the DiscordRPC."));

   public RPC() {
      super("RPC", "Discord rich presence", Module.Category.MISC, false, false, false);
      INSTANCE = this;
   }

   public void onEnable() {
      Discord.start();
   }

   public void onDisable() {
      Discord.stop();
   }
}
