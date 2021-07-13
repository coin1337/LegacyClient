package me.dev.legacy.manager;

import com.google.common.base.Strings;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import me.dev.legacy.Client;
import me.dev.legacy.event.events.ConnectionEvent;
import me.dev.legacy.event.events.DeathEvent;
import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.event.events.Render2DEvent;
import me.dev.legacy.event.events.Render3DEvent;
import me.dev.legacy.event.events.TotemPopEvent;
import me.dev.legacy.event.events.UpdateWalkingPlayerEvent;
import me.dev.legacy.features.Feature;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.client.HUD;
import me.dev.legacy.features.modules.misc.PopCounter;
import me.dev.legacy.util.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import org.lwjgl.input.Keyboard;

public class EventManager extends Feature {
   private final Timer logoutTimer = new Timer();
   private final AtomicBoolean tickOngoing = new AtomicBoolean(false);

   public void init() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   public boolean ticksOngoing() {
      return this.tickOngoing.get();
   }

   public void onUnload() {
      MinecraftForge.EVENT_BUS.unregister(this);
   }

   @SubscribeEvent
   public void onUpdate(LivingUpdateEvent event) {
      if (!fullNullCheck() && event.getEntity().func_130014_f_().field_72995_K && event.getEntityLiving().equals(mc.field_71439_g)) {
         Client.inventoryManager.update();
         Client.moduleManager.onUpdate();
         if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
            Client.moduleManager.sortModules(true);
         } else {
            Client.moduleManager.sortModulesABC();
         }
      }

   }

   @SubscribeEvent
   public void onClientConnect(ClientConnectedToServerEvent event) {
      this.logoutTimer.reset();
      Client.moduleManager.onLogin();
   }

   @SubscribeEvent
   public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
      Client.moduleManager.onLogout();
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (!fullNullCheck()) {
         Client.moduleManager.onTick();
         Iterator var2 = mc.field_71441_e.field_73010_i.iterator();

         while(var2.hasNext()) {
            EntityPlayer player = (EntityPlayer)var2.next();
            if (player != null && !(player.func_110143_aJ() > 0.0F)) {
               MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
               PopCounter.getInstance().onDeath(player);
            }
         }

      }
   }

   @SubscribeEvent
   public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
      if (!fullNullCheck()) {
         if (event.getStage() == 0) {
            Client.speedManager.updateValues();
            Client.rotationManager.updateRotations();
            Client.positionManager.updatePosition();
         }

         if (event.getStage() == 1) {
            Client.rotationManager.restoreRotations();
            Client.positionManager.restorePosition();
         }

      }
   }

   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getStage() == 0) {
         Client.serverManager.onPacketReceived();
         if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.func_149160_c() == 35 && packet.func_149161_a(mc.field_71441_e) instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)packet.func_149161_a(mc.field_71441_e);
               MinecraftForge.EVENT_BUS.post(new TotemPopEvent(player));
               PopCounter.getInstance().onTotemPop(player);
            }
         }

         if (event.getPacket() instanceof SPacketPlayerListItem && !fullNullCheck() && this.logoutTimer.passedS(1.0D)) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (!Action.ADD_PLAYER.equals(packet.func_179768_b()) && !Action.REMOVE_PLAYER.equals(packet.func_179768_b())) {
               return;
            }

            packet.func_179767_a().stream().filter(Objects::nonNull).filter((data) -> {
               return !Strings.isNullOrEmpty(data.func_179962_a().getName()) || data.func_179962_a().getId() != null;
            }).forEach((data) -> {
               UUID id = data.func_179962_a().getId();
               switch(packet.func_179768_b()) {
               case ADD_PLAYER:
                  String name = data.func_179962_a().getName();
                  MinecraftForge.EVENT_BUS.post(new ConnectionEvent(0, id, name));
                  break;
               case REMOVE_PLAYER:
                  EntityPlayer entity = mc.field_71441_e.func_152378_a(id);
                  if (entity != null) {
                     String logoutName = entity.func_70005_c_();
                     MinecraftForge.EVENT_BUS.post(new ConnectionEvent(1, entity, id, logoutName));
                  } else {
                     MinecraftForge.EVENT_BUS.post(new ConnectionEvent(2, id, (String)null));
                  }
               }

            });
         }

         if (event.getPacket() instanceof SPacketTimeUpdate) {
            Client.serverManager.update();
         }

      }
   }

   @SubscribeEvent
   public void onWorldRender(RenderWorldLastEvent event) {
      if (!event.isCanceled()) {
         mc.field_71424_I.func_76320_a("oyvey");
         GlStateManager.func_179090_x();
         GlStateManager.func_179147_l();
         GlStateManager.func_179118_c();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179103_j(7425);
         GlStateManager.func_179097_i();
         GlStateManager.func_187441_d(1.0F);
         Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
         Client.moduleManager.onRender3D(render3dEvent);
         GlStateManager.func_187441_d(1.0F);
         GlStateManager.func_179103_j(7424);
         GlStateManager.func_179084_k();
         GlStateManager.func_179141_d();
         GlStateManager.func_179098_w();
         GlStateManager.func_179126_j();
         GlStateManager.func_179089_o();
         GlStateManager.func_179089_o();
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179098_w();
         GlStateManager.func_179147_l();
         GlStateManager.func_179126_j();
         mc.field_71424_I.func_76319_b();
      }
   }

   @SubscribeEvent
   public void renderHUD(Post event) {
      if (event.getType() == ElementType.HOTBAR) {
         Client.textManager.updateResolution();
      }

   }

   @SubscribeEvent(
      priority = EventPriority.LOW
   )
   public void onRenderGameOverlayEvent(Text event) {
      if (event.getType().equals(ElementType.TEXT)) {
         ScaledResolution resolution = new ScaledResolution(mc);
         Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
         Client.moduleManager.onRender2D(render2DEvent);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL,
      receiveCanceled = true
   )
   public void onKeyInput(KeyInputEvent event) {
      if (Keyboard.getEventKeyState()) {
         Client.moduleManager.onKeyPressed(Keyboard.getEventKey());
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onChatSent(ClientChatEvent event) {
      if (event.getMessage().startsWith(Command.getCommandPrefix())) {
         event.setCanceled(true);

         try {
            mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
            if (event.getMessage().length() > 1) {
               Client.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
            } else {
               Command.sendMessage("Please enter a command.");
            }
         } catch (Exception var3) {
            var3.printStackTrace();
            Command.sendMessage(ChatFormatting.RED + "An error occurred while running this command. Check the log!");
         }
      }

   }
}
