package org.example.aspects;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
public class LogAspect {
    public static final Logger LOGGER = LogManager.getLogger("com.qaconsultants.core");
    private static final Level LOGGING_LEVEL = Level.DEBUG;

    /* ********************************* POINTCUTs ******************************************** */
    @Pointcut("execution(* org.example.core..*(..)) && ! execution(* *.toBuilder(..))")
    public void pointcutExecutionFramework() {
        // it is a pointcut
    }

    @Pointcut("execution(* *.toString(..)) || execution(* *.lambda$*(..))   || execution(* *.hashCode(..)) ")
    public void noNeedMethods() {
        // it is a pointcut
    }

    @Pointcut("@annotation(org.example.aspects.NoAopLog)")
    public void noLog() {
        // it is a pointcut
    }

    @Pointcut("@annotation(org.example.aspects.ReplaceAop)")
    public void replaceAop() {
        // it is a pointcut
    }


    /* ********************************* ADVICE ******************************************** */
    @Before("pointcutExecutionFramework() && ! noLog()")
    public void beforeLog(@NotNull JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        Object[] arguments = joinPoint.getArgs();
        LOGGER.log(LOGGING_LEVEL, () -> "[>>] " + methodName + "(" + Arrays.toString(arguments) + ")");

    }

    @Around(value = "pointcutExecutionFramework()  && noLog() && !noNeedMethods() ")
    public Object aroundNoLog(@NotNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    @Around(value = "replaceAop()")
    public Object aroundReplace(@NotNull ProceedingJoinPoint proceedingJoinPoint) {
        LOGGER.log(LOGGING_LEVEL, "We start working instead of " + proceedingJoinPoint.getSignature().getName());
        LOGGER.log(LOGGING_LEVEL, "We have finished working instead of " + proceedingJoinPoint.getSignature().getName());
        return null;
    }

    @Around(value = "replaceAop()")
    public Object aroundReplace2(@NotNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LOGGER.log(LOGGING_LEVEL, "We start working instead of " + proceedingJoinPoint.getSignature().getName());
        Object result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        LOGGER.log(LOGGING_LEVEL, "We have finished working instead of " + proceedingJoinPoint.getSignature().getName());
        return result;
    }

    @AfterReturning(value = "pointcutExecutionFramework()   && ! noLog() && !noNeedMethods()"
            , returning = "result")
    public void afterReturningLog(@NotNull JoinPoint joinPoint, Object result) {
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();
        if (!method.getGenericReturnType().toString().equals("void")) {
            String methodReturnedResultAsString = result.toString();
            LOGGER.log(LOGGING_LEVEL, "[o<] [{}] <== {}::{}()"
                    , methodReturnedResultAsString
                    , signature.getDeclaringType().getSimpleName()
                    , signature.getName());
        } else {
            LOGGER.log(LOGGING_LEVEL, () -> "[<<] "
                    + signature.getDeclaringType().getSimpleName()
                    + "::"
                    + signature.getName()
                    + "()");

        }
    }

    @AfterThrowing(value = "pointcutExecutionFramework()", throwing = "e")
    public void afterThrowingFramework(@NotNull JoinPoint joinPoint, Throwable e) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName()
                + "." + signature.getName();
        String signatureString = signature.toString();
        Object[] arguments = joinPoint.getArgs();
        LOGGER.error(() -> "Exception in method: ["
                + methodName + "] with arguments "
                + "("
                + (Arrays.toString(arguments))
                + ")\n"
                + " Signature [ " + signatureString + " ]"
                + "\n Exception [ "
                + e.getMessage() + " ]");
    }
}
