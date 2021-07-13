package me.dev.legacy.features.modules.player;

import me.dev.legacy.event.events.MoveEvent;
import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Freecam extends Module {
   private static Freecam INSTANCE = new Freecam();
   public Setting<Double> speed = this.register(new Setting("Speed", 0.5D, 0.1D, 5.0D));
   public Setting<Boolean> packet = this.register(new Setting("Cancel Packets", true));
   private double posX;
   private double posY;
   private double posZ;
   private float pitch;
   private float yaw;
   private EntityOtherPlayerMP clonedPlayer;
   private boolean isRidingEntity;
   private Entity ridingEntity;

   public Freecam() {
      super("Freecam", "Look around freely.", Module.Category.PLAYER, true, false, false);
      this.setInstance();
   }

   public static Freecam getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new Freecam();
      }

      return INSTANCE;
   }

   private void setInstance() {
      INSTANCE = this;
   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         this.isRidingEntity = mc.field_71439_g.func_184187_bx() != null;
         if (mc.field_71439_g.func_184187_bx() == null) {
            this.posX = mc.field_71439_g.field_70165_t;
            this.posY = mc.field_71439_g.field_70163_u;
            this.posZ = mc.field_71439_g.field_70161_v;
         } else {
            this.ridingEntity = mc.field_71439_g.func_184187_bx();
            mc.field_71439_g.func_184210_p();
         }

         this.pitch = mc.field_71439_g.field_70125_A;
         this.yaw = mc.field_71439_g.field_70177_z;
         this.clonedPlayer = new EntityOtherPlayerMP(mc.field_71441_e, mc.func_110432_I().func_148256_e());
         this.clonedPlayer.func_82149_j(mc.field_71439_g);
         this.clonedPlayer.field_70759_as = mc.field_71439_g.field_70759_as;
         mc.field_71441_e.func_73027_a(-100, this.clonedPlayer);
         mc.field_71439_g.field_71075_bZ.field_75100_b = true;
         mc.field_71439_g.field_71075_bZ.func_75092_a((float)((Double)this.speed.getValue() / 100.0D));
         mc.field_71439_g.field_70145_X = true;
      }

   }

   public void onDisable() {
      EntityPlayer localPlayer = mc.field_71439_g;
      if (localPlayer != null) {
         mc.field_71439_g.func_70080_a(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
         mc.field_71441_e.func_73028_b(-100);
         this.clonedPlayer = null;
         this.posX = this.posY = this.posZ = 0.0D;
         this.pitch = this.yaw = 0.0F;
         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
         mc.field_71439_g.field_71075_bZ.func_75092_a(0.05F);
         mc.field_71439_g.field_70145_X = false;
         mc.field_71439_g.field_70159_w = mc.field_71439_g.field_70181_x = mc.field_71439_g.field_70179_y = 0.0D;
         if (this.isRidingEntity) {
            mc.field_71439_g.func_184205_a(this.ridingEntity, true);
         }
      }

   }

   public void onUpdate() {
      mc.field_71439_g.field_71075_bZ.field_75100_b = true;
      mc.field_71439_g.field_71075_bZ.func_75092_a((float)((Double)this.speed.getValue() / 100.0D));
      mc.field_71439_g.field_70145_X = true;
      mc.field_71439_g.field_70122_E = false;
      mc.field_71439_g.field_70143_R = 0.0F;
   }

   @SubscribeEvent
   public void onMove(MoveEvent event) {
      mc.field_71439_g.field_70145_X = true;
   }

   @SubscribeEvent
   public void onPlayerPushOutOfBlock(PlayerSPPushOutOfBlocksEvent event) {
      event.setCanceled(true);
   }

   @SubscribeEvent
   public void onPacketSend(PacketEvent event) {
      if ((event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) && (Boolean)this.packet.getValue()) {
         event.setCanceled(true);
      }

   }
}
