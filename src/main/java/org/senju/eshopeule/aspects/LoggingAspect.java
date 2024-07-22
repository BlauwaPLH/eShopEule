package org.senju.eshopeule.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.senju.eshopeule.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class LoggingAspect {

    private static final Set<Class<? extends Throwable>> NO_STACK_TREE_EXCEPTIONS;
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    static {
        NO_STACK_TREE_EXCEPTIONS = Set.of(
                NotFoundException.class,
                ObjectAlreadyExistsException.class,
                JwtAuthenticationException.class,
                LoginException.class,
                SignUpException.class,
                VerifyException.class,
                RefreshTokenException.class,
                SendNotificationException.class,
                ChangePasswordException.class,

                CartException.class,
                OrderException.class,
                ProductException.class,
                RatingException.class,
                PagingException.class
        );
    }

    @Pointcut(value = "execution(* org.senju.eshopeule.service.impl..*(..))")
    public void serviceMethod() {}

    @Around(value = "serviceMethod()")
    public Object logMethodDetails(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();

        logger.info("Calling method {}", methodName);
        logger.info("Arguments: {}", args);

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed(args);
            return result;
        } catch (Throwable thr) {
            if (NO_STACK_TREE_EXCEPTIONS.contains(thr.getClass())) {
                logger.error("Exception in method: [{}], [class: {}] with message [{}]", methodName, className, thr.getMessage());
            }
            else {
                logger.error("Exception in method: {} [class: {}]", methodName, className, thr);
            }
            throw thr;
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.info("Execution time of method {} [class: {}] : {}ms", methodName, className, duration);
        }
    }
}
