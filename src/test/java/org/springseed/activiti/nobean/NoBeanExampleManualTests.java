package org.springseed.activiti.nobean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springseed.activiti.ActivitiApplication;
import org.springseed.activiti.SecurityUtil;
import org.springseed.activiti.SpringseedActiveProfiles;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ActivitiApplication.class })
@SpringseedActiveProfiles
@Import(NoBeanExampleTestConfig.class)
@Slf4j
public class NoBeanExampleManualTests {
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void processText() {

        securityUtil.logInAs("system");

        LinkedHashMap<String, Object> content = pickRandomString();
        //here content represents a json document with a body, approved flag and tags as attributes
        //these attributes could be handled as plain variables as the variables are in activiti-api-basic-process-example
        //or the document could be mapped directly to a bean as in activiti-api-basic-full-example-bean

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        log.info("> Starting process to process content: " + content + " at " + formatter.format(new Date()));

        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("categorizeHumanNoBeanProcess")
                .withName("Processing Content: " + content)
                .withVariable("content", objectMapper.convertValue(content, JsonNode.class))
                .build());
        log.info(">>> Created Process Instance: " + processInstance);


    }

    @Test
    public void checkAndWorkOnTasksWhenAvailable() {
        securityUtil.logInAs("salaboy");

        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
        if (tasks.getTotalItems() > 0) {
            for (Task t : tasks.getContent()) {

                log.info("> Claiming task: " + t.getId());
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(t.getId()).build());

                List<VariableInstance> variables = taskRuntime.variables(TaskPayloadBuilder.variables().withTaskId(t.getId()).build());
                VariableInstance variableInstance = variables.get(0);
                if (variableInstance.getName().equals("content")) {
                    LinkedHashMap<String, Object> contentToProcess = objectMapper.convertValue(variableInstance.getValue(), new TypeReference<LinkedHashMap<String, Object>>(){});
                    log.info("> Content received inside the task to approve: " + contentToProcess);

                    if (contentToProcess.get("body").toString().contains("activiti")) {
                        log.info("> User Approving content");
                        contentToProcess.put("approved",true);
                    } else {
                        log.info("> User Discarding content");
                        contentToProcess.put("approved",false);
                    }
                    taskRuntime.complete(TaskPayloadBuilder.complete()
                            .withTaskId(t.getId()).withVariable("content", contentToProcess).build());
                }


            }

        } else {
            log.info("> There are no task for me to work on.");
        }

    }   

    private LinkedHashMap<String, Object> pickRandomString() {
        String[] texts = {"hello from london", "Hi there from activiti!", "all good news over here.", "I've tweeted about activiti today.",
                "other boring projects.", "activiti cloud - Cloud Native Java BPM"};
        LinkedHashMap<String,Object> content = new LinkedHashMap<>();
        content.put("body",texts[new Random().nextInt(texts.length)]);
        return content;
    }
}
