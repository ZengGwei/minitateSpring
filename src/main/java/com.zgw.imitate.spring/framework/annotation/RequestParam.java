package com.zgw.imitate.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by gw.zeng on 2018/7/14.
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
