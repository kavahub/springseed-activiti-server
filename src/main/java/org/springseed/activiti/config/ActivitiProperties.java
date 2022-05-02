package org.springseed.activiti.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 属性配置文件
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("springseed.activiti")
public class ActivitiProperties {
    private Log server = new Log();

    @Data
    public static class Log {
        private boolean taskEvent = false;
        private boolean processEvent = false;
    }

}
