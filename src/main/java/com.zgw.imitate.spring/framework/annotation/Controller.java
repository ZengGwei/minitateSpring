package com.zgw.imitate.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 〈〉
 *
 * @author gw.Zeng
 * @create 2018/7/14
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
