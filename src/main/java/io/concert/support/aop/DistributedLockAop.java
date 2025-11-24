package io.concert.support.aop;

import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {

        String lockKey = parseKey(joinPoint, distributedLock.key());
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();

        RLock rLock = redissonClient.getLock(lockKey);
        boolean lockAcquired = false;

        try {
            lockAcquired = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if (!lockAcquired) {
                log.warn("[RedissonLock] 락 획득 실패 - lockKey: {}", lockKey);
                throw new IllegalStateException("락 획득 실패 - lockKey: " + lockKey);
            }

            log.info("[RedissonLock] 락 획득 성공 - lockKey: {}", lockKey);
            return joinPoint.proceed();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 획득 중 인터럽트 발생", e);

        } finally {
            if (lockAcquired && rLock.isHeldByCurrentThread()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        try {
                            rLock.unlock();
                            log.info("[RedissonLock] 락 해제 완료 - lockKey: {}", lockKey);
                        } catch (Exception e) {
                            log.warn("[RedissonLock] 이미 해제된 락 또는 스레드 불일치 - lockKey: {}", lockKey);
                        }
                    }
                });
            }
        }
    }

    private String parseKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(keyExpression).getValue(context, String.class);
    }
}



//    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
//
//    private final RedissonClient redissonClient;
//    private final AopForTransaction aopForTransaction;
//
//    @Around("@annotation(io.concert.support.aop.DistributedLock)")
//    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
//
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
//
//        String key = REDISSON_LOCK_PREFIX + CustomSpringElParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
//        RLock rLock = redissonClient.getLock(key);
//        boolean available = false;
//        try {
//            log.info("🔐 Try Lock for key={}", key);
//            available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
//            log.info("🔒 Lock result for key={}, available={}", key, available);
//
//            if(!available) {
////                return false;
//                throw new CoreException(ErrorType.LOCK_ACQUISITION_FAILED, key);
//            }
//            return aopForTransaction.proceed(joinPoint);
//
//        } catch (InterruptedException e) {
//            throw new InterruptedException();
//        } finally {
//            try {
//                if(available && rLock.isHeldByCurrentThread()) {
//                    rLock.unlock();
//                    log.info("🔓 Lock released for key={}", key);
//                }
//            } catch (Exception e) {
//                log.info("Redisson Lock Already Unlock ServiceName: {}, key: {}", method.getName(), key);
//            }
//        }
//    }
//}
