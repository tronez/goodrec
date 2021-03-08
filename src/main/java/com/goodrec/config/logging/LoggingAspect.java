package com.goodrec.config.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

@Aspect
class LoggingAspect {

    @Pointcut("@within(com.goodrec.config.logging.Log)")
    void logAnnotation() {
//        pointcut for all classes implementing @Log annotation
    }

    @Around("logAnnotation()")
    Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();
        log(joinPoint, stopWatch, proceed);
        return proceed;
    }

    private void log(ProceedingJoinPoint joinPoint, StopWatch stopWatch, Object result) {
        String methodName = getMethodName(joinPoint);
        String timerString = createTimerString(stopWatch);
        String resultString = createResultString(result);

        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        logger.info("{}{} {}", methodName, timerString, resultString);
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        String classWithPackageName = joinPoint.getSignature().getDeclaringTypeName();
        String className = classWithPackageName.substring(classWithPackageName.lastIndexOf('.') + 1);
        return className + "." + joinPoint.getSignature().getName();
    }

    private String createTimerString(StopWatch stopWatch) {
        long millis = stopWatch.getTotalTimeMillis();
        return String.format(" [%d ms]", millis);
    }

    private String createResultString(Object result) {
        return String.format("operation result: %s", result);
    }
}
