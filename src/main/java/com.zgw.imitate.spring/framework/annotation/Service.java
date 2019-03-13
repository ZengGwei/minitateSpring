package com.zgw.imitate.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by gw.zeng on 2018/7/14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
