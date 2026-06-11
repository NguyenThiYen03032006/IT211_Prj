package com.it211_prj.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    // Do thoi gian thuc thi cho tat ca public method o controller va service.
    @Around("execution(public * com.it211_prj.service..*(..)) || execution(public * com.it211_prj.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            log.info("[EXECUTION] {} completed in {}ms", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
            return result;
        } catch (Throwable ex) {
            log.info("[EXECUTION] {} failed in {}ms", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
            throw ex;
        }
    }

    // Pointcut gom cac service quan trong can audit theo prompt.
    @Before("execution(* com.it211_prj.service.AuthService.login(..))")
    public void beforeLogin(JoinPoint joinPoint) {
        log.info("[AUTH] Login attempt with args={}", joinPoint.getArgs());
    }

    @AfterReturning("execution(* com.it211_prj.service.AuthService.login(..))")
    public void afterLogin() {
        log.info("[AUTH] Login success");
    }

    @AfterReturning("execution(* com.it211_prj.service.AuthService.logout(..))")
    public void afterLogout() {
        log.info("[AUTH] Logout success and token blacklisted");
    }

    @AfterReturning("execution(* com.it211_prj.service.SubmissionService.upload(..))")
    public void afterUploadSubmission(JoinPoint joinPoint) {
        log.info("[SUBMISSION] Student uploaded submission with args={}", joinPoint.getArgs());
    }

    @AfterReturning("execution(* com.it211_prj.service.GradeService.grade(..))")
    public void afterGrade(JoinPoint joinPoint) {
        log.info("[GRADE] Lecturer graded submission with args={}", joinPoint.getArgs());
    }

    @AfterReturning("execution(* com.it211_prj.service.CourseService.create(..))")
    public void afterCreateCourse(JoinPoint joinPoint) {
        log.info("[COURSE] Course created with args={}", joinPoint.getArgs());
    }

    @AfterReturning("execution(* com.it211_prj.service.UserService.delete(..))")
    public void afterDeleteUser(JoinPoint joinPoint) {
        log.info("[USER] User deleted with args={}", joinPoint.getArgs());
    }

    // Ghi loi service de debug va phuc vu yeu cau After Throwing cua AOP.
    @AfterThrowing(pointcut = "execution(* com.it211_prj.service..*(..))", throwing = "ex")
    public void afterServiceError(JoinPoint joinPoint, Throwable ex) {
        log.error("[ERROR] {} failed: {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }
}
