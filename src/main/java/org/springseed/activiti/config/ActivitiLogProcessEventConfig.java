package org.springseed.activiti.config;

import org.activiti.api.process.runtime.events.ProcessCancelledEvent;
import org.activiti.api.process.runtime.events.ProcessCompletedEvent;
import org.activiti.api.process.runtime.events.ProcessStartedEvent;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 记录流程执行日志配置
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "springseed.activiti.log", name = "process_event", havingValue = "true")
public class ActivitiLogProcessEventConfig {
    @Bean
    public ProcessRuntimeEventListener<ProcessStartedEvent> processStartedListener() {
        log.info("Process STARTED event enabled");
        return processStarted -> log.info("Process STARTED [{}]", processStarted.toString());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCompletedEvent> processCompletedListener() {
        log.info("Process COMPLETED event enabled");
        return processCompleted -> log.info("Process COMPLETED, [{}]", processCompleted.toString());
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCancelledEvent> processCancelledListener() {
        log.info("Process CANCELLED event enabled");
        return processCancelled -> log.info("Process COMPLETED, [{}]", processCancelled.toString());
    }
}
