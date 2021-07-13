package me.dev.legacy.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import java.util.Map;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.BlockUtil;
import me.dev.legacy.util.BlockUtilll;
import me.dev.legacy.util.ItemUtil;
import me.dev.legacy.util.Timer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoFeetplace extends Module {
   private final Setting<Integer> delay = this.register(new Setting("Delay", 50, 0, 250));
   private final Setting<Integer> blocksPerTick = this.register(new Setting("BPT", 8, 1, 20));
   private final Setting<Boolean> helpingBlocks = this.register(new Setting("HelpingBlocks", true));
   private final Setting<Boolean> intelligent = this.register(new Setting("Intelligent", false));
   private final Setting<Boolean> antiPedo = this.register(new Setting("Always Help", false));
   private final Setting<Boolean> floor = this.register(new Setting("Floor", false));
   private final Setting<Integer> retryer = this.register(new Setting("Retries", 4, 1, 15));
   private final Setting<Integer> retryDelay = this.register(new Setting("Retry Delay", 200, 1, 2500));
   private final Setting<Boolean> existCheck = this.register(new Setting("Exist", false));
   private final Setting<Integer> existed = this.register(new Setting("Existed", 4, 1, 15));
   private final Map<BlockPos, Integer> retries = new HashMap();
   private final Timer timer = new Timer();
   private final Timer retryTimer = new Timer();
   private boolean didPlace = false;
   private int placements = 0;
   private int obbySlot = -1;
   double posY;

   public AutoFeetplace() {
      super("AutoFeetplace", "Surrounds you with obsidian", Module.Category.COMBAT, true, false, false);
   }

   public void onEnable() {
      if (fullNullCheck()) {
         this.setEnabled(false);
      } else {
         this.retries.clear();
         this.retryTimer.reset();
         this.posY = mc.field_71439_g.field_70163_u;
      }
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (!this.check()) {
         if (this.posY < mc.field_71439_g.field_70163_u) {
            this.setEnabled(false);
         } else {
            boolean onEChest = mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB;
            if (mc.field_71439_g.field_70163_u - (double)((int)mc.field_71439_g.field_70163_u) < 0.7D) {
               onEChest = false;
            }

            if (!BlockUtil.isSafe(mc.field_71439_g, onEChest ? 1 : 0, (Boolean)this.floor.getValue())) {
               this.placeBlocks(mc.field_71439_g.func_174791_d(), BlockUtil.getUnsafeBlockArray(mc.field_71439_g, onEChest ? 1 : 0, (Boolean)this.floor.getValue()), (Boolean)this.helpingBlocks.getValue(), false);
            } else if (!BlockUtil.isSafe(mc.field_71439_g, onEChest ? 0 : -1, false) && (Boolean)this.antiPedo.getValue()) {
               this.placeBlocks(mc.field_71439_g.func_174791_d(), BlockUtil.getUnsafeBlockArray(mc.field_71439_g, onEChest ? 0 : -1, false), false, false);
            }

            if (this.didPlace) {
               this.timer.reset();
            }

         }
      }
   }

   private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping) {
      int helpings = 0;
      if (this.obbySlot == -1) {
         return false;
      } else if (mc.field_71439_g == null) {
         return false;
      } else {
         int lastSlot = mc.field_71439_g.field_71071_by.field_70461_c;
         mc.func_147114_u().func_147297_a(new CPacketHeldItemChange(this.obbySlot));
         Vec3d[] var8 = vec3ds;
         int var9 = vec3ds.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Vec3d vec3d = var8[var10];
            boolean gotHelp = true;
            ++helpings;
            if (isHelping && !(Boolean)this.intelligent.getValue() && helpings > 1) {
               return false;
            }

            BlockPos position = (new BlockPos(pos)).func_177963_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
            switch(BlockUtil.isPositionPlaceable(position, true)) {
            case -1:
            case 0:
            case 1:
            default:
               break;
            case 2:
               if (!hasHelpingBlocks) {
                  break;
               }

               gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true);
            case 3:
               if (gotHelp) {
                  this.placeBlock(position);
               }

               if (isHelping) {
                  return true;
               }
            }
         }

         mc.func_147114_u().func_147297_a(new CPacketHeldItemChange(lastSlot));
         return false;
      }
   }

   private boolean check() {
      if (fullNullCheck()) {
         return true;
      } else {
         this.didPlace = false;
         this.placements = 0;
         this.obbySlot = ItemUtil.getBlockFromHotbar(Blocks.field_150343_Z);
         if (this.retryTimer.passed((long)(Integer)this.retryDelay.getValue())) {
            this.retries.clear();
            this.retryTimer.reset();
         }

         if (this.obbySlot == -1) {
            this.obbySlot = ItemUtil.getBlockFromHotbar(Blocks.field_150477_bB);
            if (this.obbySlot == -1) {
               Command.sendMessage(ChatFormatting.RED + "<AutoFeetPlace> No obsidian.");
               this.setEnabled(false);
               return true;
            }
         }

         return !this.timer.passed((long)(Integer)this.delay.getValue());
      }
   }

   private void placeBlock(BlockPos pos) {
      if (this.placements < (Integer)this.blocksPerTick.getValue()) {
         BlockUtilll.placeBlock(pos);
         this.didPlace = true;
         ++this.placements;
      }

   }
}
