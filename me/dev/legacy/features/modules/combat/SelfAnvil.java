package me.dev.legacy.features.modules.combat;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.BlockUtil;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.InventoryUtil;
import me.dev.legacy.util.PlayerUtil;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class SelfAnvil extends Module {
   Setting<Integer> ammount = this.register(new Setting("Ammount", 1, 1, 2));
   Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
   private int placedAmmount;

   public SelfAnvil() {
      super("SelfAnvil", "Drops anvil on you.", Module.Category.COMBAT, true, false, false);
   }

   public void onEnable() {
      this.placedAmmount = 0;
      if (InventoryUtil.findHotbarBlock(BlockAnvil.class) == -1 || PlayerUtil.findObiInHotbar() == -1) {
         this.disable();
      }

   }

   public void onTick() {
      EntityPlayer target = mc.field_71439_g;
      if (target != null && this.placedAmmount < (Integer)this.ammount.getValue()) {
         BlockPos anvilPos = EntityUtil.getFlooredPos(target).func_177981_b(2);
         if (BlockUtil.canPlaceBlock(anvilPos)) {
            this.placeAnvil(anvilPos);
            ++this.placedAmmount;
         } else {
            this.placeObi(anvilPos.func_177977_b().func_177974_f());
            this.placeObi(anvilPos.func_177974_f());
         }

      }
   }

   private void placeAnvil(BlockPos pos) {
      int old = mc.field_71439_g.field_71071_by.field_70461_c;
      this.switchToSlot(InventoryUtil.findHotbarBlock(BlockAnvil.class));
      BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), true, false);
      this.switchToSlot(old);
      this.toggle();
   }

   private void placeObi(BlockPos pos) {
      int old = mc.field_71439_g.field_71071_by.field_70461_c;
      this.switchToSlot(PlayerUtil.findObiInHotbar());
      BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), true, false);
      this.switchToSlot(old);
   }

   private void switchToSlot(int slot) {
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(slot));
      mc.field_71439_g.field_71071_by.field_70461_c = slot;
      mc.field_71442_b.func_78765_e();
   }
}
