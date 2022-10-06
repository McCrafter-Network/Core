package me.lucky.core.api.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface DataColumn {
    String Key() default "";

    boolean IsAuto() default false;

    boolean IsConstraint() default false;
}
