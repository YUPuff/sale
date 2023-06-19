package com.example.sale.annotation;

import java.lang.annotation.*;

/**
 * 此注解作用为标识无需登录验证就可以调用的方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuth {
}
