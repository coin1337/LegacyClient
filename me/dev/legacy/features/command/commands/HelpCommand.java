package me.dev.legacy.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import me.dev.legacy.Client;
import me.dev.legacy.features.command.Command;

public class HelpCommand extends Command {
   public HelpCommand() {
      super("help");
   }

   public void execute(String[] commands) {
      sendMessage("Commands: ");
      Iterator var2 = Client.commandManager.getCommands().iterator();

      while(var2.hasNext()) {
         Command command = (Command)var2.next();
         sendMessage(ChatFormatting.GRAY + Client.commandManager.getPrefix() + command.getName());
      }

   }
}
