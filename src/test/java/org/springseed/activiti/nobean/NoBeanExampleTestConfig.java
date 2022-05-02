package org.springseed.activiti.nobean;

import java.util.Collections;
import java.util.LinkedHashMap;

import org.activiti.api.process.runtime.connector.Connector;
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
@SuppressWarnings("unchecked")
public class NoBeanExampleTestConfig {
    @Bean
    public Connector tagTextConnectorNoBean() {
        return integrationContext -> {
            LinkedHashMap<String, Object> contentToTag = (LinkedHashMap<String, Object>) integrationContext.getInBoundVariables().get("content");
            contentToTag.put("tags", Collections.singletonList(" :) "));
            integrationContext.addOutBoundVariable("content",
                    contentToTag);
            log.info("Final Content: " + contentToTag);
            return integrationContext;
        };
    }

    @Bean
    public Connector discardTextConnectorNoBean() {
        return integrationContext -> {
            LinkedHashMap<String, Object> contentToDiscard = (LinkedHashMap<String, Object>) integrationContext.getInBoundVariables().get("content");
            contentToDiscard.put("tags", Collections.singletonList(" :( "));
            integrationContext.addOutBoundVariable("content",
                    contentToDiscard);
                    log.info("Final Content: " + contentToDiscard);
            return integrationContext;
        };
    }  
}
