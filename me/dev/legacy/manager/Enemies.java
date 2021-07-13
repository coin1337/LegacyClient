package me.dev.legacy.manager;

import io.netty.util.internal.ConcurrentSet;
import java.util.Iterator;
import me.dev.legacy.Client;
import me.dev.legacy.util.Enemy;

public class Enemies extends RotationManager {
   private static ConcurrentSet<Enemy> enemies = new ConcurrentSet();

   public static void addEnemy(String name) {
      enemies.add(new Enemy(name));
   }

   public static void delEnemy(String name) {
      enemies.remove(getEnemyByName(name));
   }

   public static Enemy getEnemyByName(String name) {
      Iterator var1 = enemies.iterator();

      Enemy e;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         e = (Enemy)var1.next();
      } while(!Client.enemy.username.equalsIgnoreCase(name));

      return e;
   }

   public static ConcurrentSet<Enemy> getEnemies() {
      return enemies;
   }

   public static boolean isEnemy(String name) {
      return enemies.stream().anyMatch((enemy) -> {
         return enemy.username.equalsIgnoreCase(name);
      });
   }
}
