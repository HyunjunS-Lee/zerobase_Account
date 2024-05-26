package zerobase.AccountAssignment.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)//어노테이션을 붙이는 타입
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AccountLock {
    long tryLOckTime() default 5000L; //해당시간동안 기다리겠다.
}
