package com.example.springaop.testaop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@Aspect
public class LoggingAspect {


    @Pointcut(value = "@annotation(Log)")
    private void logPointCut () {
    }

    /*
    here value =@annotation(Log) is PointCut Expression)
     */
    @Before(value = "@annotation(Log)")
    public void logAllAdviceMethodCalls () {
        System.out.println("Inside Aspect : ");
    }

    @After(value = "execution(public void com.example.springaop.testaop.ShipmentService.matchJoinPointExecution()))")
    public void logJoinPointAfterAdviceWithExecutionExpression () {
        System.out.println("ogJoinPointAfterAdviceWithExecutionExpression" + "..........");
    }

    @Before(value = "within(com.example.springaop.testaop.ShipmentService)")
    public void logPointCutExpressionWithWithinExpression () {
        System.out.println("Inside Advice Method logPointCutExpressionWithWithinExpression ........");
    }

    @Before(value = "@annotation(com.example.springaop.testaop.BeforeLog)")
    public ResponseEntity<String> logBeforeAspect (JoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        StringBuilder stringBuilder = new StringBuilder();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        BeforeLog annotation = method.getAnnotation(BeforeLog.class);

        if (annotation.allow()) {
            stringBuilder.append("Target Method Name : " + method.getName() + "\n");
            stringBuilder.append("Request Method Type : " + request.getMethod() + "\n");
            stringBuilder.append("Server Name : " + request.getMethod() + "\n");
            stringBuilder.append("Response : " + response.getStatus() + "\n");
        }
        System.out.println(stringBuilder.toString());
        return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.OK);

    }

    @After("@annotation(AfterLog)")
    public ResponseEntity<String> logAfterAspect (JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        AfterLog annotation = method.getAnnotation(AfterLog.class);

        String methodName = method.getName();

        StringBuilder stringBuilder = new StringBuilder();

        if (annotation.allow()) {
            stringBuilder.append("Target Method Name : " + methodName + "\n");
            stringBuilder.append("Request Method Type : " + request.getMethod() + "\n");
            stringBuilder.append("Response Status : " + response.getStatus() + "\n");
        }

        System.out.println(stringBuilder.toString());
        return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.OK);
    }

    @Around(value = "@annotation(AroundLog)")
    public ResponseEntity<String> getAroundAdvice (ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        StringBuilder stringBuilder = new StringBuilder();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        AroundLog annotation = signature.getMethod().getAnnotation(AroundLog.class);

        joinPoint.proceed();

        if (annotation.allow()) {
            stringBuilder.append("Target Method Name : " + methodName + "\n");
            stringBuilder.append("Request Method Type : " + request.getMethod() + "\n");
            stringBuilder.append("Server Name : " + request.getMethod() + "\n");
            stringBuilder.append("Response : " + response.getStatus() + "\n");
        }
        System.out.println(stringBuilder.toString());

        return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.OK);

    }
}
