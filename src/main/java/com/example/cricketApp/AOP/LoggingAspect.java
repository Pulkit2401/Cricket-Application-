package com.example.cricketApp.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER= LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.cricketApp.Service.MatchService.playMatch(..))")
    public Object loggingMethodMatch(ProceedingJoinPoint jp) throws Throwable {
        LOGGER.info("Before Starting match");
        long startTime = System.currentTimeMillis();
        Object obj =jp.proceed();
        long endTime = System.currentTimeMillis();
        LOGGER.info("Time taken to complete the match: " + (endTime - startTime) + " ms");
        LOGGER.info("Match has ended");
        return obj;
    }

    @Around("execution(* com.example.cricketApp.Service.PlayerService.getPlayerById(..))")
    public Object loggingMethodPlayer(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj =jp.proceed();
        long endTime = System.currentTimeMillis();
        LOGGER.info("Time taken to fetch the player: " + (endTime - startTime) + " ms");
        return obj;
    }
}

