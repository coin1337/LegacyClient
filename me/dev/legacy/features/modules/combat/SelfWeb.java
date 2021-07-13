package me.dev.legacy.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.BlockInteractionUtil;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SelfWeb extends Module {
   public Setting<Boolean> alwayson = this.register(new Setting("AlwaysOn", false));
   public Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
   public Setting<Integer> webRange = this.register(new Setting("EnemyRange", 4, 0, 8));
   int new_slot = -1;
   boolean sneak = false;

   public SelfWeb() {
      super("SelfWeb", "Places webs at your feet", Module.Category.COMBAT, false, false, false);
   }

   public void enable() {
      if (mc.field_71439_g != null) {
         this.new_slot = this.find_in_hotbar();
         if (this.new_slot == -1) {
            Command.sendMessage(ChatFormatting.RED + "< " + ChatFormatting.GRAY + "SelfWeb" + ChatFormatting.RED + "> " + ChatFormatting.DARK_RED + "No webs in hotbar!");
         }
      }

   }

   public void disable() {
      if (mc.field_71439_g != null && this.sneak) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
         this.sneak = false;
      }

   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if ((Boolean)this.alwayson.getValue()) {
            EntityPlayer target = this.find_closest_target();
            if (target == null) {
               return;
            }

            if (mc.field_71439_g.func_70032_d(target) < (float)(Integer)this.webRange.getValue() && this.is_surround()) {
               int last_slot = mc.field_71439_g.field_71071_by.field_70461_c;
               mc.field_71439_g.field_71071_by.field_70461_c = this.new_slot;
               mc.field_71442_b.func_78765_e();
               this.place_blocks(WorldUtil.GetLocalPlayerPosFloored());
               mc.field_71439_g.field_71071_by.field_70461_c = last_slot;
            }
         } else {
            int last_slot = mc.field_71439_g.field_71071_by.field_70461_c;
            mc.field_71439_g.field_71071_by.field_70461_c = this.new_slot;
            mc.field_71442_b.func_78765_e();
            this.place_blocks(WorldUtil.GetLocalPlayerPosFloored());
            mc.field_71439_g.field_71071_by.field_70461_c = last_slot;
            this.disable();
         }

      }
   }

   public EntityPlayer find_closest_target() {
      if (mc.field_71441_e.field_73010_i.isEmpty()) {
         return null;
      } else {
         EntityPlayer closestTarget = null;
         Iterator var2 = mc.field_71441_e.field_73010_i.iterator();

         while(true) {
            EntityPlayer target;
            do {
               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           return closestTarget;
                        }

                        target = (EntityPlayer)var2.next();
                     } while(target == mc.field_71439_g);
                  } while(EntityUtil.isLiving(target));
               } while(target.func_110143_aJ() <= 0.0F);
            } while(closestTarget != null && mc.field_71439_g.func_70032_d(target) > mc.field_71439_g.func_70032_d(closestTarget));

            closestTarget = target;
         }
      }
   }

   private int find_in_hotbar() {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack.func_77973_b() == Item.func_150899_d(30)) {
            return i;
         }
      }

      return -1;
   }

   private boolean is_surround() {
      BlockPos player_block = WorldUtil.GetLocalPlayerPosFloored();
      return mc.field_71441_e.func_180495_p(player_block.func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block).func_177230_c() == Blocks.field_150350_a;
   }

   private void place_blocks(BlockPos pos) {
      if (mc.field_71441_e.func_180495_p(pos).func_185904_a().func_76222_j()) {
         if (BlockInteractionUtil.checkForNeighbours(pos)) {
            EnumFacing[] var2 = EnumFacing.values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               EnumFacing side = var2[var4];
               BlockPos neighbor = pos.func_177972_a(side);
               EnumFacing side2 = side.func_176734_d();
               if (BlockInteractionUtil.canBeClicked(neighbor)) {
                  if (BlockInteractionUtil.blackList.contains(mc.field_71441_e.func_180495_p(neighbor).func_177230_c())) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
                     this.sneak = true;
                  }

                  Vec3d hitVec = (new Vec3d(neighbor)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(side2.func_176730_m())).func_186678_a(0.5D));
                  if ((Boolean)this.rotate.getValue()) {
                     BlockInteractionUtil.faceVectorPacketInstant(hitVec);
                  }

                  mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                  mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                  return;
               }
            }

         }
      }
   }
}
