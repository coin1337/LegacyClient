package me.dev.legacy.mixin.mixins;

import me.dev.legacy.Client;
import me.dev.legacy.event.events.BlockEvent;
import me.dev.legacy.event.events.ProcessRightClickBlockEvent;
import me.dev.legacy.features.modules.player.Reach;
import me.dev.legacy.features.modules.player.TpsSync;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerControllerMP.class})
public class MixinPlayerControllerMP {
   @Redirect(
      method = {"onPlayerDamageBlock"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"
)
   )
   public float getPlayerRelativeBlockHardnessHook(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
      return state.func_185903_a(player, worldIn, pos) * (TpsSync.getInstance().isOn() && (Boolean)TpsSync.getInstance().mining.getValue() ? 1.0F / Client.serverManager.getTpsFactor() : 1.0F);
   }

   @Inject(
      method = {"clickBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
      BlockEvent event = new BlockEvent(3, pos, face);
      MinecraftForge.EVENT_BUS.post(event);
   }

   @Inject(
      method = {"onPlayerDamageBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
      BlockEvent event = new BlockEvent(4, pos, face);
      MinecraftForge.EVENT_BUS.post(event);
   }

   @Inject(
      method = {"getBlockReachDistance"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void getReachDistanceHook(CallbackInfoReturnable<Float> distance) {
      if (Reach.getInstance().isOn()) {
         float range = (Float)distance.getReturnValue();
         distance.setReturnValue((Boolean)Reach.getInstance().override.getValue() ? (Float)Reach.getInstance().reach.getValue() : range + (Float)Reach.getInstance().add.getValue());
      }

   }

   @Redirect(
      method = {"processRightClickBlock"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/item/ItemBlock;canPlaceBlockOnSide(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z"
)
   )
   public boolean canPlaceBlockOnSideHook(ItemBlock itemBlock, World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
      Block block = worldIn.func_180495_p(pos).func_177230_c();
      if (block == Blocks.field_150431_aC && block.func_176200_f(worldIn, pos)) {
         side = EnumFacing.UP;
      } else if (!block.func_176200_f(worldIn, pos)) {
         pos = pos.func_177972_a(side);
      }

      IBlockState iblockstate1 = worldIn.func_180495_p(pos);
      AxisAlignedBB axisalignedbb = itemBlock.field_150939_a.func_176223_P().func_185890_d(worldIn, pos);
      if ((axisalignedbb == Block.field_185506_k || worldIn.func_72917_a(axisalignedbb.func_186670_a(pos), (Entity)null)) && iblockstate1.func_185904_a() == Material.field_151594_q && itemBlock.field_150939_a == Blocks.field_150467_bQ) {
         return true;
      } else {
         return iblockstate1.func_177230_c().func_176200_f(worldIn, pos) && itemBlock.field_150939_a.func_176198_a(worldIn, pos, side);
      }
   }

   @Inject(
      method = {"processRightClickBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
      ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(pos, hand, Minecraft.func_71410_x().field_71439_g.func_184586_b(hand));
      MinecraftForge.EVENT_BUS.post(event);
      if (event.isCanceled()) {
         cir.cancel();
      }

   }
}
