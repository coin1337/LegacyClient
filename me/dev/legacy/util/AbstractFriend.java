package me.dev.legacy.util;

public abstract class AbstractFriend implements INameable, IFriendable {
   private String name;
   private String alias;

   public AbstractFriend(String name, String alias) {
      this.name = name;
      this.alias = alias;
   }

   public String getAlias() {
      return this.alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setDisplayName(String displayName) {
      this.name = this.name;
   }
}
