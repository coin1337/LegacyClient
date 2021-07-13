package me.dev.legacy.features.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ModuleManifest {
   String label() default "";

   Module.Category category();

   int key() default 0;

   boolean persistent() default false;

   boolean enabled() default false;

   boolean listen() default true;

   boolean drawn() default true;
}
