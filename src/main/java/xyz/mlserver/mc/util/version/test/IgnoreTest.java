package xyz.mlserver.mc.util.version.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreTest {
    String value() default "no reason specified";
}
