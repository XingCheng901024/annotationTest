package com.tyhy.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2019/6/10.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public  @interface SystemControllerLog {
    //作者
    String author() default "";
    // 模块
    String title() default "";
    // 功能
    String action() default "";
}