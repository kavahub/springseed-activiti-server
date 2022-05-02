package org.springseed.activiti.config;

import org.activiti.api.task.runtime.events.TaskAssignedEvent;
import org.activiti.api.task.runtime.events.TaskCompletedEvent;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 记录任务执行日志配置
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "springseed.activiti.log", name = "task_event", havingValue = "true")
public class ActivitiLogTaskEventConfig {
    @Bean
    public TaskRuntimeEventListener<TaskAssignedEvent> taskAssignedListener() {
        log.info("Task runtime ASSIGNED event enabled");
        return taskAssigned -> log.info("Task ASSIGNED [{}]", taskAssigned.toString());
    }

    @Bean
    public TaskRuntimeEventListener<TaskCompletedEvent> taskCompletedListener() {
        log.info("Task runtime COMPLETED event enabled");
        return taskCompleted -> log.info("Task COMPLETED, [{}]", taskCompleted.toString());

    }
}
