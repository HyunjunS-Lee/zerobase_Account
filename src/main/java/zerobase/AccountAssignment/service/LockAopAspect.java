package zerobase.AccountAssignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import zerobase.AccountAssignment.aop.AccountLockIdInterface;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;
    @Around("@annotation(zerobase.AccountAssignment.aop.AccountLock) && args(request)")
    public Object aroundMethod(ProceedingJoinPoint pjp, AccountLockIdInterface request) throws Throwable {
        //lock 취득시도
        lockService.lock(request.getAccountNumber());
        try {
            //after before 모두
            return pjp.proceed();
        }  finally {
            //lock을 해제
            lockService.unlock(request.getAccountNumber());
        }
    }
}
