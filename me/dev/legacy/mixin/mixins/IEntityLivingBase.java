package me.dev.legacy.mixin.mixins;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({EntityLivingBase.class})
public interface IEntityLivingBase {
   @Invoker("getArmSwingAnimationEnd")
   int getArmSwingAnimationEnd();
}
