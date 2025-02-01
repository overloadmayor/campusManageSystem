//org/example/demoaop/aop/Mylog.java
package com.campus.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//表示运行时生效
@Target(ElementType.METHOD)//作用在方法上
public @interface Admin {
}