package me.dev.legacy.features.modules.combat;

import java.util.Iterator;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.BurrowUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Burrow extends Module {
   private final Setting<Integer> offset = this.register(new Setting("Offset", 3, -5, 5));
   private final Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
   private final Setting<Boolean> echest = this.register(new Setting("Use echest", false));
   private BlockPos originalPos;
   private int oldSlot = -1;

   public Burrow() {
      super("Burrow", "TPs you into a block", Module.Category.COMBAT, true, false, false);
   }

   public void onEnable() {
      super.onEnable();
      this.originalPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
      if (!mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)).func_177230_c().equals(Blocks.field_150343_Z) && !this.intersectsWithEntity(this.originalPos)) {
         this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
      } else {
         this.toggle();
      }
   }

   public void onUpdate() {
      label27: {
         if ((Boolean)this.echest.getValue()) {
            if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
               break label27;
            }
         } else if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            break label27;
         }

         BurrowUtil.switchToSlot((Boolean)this.echest.getValue() ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockObsidian.class));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.41999998688698D, mc.field_71439_g.field_70161_v, true));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.7531999805211997D, mc.field_71439_g.field_70161_v, true));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.00133597911214D, mc.field_71439_g.field_70161_v, true));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.16610926093821D, mc.field_71439_g.field_70161_v, true));
         BurrowUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), true, false);
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)(Integer)this.offset.getValue(), mc.field_71439_g.field_70161_v, false));
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
         mc.field_71439_g.func_70095_a(false);
         BurrowUtil.switchToSlot(this.oldSlot);
         this.toggle();
         return;
      }

      Command.sendMessage((Boolean)this.echest.getValue() ? "Can't find echest in hotbar!" : "Can't find obby in hotbar!");
      this.toggle();
   }

   private boolean intersectsWithEntity(BlockPos pos) {
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      Entity entity;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         entity = (Entity)var2.next();
      } while(entity.equals(mc.field_71439_g) || entity instanceof EntityItem || !(new AxisAlignedBB(pos)).func_72326_a(entity.func_174813_aQ()));

      return true;
   }
}
