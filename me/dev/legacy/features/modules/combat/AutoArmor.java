package me.dev.legacy.features.modules.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.dev.legacy.Client;
import me.dev.legacy.features.gui.OyVeyGui;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.modules.player.XCarry;
import me.dev.legacy.features.setting.Bind;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.DamageUtil;
import me.dev.legacy.util.EntityUtil;
import me.dev.legacy.util.InventoryUtil;
import me.dev.legacy.util.MathUtil;
import me.dev.legacy.util.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public class AutoArmor extends Module {
   private final Setting<Integer> delay = this.register(new Setting("Delay", 50, 0, 500));
   private final Setting<Boolean> mendingTakeOff = this.register(new Setting("AutoMend", false));
   private final Setting<Integer> closestEnemy = this.register(new Setting("Enemy", 8, 1, 20, (v) -> {
      return (Boolean)this.mendingTakeOff.getValue();
   }));
   private final Setting<Integer> helmetThreshold = this.register(new Setting("Helmet%", 80, 1, 100, (v) -> {
      return (Boolean)this.mendingTakeOff.getValue();
   }));
   private final Setting<Integer> chestThreshold = this.register(new Setting("Chest%", 80, 1, 100, (v) -> {
      return (Boolean)this.mendingTakeOff.getValue();
   }));
   private final Setting<Integer> legThreshold = this.register(new Setting("Legs%", 80, 1, 100, (v) -> {
      return (Boolean)this.mendingTakeOff.getValue();
   }));
   private final Setting<Integer> bootsThreshold = this.register(new Setting("Boots%", 80, 1, 100, (v) -> {
      return (Boolean)this.mendingTakeOff.getValue();
   }));
   private final Setting<Boolean> curse = this.register(new Setting("CurseOfBinding", false));
   private final Setting<Integer> actions = this.register(new Setting("Actions", 3, 1, 12));
   private final Setting<Bind> elytraBind = this.register(new Setting("Elytra", new Bind(-1)));
   private final Setting<Boolean> tps = this.register(new Setting("TpsSync", true));
   private final Setting<Boolean> updateController = this.register(new Setting("Update", true));
   private final Setting<Boolean> shiftClick = this.register(new Setting("ShiftClick", false));
   private final Timer timer = new Timer();
   private final Timer elytraTimer = new Timer();
   private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue();
   private final List<Integer> doneSlots = new ArrayList();
   private boolean elytraOn = false;

   public AutoArmor() {
      super("AutoArmor", "Puts Armor on for you.", Module.Category.COMBAT, true, false, false);
   }

   @SubscribeEvent
   public void onKeyInput(KeyInputEvent event) {
      if (Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof OyVeyGui) && ((Bind)this.elytraBind.getValue()).getKey() == Keyboard.getEventKey()) {
         this.elytraOn = !this.elytraOn;
      }

   }

   public void onLogin() {
      this.timer.reset();
      this.elytraTimer.reset();
   }

   public void onDisable() {
      this.taskList.clear();
      this.doneSlots.clear();
      this.elytraOn = false;
   }

   public void onLogout() {
      this.taskList.clear();
      this.doneSlots.clear();
   }

   public void onTick() {
      if (!fullNullCheck() && (!(mc.field_71462_r instanceof GuiContainer) || mc.field_71462_r instanceof GuiInventory)) {
         int slot;
         if (this.taskList.isEmpty()) {
            if ((Boolean)this.mendingTakeOff.getValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && mc.field_71474_y.field_74313_G.func_151470_d() && (this.isSafe() || EntityUtil.isSafe(mc.field_71439_g, 1, false, true))) {
               ItemStack helm = mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
               if (!helm.field_190928_g && DamageUtil.getRoundedDamage(helm) >= (Integer)this.helmetThreshold.getValue()) {
                  this.takeOffSlot(5);
               }

               ItemStack chest2 = mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c();
               if (!chest2.field_190928_g && DamageUtil.getRoundedDamage(chest2) >= (Integer)this.chestThreshold.getValue()) {
                  this.takeOffSlot(6);
               }

               ItemStack legging2 = mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c();
               if (!legging2.field_190928_g && DamageUtil.getRoundedDamage(legging2) >= (Integer)this.legThreshold.getValue()) {
                  this.takeOffSlot(7);
               }

               ItemStack feet2 = mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c();
               if (!feet2.field_190928_g && DamageUtil.getRoundedDamage(feet2) >= (Integer)this.bootsThreshold.getValue()) {
                  this.takeOffSlot(8);
               }

               return;
            }

            ItemStack helm = mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
            int slot4;
            if (helm.func_77973_b() == Items.field_190931_a && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, (Boolean)this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
               this.getSlotOn(5, slot4);
            }

            int slot3;
            ItemStack chest;
            if ((chest = mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c()).func_77973_b() == Items.field_190931_a) {
               if (this.taskList.isEmpty()) {
                  if (this.elytraOn && this.elytraTimer.passedMs(500L)) {
                     int elytraSlot = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false, XCarry.getInstance().isOn());
                     if (elytraSlot != -1) {
                        if ((elytraSlot >= 5 || elytraSlot <= 1) && (Boolean)this.shiftClick.getValue()) {
                           this.taskList.add(new InventoryUtil.Task(elytraSlot, true));
                        } else {
                           this.taskList.add(new InventoryUtil.Task(elytraSlot));
                           this.taskList.add(new InventoryUtil.Task(6));
                        }

                        if ((Boolean)this.updateController.getValue()) {
                           this.taskList.add(new InventoryUtil.Task());
                        }

                        this.elytraTimer.reset();
                     }
                  } else if (!this.elytraOn && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, (Boolean)this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                     this.getSlotOn(6, slot3);
                  }
               }
            } else if (this.elytraOn && chest.func_77973_b() != Items.field_185160_cR && this.elytraTimer.passedMs(500L)) {
               if (this.taskList.isEmpty()) {
                  slot3 = InventoryUtil.findItemInventorySlot(Items.field_185160_cR, false, XCarry.getInstance().isOn());
                  if (slot3 != -1) {
                     this.taskList.add(new InventoryUtil.Task(slot3));
                     this.taskList.add(new InventoryUtil.Task(6));
                     this.taskList.add(new InventoryUtil.Task(slot3));
                     if ((Boolean)this.updateController.getValue()) {
                        this.taskList.add(new InventoryUtil.Task());
                     }
                  }

                  this.elytraTimer.reset();
               }
            } else if (!this.elytraOn && chest.func_77973_b() == Items.field_185160_cR && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
               slot3 = InventoryUtil.findItemInventorySlot(Items.field_151163_ad, false, XCarry.getInstance().isOn());
               if (slot3 == -1 && (slot3 = InventoryUtil.findItemInventorySlot(Items.field_151030_Z, false, XCarry.getInstance().isOn())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot(Items.field_151171_ah, false, XCarry.getInstance().isOn())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot(Items.field_151023_V, false, XCarry.getInstance().isOn())) == -1) {
                  slot3 = InventoryUtil.findItemInventorySlot(Items.field_151027_R, false, XCarry.getInstance().isOn());
               }

               if (slot3 != -1) {
                  this.taskList.add(new InventoryUtil.Task(slot3));
                  this.taskList.add(new InventoryUtil.Task(6));
                  this.taskList.add(new InventoryUtil.Task(slot3));
                  if ((Boolean)this.updateController.getValue()) {
                     this.taskList.add(new InventoryUtil.Task());
                  }
               }

               this.elytraTimer.reset();
            }

            int slot2;
            if (mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c().func_77973_b() == Items.field_190931_a && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, (Boolean)this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
               this.getSlotOn(7, slot2);
            }

            if (mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c().func_77973_b() == Items.field_190931_a && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, (Boolean)this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
               this.getSlotOn(8, slot);
            }
         }

         if (this.timer.passedMs((long)((int)((float)(Integer)this.delay.getValue() * ((Boolean)this.tps.getValue() ? Client.serverManager.getTpsFactor() : 1.0F))))) {
            if (!this.taskList.isEmpty()) {
               for(slot = 0; slot < (Integer)this.actions.getValue(); ++slot) {
                  InventoryUtil.Task task = (InventoryUtil.Task)this.taskList.poll();
                  if (task != null) {
                     task.run();
                  }
               }
            }

            this.timer.reset();
         }

      }
   }

   public String getDisplayInfo() {
      return this.elytraOn ? "Elytra" : null;
   }

   private void takeOffSlot(int slot) {
      if (this.taskList.isEmpty()) {
         int target = -1;
         Iterator var3 = InventoryUtil.findEmptySlots(XCarry.getInstance().isOn()).iterator();

         while(var3.hasNext()) {
            int i = (Integer)var3.next();
            if (!this.doneSlots.contains(target)) {
               target = i;
               this.doneSlots.add(i);
            }
         }

         if (target != -1) {
            if ((target >= 5 || target <= 0) && (Boolean)this.shiftClick.getValue()) {
               this.taskList.add(new InventoryUtil.Task(slot, true));
            } else {
               this.taskList.add(new InventoryUtil.Task(slot));
               this.taskList.add(new InventoryUtil.Task(target));
            }

            if ((Boolean)this.updateController.getValue()) {
               this.taskList.add(new InventoryUtil.Task());
            }
         }
      }

   }

   private void getSlotOn(int slot, int target) {
      if (this.taskList.isEmpty()) {
         this.doneSlots.remove(target);
         if ((target >= 5 || target <= 0) && (Boolean)this.shiftClick.getValue()) {
            this.taskList.add(new InventoryUtil.Task(target, true));
         } else {
            this.taskList.add(new InventoryUtil.Task(target));
            this.taskList.add(new InventoryUtil.Task(slot));
         }

         if ((Boolean)this.updateController.getValue()) {
            this.taskList.add(new InventoryUtil.Task());
         }
      }

   }

   private boolean isSafe() {
      EntityPlayer closest = EntityUtil.getClosestEnemy((double)(Integer)this.closestEnemy.getValue());
      if (closest == null) {
         return true;
      } else {
         return mc.field_71439_g.func_70068_e(closest) >= MathUtil.square((double)(Integer)this.closestEnemy.getValue());
      }
   }
}
