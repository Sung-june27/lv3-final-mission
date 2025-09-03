package finalmission.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class ApiLogger {

    @Around("@within(org.springframework.web.bind.annotation.RestController)"
    + "|| @within(org.springframework.stereotype.Controller)")
    public Object loggingApi(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String uri = request.getRequestURI();
        String methodName = joinPoint.getSignature().getName();

        log.info("{} {}", uri, methodName);
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            String className = joinPoint.getSignature().getDeclaringTypeName();

            StackTraceElement topStackTrace = e.getStackTrace()[0];
            int lineNumber = topStackTrace.getLineNumber();

            log.error("{} {} - {}.{}():{} - {}: {}",
                    uri, methodName, className, methodName, lineNumber, e.getClass().getName(), e.getMessage());

            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("API 요청 소요시간: " + (endTime - startTime) + "ms");
        }
    }
}
