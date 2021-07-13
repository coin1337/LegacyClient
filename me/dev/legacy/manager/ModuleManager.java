package me.dev.legacy.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.dev.legacy.Client;
import me.dev.legacy.event.events.Render2DEvent;
import me.dev.legacy.event.events.Render3DEvent;
import me.dev.legacy.features.Feature;
import me.dev.legacy.features.gui.OyVeyGui;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.modules.client.ClickGui;
import me.dev.legacy.features.modules.client.FontMod;
import me.dev.legacy.features.modules.client.HUD;
import me.dev.legacy.features.modules.client.HudComponents;
import me.dev.legacy.features.modules.client.MediaModule;
import me.dev.legacy.features.modules.combat.AntiRegear;
import me.dev.legacy.features.modules.combat.Aura;
import me.dev.legacy.features.modules.combat.AutoArmor;
import me.dev.legacy.features.modules.combat.AutoCrystal;
import me.dev.legacy.features.modules.combat.AutoFeetplace;
import me.dev.legacy.features.modules.combat.AutoLog;
import me.dev.legacy.features.modules.combat.AutoTrap;
import me.dev.legacy.features.modules.combat.AutoWeb;
import me.dev.legacy.features.modules.combat.BedAura;
import me.dev.legacy.features.modules.combat.Burrow;
import me.dev.legacy.features.modules.combat.Crits;
import me.dev.legacy.features.modules.combat.HoleFiller;
import me.dev.legacy.features.modules.combat.MinDmg;
import me.dev.legacy.features.modules.combat.Offhand;
import me.dev.legacy.features.modules.combat.Quiver;
import me.dev.legacy.features.modules.combat.SelfAnvil;
import me.dev.legacy.features.modules.combat.SelfWeb;
import me.dev.legacy.features.modules.misc.BuildHeight;
import me.dev.legacy.features.modules.misc.ChatSuffix;
import me.dev.legacy.features.modules.misc.Dupe;
import me.dev.legacy.features.modules.misc.GhastNotifier;
import me.dev.legacy.features.modules.misc.MCF;
import me.dev.legacy.features.modules.misc.PopCounter;
import me.dev.legacy.features.modules.misc.RPC;
import me.dev.legacy.features.modules.misc.Timestamps;
import me.dev.legacy.features.modules.misc.ToolTips;
import me.dev.legacy.features.modules.misc.Tracker;
import me.dev.legacy.features.modules.movement.AntiVoid;
import me.dev.legacy.features.modules.movement.FastDrop;
import me.dev.legacy.features.modules.movement.NoFall;
import me.dev.legacy.features.modules.movement.NoSlowDown;
import me.dev.legacy.features.modules.movement.ReverseStep;
import me.dev.legacy.features.modules.movement.Speed;
import me.dev.legacy.features.modules.movement.Step;
import me.dev.legacy.features.modules.movement.Velocity;
import me.dev.legacy.features.modules.player.FakeKick;
import me.dev.legacy.features.modules.player.FakePlayer;
import me.dev.legacy.features.modules.player.FastPlace;
import me.dev.legacy.features.modules.player.Freecam;
import me.dev.legacy.features.modules.player.LiquidInteract;
import me.dev.legacy.features.modules.player.MCP;
import me.dev.legacy.features.modules.player.MultiTask;
import me.dev.legacy.features.modules.player.Reach;
import me.dev.legacy.features.modules.player.Replenish;
import me.dev.legacy.features.modules.player.Speedmine;
import me.dev.legacy.features.modules.player.TpsSync;
import me.dev.legacy.features.modules.player.XCarry;
import me.dev.legacy.features.modules.render.BlockHighlight;
import me.dev.legacy.features.modules.render.ESP;
import me.dev.legacy.features.modules.render.Fullbright;
import me.dev.legacy.features.modules.render.NoFog;
import me.dev.legacy.features.modules.render.NoRender;
import me.dev.legacy.features.modules.render.SkyColor;
import me.dev.legacy.features.modules.render.Swing;
import me.dev.legacy.features.modules.render.Trajectories;
import me.dev.legacy.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager extends Feature {
   public ArrayList<Module> modules = new ArrayList();
   public List<Module> sortedModules = new ArrayList();
   public List<String> sortedModulesABC = new ArrayList();
   public ModuleManager.Animation animationThread;
   public static ArrayList<Module> nigger;

   public void init() {
      this.modules.add(new ClickGui());
      this.modules.add(new FontMod());
      this.modules.add(new HUD());
      this.modules.add(new HudComponents());
      this.modules.add(new MediaModule());
      this.modules.add(new BlockHighlight());
      this.modules.add(new Trajectories());
      this.modules.add(new NoFog());
      this.modules.add(new SkyColor());
      this.modules.add(new ESP());
      this.modules.add(new NoRender());
      this.modules.add(new Fullbright());
      this.modules.add(new BedAura());
      this.modules.add(new Crits());
      this.modules.add(new SelfAnvil());
      this.modules.add(new Offhand());
      this.modules.add(new AutoWeb());
      this.modules.add(new HoleFiller());
      this.modules.add(new AutoArmor());
      this.modules.add(new Burrow());
      this.modules.add(new AutoCrystal());
      this.modules.add(new AutoLog());
      this.modules.add(new MinDmg());
      this.modules.add(new Quiver());
      this.modules.add(new Aura());
      this.modules.add(new AutoTrap());
      this.modules.add(new SelfWeb());
      this.modules.add(new AntiRegear());
      this.modules.add(new AutoFeetplace());
      this.modules.add(new FakeKick());
      this.modules.add(new Freecam());
      this.modules.add(new FastPlace());
      this.modules.add(new Replenish());
      this.modules.add(new FakePlayer());
      this.modules.add(new MCP());
      this.modules.add(new LiquidInteract());
      this.modules.add(new TpsSync());
      this.modules.add(new MultiTask());
      this.modules.add(new Reach());
      this.modules.add(new Speedmine());
      this.modules.add(new Timestamps());
      this.modules.add(new ChatSuffix());
      this.modules.add(new BuildHeight());
      this.modules.add(new MCF());
      this.modules.add(new ToolTips());
      this.modules.add(new RPC());
      this.modules.add(new Tracker());
      this.modules.add(new PopCounter());
      this.modules.add(new XCarry());
      this.modules.add(new GhastNotifier());
      this.modules.add(new Swing());
      this.modules.add(new Dupe());
      this.modules.add(new ReverseStep());
      this.modules.add(new AntiVoid());
      this.modules.add(new Step());
      this.modules.add(new NoSlowDown());
      this.modules.add(new NoFall());
      this.modules.add(new Speed());
      this.modules.add(new FastDrop());
      this.modules.add(new Velocity());
   }

   public Module getModuleByName(String name) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(!module.getName().equalsIgnoreCase(name));

      return module;
   }

   public <T extends Module> T getModuleByClass(Class<T> clazz) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(!clazz.isInstance(module));

      return module;
   }

   public void enableModule(Class<Module> clazz) {
      Module module = this.getModuleByClass(clazz);
      if (module != null) {
         module.enable();
      }

   }

   public void disableModule(Class<Module> clazz) {
      Module module = this.getModuleByClass(clazz);
      if (module != null) {
         module.disable();
      }

   }

   public void enableModule(String name) {
      Module module = this.getModuleByName(name);
      if (module != null) {
         module.enable();
      }

   }

   public void disableModule(String name) {
      Module module = this.getModuleByName(name);
      if (module != null) {
         module.disable();
      }

   }

   public boolean isModuleEnabled(String name) {
      Module module = this.getModuleByName(name);
      return module != null && module.isOn();
   }

   public boolean isModuleEnabled(Class<Module> clazz) {
      Module module = this.getModuleByClass(clazz);
      return module != null && module.isOn();
   }

   public Module getModuleByDisplayName(String displayName) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(!module.getDisplayName().equalsIgnoreCase(displayName));

      return module;
   }

   public ArrayList<Module> getEnabledModules() {
      ArrayList<Module> enabledModules = new ArrayList();
      Iterator var2 = this.modules.iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();
         if (module.isEnabled()) {
            enabledModules.add(module);
         }
      }

      return enabledModules;
   }

   public ArrayList<String> getEnabledModulesName() {
      ArrayList<String> enabledModules = new ArrayList();
      Iterator var2 = this.modules.iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();
         if (module.isEnabled() && module.isDrawn()) {
            enabledModules.add(module.getFullArrayString());
         }
      }

      return enabledModules;
   }

   public ArrayList<Module> getModulesByCategory(Module.Category category) {
      ArrayList<Module> modulesCategory = new ArrayList();
      this.modules.forEach((module) -> {
         if (module.getCategory() == category) {
            modulesCategory.add(module);
         }

      });
      return modulesCategory;
   }

   public List<Module.Category> getCategories() {
      return Arrays.asList(Module.Category.values());
   }

   public void onLoad() {
      Stream var10000 = this.modules.stream().filter(Module::listening);
      EventBus var10001 = MinecraftForge.EVENT_BUS;
      var10000.forEach(var10001::register);
      this.modules.forEach(Module::onLoad);
   }

   public void onUpdate() {
      this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
   }

   public void onTick() {
      this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
   }

   public void onRender2D(Render2DEvent event) {
      this.modules.stream().filter(Feature::isEnabled).forEach((module) -> {
         module.onRender2D(event);
      });
   }

   public void onRender3D(Render3DEvent event) {
      this.modules.stream().filter(Feature::isEnabled).forEach((module) -> {
         module.onRender3D(event);
      });
   }

   public <T extends Module> T getModuleT(Class<T> clazz) {
      return (Module)this.modules.stream().filter((module) -> {
         return module.getClass() == clazz;
      }).map((module) -> {
         return module;
      }).findFirst().orElse((Object)null);
   }

   public void sortModules(boolean reverse) {
      this.sortedModules = (List)this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing((module) -> {
         return this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1);
      })).collect(Collectors.toList());
   }

   public void sortModulesABC() {
      this.sortedModulesABC = new ArrayList(this.getEnabledModulesName());
      this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
   }

   public void onLogout() {
      this.modules.forEach(Module::onLogout);
   }

   public void onLogin() {
      this.modules.forEach(Module::onLogin);
   }

   public void onUnload() {
      EventBus var10001 = MinecraftForge.EVENT_BUS;
      this.modules.forEach(var10001::unregister);
      this.modules.forEach(Module::onUnload);
   }

   public void onUnloadPost() {
      Iterator var1 = this.modules.iterator();

      while(var1.hasNext()) {
         Module module = (Module)var1.next();
         module.enabled.setValue(false);
      }

   }

   public void onKeyPressed(int eventKey) {
      if (eventKey != 0 && Keyboard.getEventKeyState() && !(mc.field_71462_r instanceof OyVeyGui)) {
         this.modules.forEach((module) -> {
            if (module.getBind().getKey() == eventKey) {
               module.toggle();
            }

         });
      }
   }

   public static ArrayList<Module> getModules() {
      return nigger;
   }

   public static boolean isModuleEnablednigger(String name) {
      Module modulenigger = (Module)getModules().stream().filter((mm) -> {
         return mm.getName().equalsIgnoreCase(name);
      }).findFirst().orElse((Object)null);
      return modulenigger.isEnabled();
   }

   public static boolean isModuleEnablednigger(Module modulenigger) {
      return modulenigger.isEnabled();
   }

   private class Animation extends Thread {
      public Module module;
      public float offset;
      public float vOffset;
      ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

      public Animation() {
         super("Animation");
      }

      public void run() {
         Iterator var1;
         if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
            var1 = ModuleManager.this.sortedModules.iterator();

            while(true) {
               while(var1.hasNext()) {
                  Module modulex = (Module)var1.next();
                  String text = modulex.getDisplayName() + ChatFormatting.GRAY + (modulex.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + modulex.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                  modulex.offset = (float)ModuleManager.this.renderer.getStringWidth(text) / ((Integer)HUD.getInstance().animationHorizontalTime.getValue()).floatValue();
                  modulex.vOffset = (float)ModuleManager.this.renderer.getFontHeight() / ((Integer)HUD.getInstance().animationVerticalTime.getValue()).floatValue();
                  if (modulex.isEnabled() && (Integer)HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                     if (modulex.arrayListOffset > modulex.offset && Util.mc.field_71441_e != null) {
                        modulex.arrayListOffset -= modulex.offset;
                        modulex.sliding = true;
                     }
                  } else if (modulex.isDisabled() && (Integer)HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                     if (modulex.arrayListOffset < (float)ModuleManager.this.renderer.getStringWidth(text) && Util.mc.field_71441_e != null) {
                        modulex.arrayListOffset += modulex.offset;
                        modulex.sliding = true;
                     } else {
                        modulex.sliding = false;
                     }
                  }
               }

               return;
            }
         } else {
            var1 = ModuleManager.this.sortedModulesABC.iterator();

            while(true) {
               while(var1.hasNext()) {
                  String e = (String)var1.next();
                  Module module = Client.moduleManager.getModuleByName(e);
                  String textx = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                  module.offset = (float)ModuleManager.this.renderer.getStringWidth(textx) / ((Integer)HUD.getInstance().animationHorizontalTime.getValue()).floatValue();
                  module.vOffset = (float)ModuleManager.this.renderer.getFontHeight() / ((Integer)HUD.getInstance().animationVerticalTime.getValue()).floatValue();
                  if (module.isEnabled() && (Integer)HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                     if (module.arrayListOffset > module.offset && Util.mc.field_71441_e != null) {
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                     }
                  } else if (module.isDisabled() && (Integer)HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                     if (module.arrayListOffset < (float)ModuleManager.this.renderer.getStringWidth(textx) && Util.mc.field_71441_e != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                     } else {
                        module.sliding = false;
                     }
                  }
               }

               return;
            }
         }
      }

      public void start() {
         System.out.println("Starting animation thread.");
         this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
      }
   }
}
