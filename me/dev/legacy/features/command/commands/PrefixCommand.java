package me.dev.legacy.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.legacy.Client;
import me.dev.legacy.features.command.Command;

public class PrefixCommand extends Command {
   public PrefixCommand() {
      super("prefix", new String[]{"<char>"});
   }

   public void execute(String[] commands) {
      if (commands.length == 1) {
         Command.sendMessage(ChatFormatting.GREEN + "Current prefix is " + Client.commandManager.getPrefix());
      } else {
         Client.commandManager.setPrefix(commands[0]);
         Command.sendMessage("Prefix changed to " + ChatFormatting.GRAY + commands[0]);
      }
   }
}
