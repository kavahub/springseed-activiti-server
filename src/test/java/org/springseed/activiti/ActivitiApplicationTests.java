package org.springseed.activiti;

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ActivitiApplication.class })
@SpringseedActiveProfiles
public class ActivitiApplicationTests {
    private static final String PROCESS_DEFINITION_KEY = "categorizeProcess";

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Test
    public void contextLoads() {
        securityUtil.logInAs("system");

        ProcessDefinition processDefinition = processRuntime.processDefinition(PROCESS_DEFINITION_KEY);

        assertThat(processDefinition).isNotNull();
        assertThat(processDefinition.getKey()).isEqualTo(PROCESS_DEFINITION_KEY);
        assertThat(processDefinition.getAppVersion()).isNull();
    } 
}
