package org.springseed.activiti.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 流程自动配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ActivitiProperties.class})
public class ActivitiAutoConfig {
    
}
