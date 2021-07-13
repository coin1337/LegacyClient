package me.dev.legacy.features.modules.player;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class FakePlayer extends Module {
   public Setting<String> fakename = this.register(new Setting("Name", "Skitttyy"));
   private EntityOtherPlayerMP clonedPlayer;

   public FakePlayer() {
      super("FakePlayer", "Spawns a literal fake player", Module.Category.PLAYER, false, false, false);
   }

   public void onEnable() {
      Command.sendMessage("FakePlayer by the name of " + this.fakename.getValueAsString() + " has been spawned!");
      if (mc.field_71439_g != null && !mc.field_71439_g.field_70128_L) {
         this.clonedPlayer = new EntityOtherPlayerMP(mc.field_71441_e, new GameProfile(UUID.fromString("48efc40f-56bf-42c3-aa24-28e0c053f325"), this.fakename.getValueAsString()));
         this.clonedPlayer.func_82149_j(mc.field_71439_g);
         this.clonedPlayer.field_70759_as = mc.field_71439_g.field_70759_as;
         this.clonedPlayer.field_70177_z = mc.field_71439_g.field_70177_z;
         this.clonedPlayer.field_70125_A = mc.field_71439_g.field_70125_A;
         this.clonedPlayer.func_71033_a(GameType.SURVIVAL);
         this.clonedPlayer.func_70606_j(20.0F);
         mc.field_71441_e.func_73027_a(-12345, this.clonedPlayer);
         this.clonedPlayer.func_70636_d();
      } else {
         this.disable();
      }
   }

   public void onDisable() {
      if (mc.field_71441_e != null) {
         mc.field_71441_e.func_73028_b(-12345);
      }

   }

   @SubscribeEvent
   public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
      if (this.isEnabled()) {
         this.disable();
      }

   }
}
