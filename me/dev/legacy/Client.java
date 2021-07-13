package me.dev.legacy;

import java.io.InputStream;
import java.nio.ByteBuffer;
import me.dev.legacy.event.events.Render3DEvent;
import me.dev.legacy.features.gui.font.CustomFont;
import me.dev.legacy.manager.ColorManager;
import me.dev.legacy.manager.CommandManager;
import me.dev.legacy.manager.ConfigManager;
import me.dev.legacy.manager.EventManager;
import me.dev.legacy.manager.FileManager;
import me.dev.legacy.manager.FriendManager;
import me.dev.legacy.manager.HoleManager;
import me.dev.legacy.manager.InventoryManager;
import me.dev.legacy.manager.ModuleManager;
import me.dev.legacy.manager.PacketManager;
import me.dev.legacy.manager.PositionManager;
import me.dev.legacy.manager.PotionManager;
import me.dev.legacy.manager.ReloadManager;
import me.dev.legacy.manager.RotationManager;
import me.dev.legacy.manager.ServerManager;
import me.dev.legacy.manager.SpeedManager;
import me.dev.legacy.manager.TextManager;
import me.dev.legacy.manager.TimerManager;
import me.dev.legacy.util.Enemy;
import me.dev.legacy.util.IconUtil;
import me.dev.legacy.util.Title;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(
   modid = "legacy",
   name = "legacy",
   version = "1.0.0"
)
public class Client {
   public static final String MODID = "legacy";
   public static final String MODNAME = "Legacy";
   public static final String MODVER = "1.0.0";
   public static final Logger LOGGER = LogManager.getLogger("legacy");
   public static TimerManager timerManager;
   public static CommandManager commandManager;
   public static FriendManager friendManager;
   public static ModuleManager moduleManager;
   public static PacketManager packetManager;
   public static ColorManager colorManager;
   public static HoleManager holeManager;
   public static InventoryManager inventoryManager;
   public static PotionManager potionManager;
   public static RotationManager rotationManager;
   public static PositionManager positionManager;
   public static SpeedManager speedManager;
   public static ReloadManager reloadManager;
   public static FileManager fileManager;
   public static ConfigManager configManager;
   public static ServerManager serverManager;
   public static EventManager eventManager;
   public static TextManager textManager;
   public static CustomFont fontRenderer;
   public static Render3DEvent render3DEvent;
   public static Enemy enemy;
   @Instance
   public static Client INSTANCE;
   private static boolean unloaded = false;

   public static void load() {
      LOGGER.info("loading legacy");
      unloaded = false;
      if (reloadManager != null) {
         reloadManager.unload();
         reloadManager = null;
      }

      textManager = new TextManager();
      commandManager = new CommandManager();
      friendManager = new FriendManager();
      moduleManager = new ModuleManager();
      rotationManager = new RotationManager();
      packetManager = new PacketManager();
      eventManager = new EventManager();
      speedManager = new SpeedManager();
      potionManager = new PotionManager();
      inventoryManager = new InventoryManager();
      serverManager = new ServerManager();
      fileManager = new FileManager();
      colorManager = new ColorManager();
      positionManager = new PositionManager();
      configManager = new ConfigManager();
      holeManager = new HoleManager();
      LOGGER.info("Managers loaded.");
      moduleManager.init();
      LOGGER.info("Modules loaded.");
      configManager.init();
      eventManager.init();
      LOGGER.info("EventManager loaded.");
      textManager.init(true);
      moduleManager.onLoad();
      LOGGER.info("legacy successfully loaded!\n");
   }

   public static void unload(boolean unload) {
      LOGGER.info("unloading legacy");
      if (unload) {
         reloadManager = new ReloadManager();
         reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
      }

      onUnload();
      eventManager = null;
      friendManager = null;
      speedManager = null;
      holeManager = null;
      positionManager = null;
      rotationManager = null;
      configManager = null;
      commandManager = null;
      colorManager = null;
      serverManager = null;
      fileManager = null;
      potionManager = null;
      inventoryManager = null;
      moduleManager = null;
      textManager = null;
      LOGGER.info("legacy unloaded!\n");
   }

   public static void reload() {
      unload(false);
      load();
   }

   public static void onUnload() {
      if (!unloaded) {
         eventManager.onUnload();
         moduleManager.onUnload();
         configManager.saveConfig(configManager.config.replaceFirst("legacy/", ""));
         moduleManager.onUnloadPost();
         unloaded = true;
      }

   }

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      LOGGER.info("blackbro4 n' rianix cool ppl's");
   }

   public static void setWindowIcon() {
      if (Util.func_110647_a() != EnumOS.OSX) {
         try {
            InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/legacy/icons/legacy-16x.png");
            Throwable var1 = null;

            try {
               InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/legacy/icons/legacy-32x.png");
               Throwable var3 = null;

               try {
                  ByteBuffer[] icons = new ByteBuffer[]{IconUtil.INSTANCE.readImageToBuffer(inputStream16x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x)};
                  Display.setIcon(icons);
               } catch (Throwable var28) {
                  var3 = var28;
                  throw var28;
               } finally {
                  if (inputStream32x != null) {
                     if (var3 != null) {
                        try {
                           inputStream32x.close();
                        } catch (Throwable var27) {
                           var3.addSuppressed(var27);
                        }
                     } else {
                        inputStream32x.close();
                     }
                  }

               }
            } catch (Throwable var30) {
               var1 = var30;
               throw var30;
            } finally {
               if (inputStream16x != null) {
                  if (var1 != null) {
                     try {
                        inputStream16x.close();
                     } catch (Throwable var26) {
                        var1.addSuppressed(var26);
                     }
                  } else {
                     inputStream16x.close();
                  }
               }

            }
         } catch (Exception var32) {
            LOGGER.error("Couldn't set Windows Icon", var32);
         }
      }

   }

   private void setWindowsIcon() {
      setWindowIcon();
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      MinecraftForge.EVENT_BUS.register(new Title());
      load();
      this.setWindowsIcon();
   }
}
