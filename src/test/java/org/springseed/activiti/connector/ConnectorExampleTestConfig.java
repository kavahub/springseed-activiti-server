package org.springseed.activiti.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.api.model.shared.event.VariableCreatedEvent;
import org.activiti.api.process.runtime.connector.Connector;
import org.activiti.api.process.runtime.events.ProcessCompletedEvent;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.activiti.api.runtime.shared.events.VariableEventListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

/**
 * 配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@TestConfiguration
@Slf4j
public class ConnectorExampleTestConfig {
    private List<VariableCreatedEvent> variableCreatedEvents = new ArrayList<>();
    private List<ProcessCompletedEvent> processCompletedEvents = new ArrayList<>();

    @Bean
    public List<VariableCreatedEvent> variableCreatedEvents() {
        return variableCreatedEvents;
    }

    @Bean
    public List<ProcessCompletedEvent> processCompletedEvents() {
        return processCompletedEvents;
    }

    @Bean("Movies.getMovieDesc")
    public Connector getMovieDesc() {
        return getConnector();
    }

    @Bean("MoviesWithUUIDs.getMovieDesc")
    public Connector getMovieDescUUIDs() {
        return getConnector();
    }

    private Connector getConnector() {
        return integrationContext -> {
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            log.info(">>inbound: " + inBoundVariables);
            integrationContext.addOutBoundVariable("movieDescription",
                                                   "The Lord of the Rings is an epic high fantasy novel written by English author and scholar J. R. R. Tolkien");
            return integrationContext;
        };
    }

    @Bean
    public VariableEventListener<VariableCreatedEvent> variableCreatedEventListener() {
        return variableCreatedEvent -> variableCreatedEvents.add(variableCreatedEvent);
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCompletedEvent> processCompletedEventListener() {
        return processCompletedEvent -> processCompletedEvents.add(processCompletedEvent);
    }    
}
