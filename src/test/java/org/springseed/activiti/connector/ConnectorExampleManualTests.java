package org.springseed.activiti.connector;

import java.util.List;

import org.activiti.api.model.shared.event.VariableCreatedEvent;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.process.runtime.events.ProcessCompletedEvent;
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
@Import(ConnectorExampleTestConfig.class)
@Slf4j
public class ConnectorExampleManualTests {
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private List<VariableCreatedEvent> variableCreatedEvents;
    @Autowired
    private List<ProcessCompletedEvent> processCompletedEvents;

    @Test
    public void runConnectorExample() {
        securityUtil.logInAs("reviewer");

        listAvailableProcesses();
        ProcessInstance processInstance = startProcess();
        listProcessVariables(processInstance);
        completeAvailableTasks();
        listAllCreatedVariables();
        listCompletedProcesses();
    }

    private void listCompletedProcesses() {
        log.info(">>> Completed process Instances: ");
        processCompletedEvents.forEach(
                processCompletedEvent -> log.info("\t> Process instance : " + processCompletedEvent.getEntity()));
    }

    private void listAllCreatedVariables() {
        log.info(">>> All created variables:");
        variableCreatedEvents
                .forEach(variableCreatedEvent -> log.info("\t> name:`" + variableCreatedEvent.getEntity().getName()
                        + "`, value: `" + variableCreatedEvent.getEntity().getValue()
                        + "`, processInstanceId: `" + variableCreatedEvent.getEntity().getProcessInstanceId()
                        + "`, taskId: `" + variableCreatedEvent.getEntity().getTaskId() + "`"));
    }

    private void completeAvailableTasks() {
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0,
                20));
        tasks.getContent().forEach(task -> {
            log.info(">>> Performing task -> " + task);
            listTaskVariables(task);
            taskRuntime.complete(TaskPayloadBuilder
                    .complete()
                    .withTaskId(task.getId())
                    .withVariable("rating", 5)
                    .build());
        });
    }

    private void listTaskVariables(Task task) {
        log.info(">>> Task variables:");
        taskRuntime.variables(TaskPayloadBuilder
                .variables()
                .withTaskId(task.getId())
                .build())
                .forEach(variableInstance -> log
                        .info("\t> " + variableInstance.getName() + " -> " + variableInstance.getValue()));
    }

    private void listProcessVariables(ProcessInstance processInstance) {
        log.info(">>> Process variables:");
        List<VariableInstance> variables = processRuntime.variables(
                ProcessPayloadBuilder
                        .variables()
                        .withProcessInstance(processInstance)
                        .build());
        variables.forEach(variableInstance -> log
                .info("\t> " + variableInstance.getName() + " -> " + variableInstance.getValue()));
    }

    private ProcessInstance startProcess() {
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("RankMovieId")
                .withName("myProcess")
                .withVariable("movieToRank",
                        "Lord of the rings")
                .build());
        log.info(">>> Created Process Instance: " + processInstance);
        return processInstance;
    }

    private void listAvailableProcesses() {
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0,
                10));
        log.info("> Available Process definitions: " + processDefinitionPage.getTotalItems());
        for (ProcessDefinition pd : processDefinitionPage.getContent()) {
            log.info("\t > Process definition: " + pd);
        }
    }
}
