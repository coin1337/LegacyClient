package me.dev.legacy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import me.dev.legacy.Client;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class CombatUtil {
   public static final List<Block> blackList;
   public static final List<Block> shulkerList;
   private static Minecraft mc;
   public static final Vec3d[] cityOffsets;
   private static final List<Integer> invalidSlots;

   public static int findCrapple() {
      if (mc.field_71439_g == null) {
         return -1;
      } else {
         for(int x = 0; x < mc.field_71439_g.field_71069_bz.func_75138_a().size(); ++x) {
            if (!invalidSlots.contains(x)) {
               ItemStack stack = (ItemStack)mc.field_71439_g.field_71069_bz.func_75138_a().get(x);
               if (!stack.func_190926_b() && stack.func_77973_b().equals(Items.field_151153_ao) && stack.func_77952_i() != 1) {
                  return x;
               }
            }
         }

         return -1;
      }
   }

   public static int findItemSlotDamage1(Item i) {
      if (mc.field_71439_g == null) {
         return -1;
      } else {
         for(int x = 0; x < mc.field_71439_g.field_71069_bz.func_75138_a().size(); ++x) {
            if (!invalidSlots.contains(x)) {
               ItemStack stack = (ItemStack)mc.field_71439_g.field_71069_bz.func_75138_a().get(x);
               if (!stack.func_190926_b() && stack.func_77973_b().equals(i) && stack.func_77952_i() == 1) {
                  return x;
               }
            }
         }

         return -1;
      }
   }

   public static int findItemSlot(Item i) {
      if (mc.field_71439_g == null) {
         return -1;
      } else {
         for(int x = 0; x < mc.field_71439_g.field_71069_bz.func_75138_a().size(); ++x) {
            if (!invalidSlots.contains(x)) {
               ItemStack stack = (ItemStack)mc.field_71439_g.field_71069_bz.func_75138_a().get(x);
               if (!stack.func_190926_b() && stack.func_77973_b().equals(i)) {
                  return x;
               }
            }
         }

         return -1;
      }
   }

   public static boolean isHoldingCrystal(boolean onlyMainHand) {
      if (onlyMainHand) {
         return mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP;
      } else {
         return mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP || mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
      }
   }

   public static boolean requiredDangerSwitch(double dangerRange) {
      int dangerousCrystals = (int)mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
         return entity instanceof EntityEnderCrystal;
      }).filter((entity) -> {
         return (double)mc.field_71439_g.func_70032_d(entity) <= dangerRange;
      }).filter((entity) -> {
         return calculateDamage(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, mc.field_71439_g) >= mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj();
      }).count();
      return dangerousCrystals > 0;
   }

   public static boolean passesOffhandCheck(double requiredHealth, Item item, boolean isCrapple) {
      double totalPlayerHealth = (double)(mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj());
      if (!isCrapple) {
         if (findItemSlot(item) == -1) {
            return false;
         }
      } else if (findCrapple() == -1) {
         return false;
      }

      return !(totalPlayerHealth < requiredHealth);
   }

   public static void switchOffhandStrict(int targetSlot, int step) {
      switch(step) {
      case 0:
         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, targetSlot, 0, ClickType.PICKUP, mc.field_71439_g);
         break;
      case 1:
         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 45, 0, ClickType.PICKUP, mc.field_71439_g);
         break;
      case 2:
         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, targetSlot, 0, ClickType.PICKUP, mc.field_71439_g);
         mc.field_71442_b.func_78765_e();
      }

   }

   public static void switchOffhandTotemNotStrict() {
      int targetSlot = findItemSlot(Items.field_190929_cY);
      if (targetSlot != -1) {
         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, targetSlot, 0, ClickType.PICKUP, mc.field_71439_g);
         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 45, 0, ClickType.PICKUP, mc.field_71439_g);
         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, targetSlot, 0, ClickType.PICKUP, mc.field_71439_g);
         mc.field_71442_b.func_78765_e();
      }

   }

   public static void switchOffhandNonStrict(int targetSlot) {
      mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, targetSlot, 0, ClickType.PICKUP, mc.field_71439_g);
      mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 45, 0, ClickType.PICKUP, mc.field_71439_g);
      mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, targetSlot, 0, ClickType.PICKUP, mc.field_71439_g);
      mc.field_71442_b.func_78765_e();
   }

   public static boolean canSeeBlock(BlockPos pos) {
      return mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + 1.0F), (double)pos.func_177952_p()), false, true, false) == null;
   }

   public static boolean placeBlock(BlockPos blockPos, boolean offhand, boolean rotate, boolean packetRotate, boolean doSwitch, boolean silentSwitch, int toSwitch) {
      if (!checkCanPlace(blockPos)) {
         return false;
      } else {
         EnumFacing placeSide = getPlaceSide(blockPos);
         BlockPos adjacentBlock = blockPos.func_177972_a(placeSide);
         EnumFacing opposingSide = placeSide.func_176734_d();
         if (!mc.field_71441_e.func_180495_p(adjacentBlock).func_177230_c().func_176209_a(mc.field_71441_e.func_180495_p(adjacentBlock), false)) {
            return false;
         } else {
            if (doSwitch) {
               if (silentSwitch) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(toSwitch));
               } else if (mc.field_71439_g.field_71071_by.field_70461_c != toSwitch) {
                  mc.field_71439_g.field_71071_by.field_70461_c = toSwitch;
               }
            }

            boolean isSneak = false;
            if (blackList.contains(mc.field_71441_e.func_180495_p(adjacentBlock).func_177230_c()) || shulkerList.contains(mc.field_71441_e.func_180495_p(adjacentBlock).func_177230_c())) {
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
               isSneak = true;
            }

            Vec3d hitVector = getHitVector(adjacentBlock, opposingSide);
            if (rotate) {
               float[] angle = getLegitRotations(hitVector);
               mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(angle[0], angle[1], mc.field_71439_g.field_70122_E));
            }

            EnumHand actionHand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, adjacentBlock, opposingSide, hitVector, actionHand);
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketAnimation(actionHand));
            if (isSneak) {
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
            }

            return true;
         }
      }
   }

   private static Vec3d getHitVector(BlockPos pos, EnumFacing opposingSide) {
      return (new Vec3d(pos)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposingSide.func_176730_m())).func_186678_a(0.5D));
   }

   public static Vec3d getHitAddition(double x, double y, double z, BlockPos pos, EnumFacing opposingSide) {
      return (new Vec3d(pos)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposingSide.func_176730_m())).func_186678_a(0.5D));
   }

   public static void betterRotate(BlockPos blockPos, EnumFacing opposite, boolean packetRotate) {
      float offsetZ = 0.0F;
      float offsetY = 0.0F;
      float offsetX = 0.0F;
      switch(getPlaceSide(blockPos)) {
      case UP:
         offsetZ = 0.5F;
         offsetX = 0.5F;
         offsetY = 0.0F;
         break;
      case DOWN:
         offsetZ = 0.5F;
         offsetX = 0.5F;
         offsetY = -0.5F;
         break;
      case NORTH:
         offsetX = 0.5F;
         offsetY = -0.5F;
         offsetZ = -0.5F;
         break;
      case EAST:
         offsetX = 0.5F;
         offsetY = -0.5F;
         offsetZ = 0.5F;
         break;
      case SOUTH:
         offsetX = 0.5F;
         offsetY = -0.5F;
         offsetZ = 0.5F;
         break;
      case WEST:
         offsetX = -0.5F;
         offsetY = -0.5F;
         offsetZ = 0.5F;
      }

      float[] angle = getLegitRotations(getHitAddition((double)offsetX, (double)offsetY, (double)offsetZ, blockPos, opposite));
      mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(angle[0], angle[1], mc.field_71439_g.field_70122_E));
   }

   private static EnumFacing getPlaceSide(BlockPos blockPos) {
      EnumFacing placeableSide = null;
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing side = var2[var4];
         BlockPos adjacent = blockPos.func_177972_a(side);
         if (mc.field_71441_e.func_180495_p(adjacent).func_177230_c().func_176209_a(mc.field_71441_e.func_180495_p(adjacent), false) && !mc.field_71441_e.func_180495_p(adjacent).func_185904_a().func_76222_j()) {
            placeableSide = side;
         }
      }

      return placeableSide;
   }

   public static boolean checkCanPlace(BlockPos pos) {
      if (!(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockAir) && !(mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) {
         return false;
      } else {
         Iterator var1 = mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos)).iterator();

         Entity entity;
         do {
            if (!var1.hasNext()) {
               return getPlaceSide(pos) != null;
            }

            entity = (Entity)var1.next();
         } while(entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow);

         return false;
      }
   }

   public static boolean isInCity(EntityPlayer player, double range, double placeRange, boolean checkFace, boolean topBlock, boolean checkPlace, boolean checkRange) {
      BlockPos pos = new BlockPos(player.func_174791_d());
      EnumFacing[] var10 = EnumFacing.values();
      int var11 = var10.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         EnumFacing face = var10[var12];
         if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            BlockPos pos1 = pos.func_177972_a(face);
            BlockPos pos2 = pos1.func_177972_a(face);
            if (mc.field_71441_e.func_180495_p(pos1).func_177230_c() == Blocks.field_150350_a && (mc.field_71441_e.func_180495_p(pos2).func_177230_c() == Blocks.field_150350_a && isHard(mc.field_71441_e.func_180495_p(pos2.func_177984_a()).func_177230_c()) || !checkFace) && !checkRange || mc.field_71439_g.func_174818_b(pos2) <= placeRange * placeRange && mc.field_71439_g.func_70068_e(player) <= range * range && isHard(mc.field_71441_e.func_180495_p(pos.func_177981_b(3)).func_177230_c()) || !topBlock) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isHard(Block block) {
      return block == Blocks.field_150343_Z || block == Blocks.field_150357_h;
   }

   public static boolean canLegPlace(EntityPlayer player, double range) {
      int safety = 0;
      int blocksInRange = 0;
      Vec3d[] var5 = HoleUtil.cityOffsets;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Vec3d vec = var5[var7];
         BlockPos pos = getFlooredPosition(player).func_177963_a(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
         if (mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150343_Z || mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150357_h) {
            ++safety;
         }

         if (mc.field_71439_g.func_174818_b(pos) >= range * range) {
            ++blocksInRange;
         }
      }

      return safety == 4 && blocksInRange >= 1;
   }

   public static int getSafetyFactor(BlockPos pos) {
      return 0;
   }

   public static boolean canPlaceCrystal(BlockPos pos, double range, double wallsRange, boolean raytraceCheck) {
      BlockPos up = pos.func_177984_a();
      BlockPos up1 = up.func_177984_a();
      AxisAlignedBB bb = (new AxisAlignedBB(up)).func_72321_a(0.0D, 1.0D, 0.0D);
      return (mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150343_Z || mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150357_h) && mc.field_71441_e.func_180495_p(up).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(up1).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_72872_a(Entity.class, bb).isEmpty() && mc.field_71439_g.func_174818_b(pos) <= range * range && !raytraceCheck || rayTraceRangeCheck(pos, wallsRange, 0.0D);
   }

   public static int getVulnerability(EntityPlayer player, double range, double placeRange, double wallsRange, double maxSelfDamage, double maxFriendDamage, double minDamage, double friendRange, double facePlaceHP, int minArmor, boolean cityCheck, boolean rayTrace, boolean lowArmorCheck, boolean antiSuicide, boolean antiFriendPop) {
      if (isInCity(player, range, placeRange, true, true, true, false) && cityCheck) {
         return 5;
      } else if (getClosestValidPos(player, maxSelfDamage, maxFriendDamage, minDamage, placeRange, wallsRange, friendRange, rayTrace, antiSuicide, antiFriendPop, true) != null) {
         return 4;
      } else if ((double)(player.func_110143_aJ() + player.func_110139_bj()) <= facePlaceHP) {
         return 3;
      } else {
         return isArmorLow(player, minArmor, true) && lowArmorCheck ? 2 : 0;
      }
   }

   public static Map<BlockPos, Double> mapBlockDamage(EntityPlayer player, double maxSelfDamage, double maxFriendDamage, double minDamage, double placeRange, double wallsRange, double friendRange, boolean rayTrace, boolean antiSuicide, boolean antiFriendPop) {
      Map<BlockPos, Double> damageMap = new HashMap();
      Iterator var17 = getSphere(new BlockPos(getFlooredPosition(mc.field_71439_g)), (float)placeRange, (int)placeRange, false, true, 0).iterator();

      while(true) {
         BlockPos pos;
         do {
            do {
               do {
                  do {
                     if (!var17.hasNext()) {
                        return damageMap;
                     }

                     pos = (BlockPos)var17.next();
                  } while(!canPlaceCrystal(pos, placeRange, wallsRange, rayTrace));
               } while(!checkFriends(pos, maxFriendDamage, friendRange, antiFriendPop));
            } while(!checkSelf(pos, maxSelfDamage, antiSuicide));
         } while(rayTrace && !rayTraceRangeCheck(pos, wallsRange, 0.0D));

         double damage = (double)calculateDamage((BlockPos)pos, player);
         if (!(damage < minDamage)) {
            damageMap.put(pos, damage);
         }
      }
   }

   public static boolean checkFriends(BlockPos pos, double maxFriendDamage, double friendRange, boolean antiFriendPop) {
      Iterator var6 = mc.field_71441_e.field_73010_i.iterator();

      while(var6.hasNext()) {
         EntityPlayer player = (EntityPlayer)var6.next();
         if (!(mc.field_71439_g.func_70068_e(player) > friendRange * friendRange)) {
            if ((double)calculateDamage((BlockPos)pos, player) > maxFriendDamage) {
               return false;
            }

            if (calculateDamage((BlockPos)pos, player) > player.func_110143_aJ() + player.func_110139_bj() && antiFriendPop) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean checkFriends(EntityEnderCrystal crystal, double maxFriendDamage, double friendRange, boolean antiFriendPop) {
      Iterator var6 = mc.field_71441_e.field_73010_i.iterator();

      while(var6.hasNext()) {
         EntityPlayer player = (EntityPlayer)var6.next();
         if (!(mc.field_71439_g.func_70068_e(player) > friendRange * friendRange)) {
            if ((double)calculateDamage((Entity)crystal, player) > maxFriendDamage) {
               return false;
            }

            if (calculateDamage((Entity)crystal, player) > player.func_110143_aJ() + player.func_110139_bj() && antiFriendPop) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean checkSelf(BlockPos pos, double maxSelfDamage, boolean antiSuicide) {
      boolean willPopSelf = calculateDamage((BlockPos)pos, mc.field_71439_g) > mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj();
      boolean willDamageSelf = (double)calculateDamage((BlockPos)pos, mc.field_71439_g) > maxSelfDamage;
      return (!antiSuicide || !willPopSelf) && !willDamageSelf;
   }

   public static boolean checkSelf(EntityEnderCrystal crystal, double maxSelfDamage, boolean antiSuicide) {
      boolean willPopSelf = calculateDamage((Entity)crystal, mc.field_71439_g) > mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj();
      boolean willDamageSelf = (double)calculateDamage((Entity)crystal, mc.field_71439_g) > maxSelfDamage;
      return (!antiSuicide || !willPopSelf) && !willDamageSelf;
   }

   public static boolean isPosValid(EntityPlayer player, BlockPos pos, double maxSelfDamage, double maxFriendDamage, double minDamage, double placeRange, double wallsRange, double friendRange, boolean rayTrace, boolean antiSuicide, boolean antiFriendPop) {
      if (pos == null) {
         return false;
      } else if (!isHard(mc.field_71441_e.func_180495_p(pos).func_177230_c())) {
         return false;
      } else if (!canPlaceCrystal(pos, placeRange, wallsRange, rayTrace)) {
         return false;
      } else if (!checkFriends(pos, maxFriendDamage, friendRange, antiFriendPop)) {
         return false;
      } else if (!checkSelf(pos, maxSelfDamage, antiSuicide)) {
         return false;
      } else {
         double damage = (double)calculateDamage((BlockPos)pos, player);
         if (damage < minDamage) {
            return false;
         } else {
            return !rayTrace || rayTraceRangeCheck(pos, wallsRange, 0.0D);
         }
      }
   }

   public static BlockPos getClosestValidPos(EntityPlayer player, double maxSelfDamage, double maxFriendDamage, double minDamage, double placeRange, double wallsRange, double friendRange, boolean rayTrace, boolean antiSuicide, boolean antiFriendPop, boolean multiplace) {
      double highestDamage = -1.0D;
      BlockPos finalPos = null;
      if (player == null) {
         return null;
      } else {
         List<BlockPos> placeLocations = getSphere(new BlockPos(getFlooredPosition(mc.field_71439_g)), (float)placeRange, (int)placeRange, false, true, 0);
         placeLocations.sort(Comparator.comparing((blockPos) -> {
            return mc.field_71439_g.func_174818_b(blockPos);
         }));
         Iterator var21 = placeLocations.iterator();

         while(true) {
            BlockPos pos;
            do {
               do {
                  if (!var21.hasNext()) {
                     return finalPos;
                  }

                  pos = (BlockPos)var21.next();
               } while(!canPlaceCrystal(pos, placeRange, wallsRange, rayTrace));
            } while(rayTrace && !rayTraceRangeCheck(pos, wallsRange, 0.0D));

            double damage = (double)calculateDamage((BlockPos)pos, player);
            if (!(damage < minDamage) && checkFriends(pos, maxFriendDamage, friendRange, antiFriendPop) && checkSelf(pos, maxSelfDamage, antiSuicide)) {
               if (damage > 15.0D) {
                  return pos;
               }

               if (damage > highestDamage) {
                  highestDamage = damage;
                  finalPos = pos;
               }
            }
         }
      }
   }

   public static BlockPos getClosestValidPosMultiThread(EntityPlayer player, double maxSelfDamage, double maxFriendDamage, double minDamage, double placeRange, double wallsRange, double friendRange, boolean rayTrace, boolean antiSuicide, boolean antiFriendPop) {
      List<CombatUtil.ValidPosThread> threads = new CopyOnWriteArrayList();
      BlockPos finalPos = null;
      Iterator var18 = getSphere(new BlockPos(player.func_174791_d()), 13.0F, 13, false, true, 0).iterator();

      CombatUtil.ValidPosThread thread;
      while(var18.hasNext()) {
         BlockPos pos = (BlockPos)var18.next();
         thread = new CombatUtil.ValidPosThread(player, pos, maxSelfDamage, maxFriendDamage, minDamage, placeRange, wallsRange, friendRange, rayTrace, antiSuicide, antiFriendPop);
         threads.add(thread);
         thread.start();
      }

      boolean areAllInvalid = false;

      do {
         Iterator var22 = threads.iterator();

         while(var22.hasNext()) {
            thread = (CombatUtil.ValidPosThread)var22.next();
            if (thread.isInterrupted() && thread.isValid) {
               finalPos = thread.pos;
            }
         }

         areAllInvalid = threads.stream().noneMatch((threadx) -> {
            return threadx.isValid && threadx.isInterrupted();
         });
      } while(finalPos == null && !areAllInvalid);

      Client.LOGGER.info(finalPos == null ? "pos was null" : finalPos.toString());
      return finalPos;
   }

   public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
      List<BlockPos> circleblocks = new ArrayList();
      int cx = pos.func_177958_n();
      int cy = pos.func_177956_o();
      int cz = pos.func_177952_p();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            for(int y = sphere ? cy - (int)r : cy; (float)y < (sphere ? (float)cy + r : (float)(cy + h)); ++y) {
               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < (double)(r * r) && (!hollow || !(dist < (double)((r - 1.0F) * (r - 1.0F))))) {
                  BlockPos l = new BlockPos(x, y + plus_y, z);
                  circleblocks.add(l);
               }
            }
         }
      }

      return circleblocks;
   }

   public static boolean isArmorLow(EntityPlayer player, int durability, boolean checkDurability) {
      Iterator var3 = player.field_71071_by.field_70460_b.iterator();

      ItemStack piece;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         piece = (ItemStack)var3.next();
         if (piece == null) {
            return true;
         }
      } while(!checkDurability || getItemDamage(piece) >= durability);

      return true;
   }

   public static int getItemDamage(ItemStack stack) {
      return stack.func_77958_k() - stack.func_77952_i();
   }

   public static boolean rayTraceRangeCheck(Entity target, double range) {
      boolean isVisible = mc.field_71439_g.func_70685_l(target);
      return !isVisible || mc.field_71439_g.func_70068_e(target) <= range * range;
   }

   public static boolean rayTraceRangeCheck(BlockPos pos, double range, double height) {
      RayTraceResult result = mc.field_71441_e.func_147447_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)pos.func_177956_o() + height, (double)pos.func_177952_p()), false, true, false);
      return result == null || mc.field_71439_g.func_174818_b(pos) <= range * range;
   }

   public static EntityEnderCrystal getClosestValidCrystal(EntityPlayer player, double maxSelfDamage, double maxFriendDamage, double minDamage, double breakRange, double wallsRange, double friendRange, boolean rayTrace, boolean antiSuicide, boolean antiFriendPop) {
      if (player == null) {
         return null;
      } else {
         List<EntityEnderCrystal> crystals = (List)mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
            return entity instanceof EntityEnderCrystal;
         }).filter((entity) -> {
            return mc.field_71439_g.func_70068_e(entity) <= breakRange * breakRange;
         }).sorted(Comparator.comparingDouble((entity) -> {
            return mc.field_71439_g.func_70068_e(entity);
         })).map((entity) -> {
            return (EntityEnderCrystal)entity;
         }).collect(Collectors.toList());
         Iterator var17 = crystals.iterator();

         EntityEnderCrystal crystal;
         do {
            do {
               if (!var17.hasNext()) {
                  return null;
               }

               crystal = (EntityEnderCrystal)var17.next();
            } while(rayTrace && !rayTraceRangeCheck(crystal, wallsRange));
         } while((double)calculateDamage((Entity)crystal, player) < minDamage || !checkSelf(crystal, maxSelfDamage, antiSuicide) || !checkFriends(crystal, maxFriendDamage, friendRange, antiFriendPop));

         return crystal;
      }
   }

   public static List<BlockPos> getDisc(BlockPos pos, float r) {
      List<BlockPos> circleblocks = new ArrayList();
      int cx = pos.func_177958_n();
      int cy = pos.func_177956_o();
      int cz = pos.func_177952_p();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z));
            if (dist < (double)(r * r)) {
               BlockPos position = new BlockPos(x, cy, z);
               circleblocks.add(position);
            }
         }
      }

      return circleblocks;
   }

   public static BlockPos getFlooredPosition(Entity entity) {
      return new BlockPos(Math.floor(entity.field_70165_t), Math.floor(entity.field_70163_u), Math.floor(entity.field_70161_v));
   }

   public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
      float doubleExplosionSize = 12.0F;
      double distancedsize = entity.func_70011_f(posX, posY, posZ) / (double)doubleExplosionSize;
      Vec3d vec3d = new Vec3d(posX, posY, posZ);
      double blockDensity = 0.0D;

      try {
         blockDensity = (double)entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
      } catch (Exception var18) {
      }

      double v = (1.0D - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0D * 7.0D * (double)doubleExplosionSize + 1.0D));
      double finald = 1.0D;
      if (entity instanceof EntityLivingBase) {
         finald = (double)getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion(Minecraft.func_71410_x().field_71441_e, (Entity)null, posX, posY, posZ, 6.0F, false, true));
      }

      return (float)finald;
   }

   public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
      float damage;
      if (entity instanceof EntityPlayer) {
         EntityPlayer ep = (EntityPlayer)entity;
         DamageSource ds = DamageSource.func_94539_a(explosion);
         damage = CombatRules.func_189427_a(damageI, (float)ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
         int k = 0;

         try {
            k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
         } catch (Exception var8) {
         }

         float f = MathHelper.func_76131_a((float)k, 0.0F, 20.0F);
         damage *= 1.0F - f / 25.0F;
         if (entity.func_70644_a(MobEffects.field_76429_m)) {
            damage -= damage / 4.0F;
         }

         damage = Math.max(damage, 0.0F);
         return damage;
      } else {
         damage = CombatRules.func_189427_a(damageI, (float)entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
         return damage;
      }
   }

   public static float getDamageMultiplied(float damage) {
      int diff = Minecraft.func_71410_x().field_71441_e.func_175659_aa().func_151525_a();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }

   public static float calculateDamage(Entity crystal, Entity entity) {
      return calculateDamage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
   }

   public static float calculateDamage(BlockPos pos, Entity entity) {
      return calculateDamage((double)pos.func_177958_n() + 0.5D, (double)(pos.func_177956_o() + 1), (double)pos.func_177952_p() + 0.5D, entity);
   }

   public static Vec3d interpolateEntity(Entity entity) {
      return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)mc.func_184121_ak(), entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)mc.func_184121_ak(), entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)mc.func_184121_ak());
   }

   public static float[] calcAngle(Vec3d from, Vec3d to) {
      double difX = to.field_72450_a - from.field_72450_a;
      double difY = (to.field_72448_b - from.field_72448_b) * -1.0D;
      double difZ = to.field_72449_c - from.field_72449_c;
      double dist = (double)MathHelper.func_76133_a(difX * difX + difZ * difZ);
      return new float[]{(float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difY, dist)))};
   }

   public static float[] getLegitRotations(Vec3d vec) {
      Vec3d eyesPos = new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
      double diffX = vec.field_72450_a - eyesPos.field_72450_a;
      double diffY = vec.field_72448_b - eyesPos.field_72448_b;
      double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z), mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - mc.field_71439_g.field_70125_A)};
   }

   static {
      blackList = Arrays.asList(Blocks.field_150329_H, Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT);
      shulkerList = Arrays.asList(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA);
      mc = Minecraft.func_71410_x();
      cityOffsets = new Vec3d[]{new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(2.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 2.0D), new Vec3d(-2.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -2.0D)};
      invalidSlots = Arrays.asList(0, 5, 6, 7, 8);
   }

   public static class CombatPosInfo {
      public EntityPlayer player;
      public BlockPos pos;
      public float damage;

      public CombatPosInfo(EntityPlayer player, BlockPos pos, float damage) {
         this.pos = pos;
         this.damage = damage;
         this.player = player;
      }
   }

   public static class ValidPosThread extends Thread {
      BlockPos pos;
      EntityPlayer player;
      double maxSelfDamage;
      double maxFriendDamage;
      double minDamage;
      double placeRange;
      double wallsRange;
      double friendRange;
      boolean rayTrace;
      boolean antiSuicide;
      boolean antiFriendPop;
      public float damage;
      public boolean isValid;
      public CombatUtil.CombatPosInfo info;

      public ValidPosThread(EntityPlayer player, BlockPos pos, double maxSelfDamage, double maxFriendDamage, double minDamage, double placeRange, double wallsRange, double friendRange, boolean rayTrace, boolean antiSuicide, boolean antiFriendPop) {
         super("Break");
         this.pos = pos;
         this.maxSelfDamage = maxSelfDamage;
         this.maxFriendDamage = maxFriendDamage;
         this.minDamage = minDamage;
         this.placeRange = placeRange;
         this.wallsRange = wallsRange;
         this.friendRange = friendRange;
         this.rayTrace = rayTrace;
         this.antiSuicide = antiSuicide;
         this.antiFriendPop = antiFriendPop;
         this.player = player;
      }

      public void run() {
         if (!(CombatUtil.mc.field_71439_g.func_174818_b(this.pos) > this.placeRange * this.placeRange) && CombatUtil.canPlaceCrystal(this.pos, this.placeRange, this.wallsRange, this.rayTrace) && CombatUtil.checkFriends(this.pos, this.maxFriendDamage, this.friendRange, this.antiFriendPop) && CombatUtil.checkSelf(this.pos, this.maxSelfDamage, this.antiSuicide)) {
            this.damage = CombatUtil.calculateDamage((BlockPos)this.pos, this.player);
            if ((double)this.damage >= this.minDamage && (!this.rayTrace || CombatUtil.rayTraceRangeCheck(this.pos, this.wallsRange, 0.0D))) {
               this.isValid = true;
               this.info = new CombatUtil.CombatPosInfo(this.player, this.pos, this.damage);
               Client.LOGGER.info("Pos was valid.");
               return;
            }
         }

         this.isValid = false;
         this.info = new CombatUtil.CombatPosInfo(this.player, this.pos, -1.0F);
         Client.LOGGER.info("Pos was invalid.");
      }
   }
}
