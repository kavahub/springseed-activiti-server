package org.springseed.activiti.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springseed.activiti.ActivitiApplication;
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
@Import(IntegrationExampleTestConfig.class)
@Slf4j
public class IntegrationExampleManualTests {

    @Test
    public void test() throws InterruptedException, IOException {
        Path folder = IntegrationExampleTestConfig.INPUT_DIR;
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }
  
        fillFileData(folder.resolve("1.txt"), "hello from london");
        fillFileData(folder.resolve("2.txt"), "Hi there from activiti!");
        fillFileData(folder.resolve("3.txt"), "hall good news over here.");
        fillFileData(folder.resolve("4.txt"), "I've tweeted about activiti today.");
        fillFileData(folder.resolve("5.txt"), "other boring projects.");
        fillFileData(folder.resolve("6.txt"), "activiti cloud - Cloud Native Java BPM");
        TimeUnit.SECONDS.sleep(10);
    }

    private void fillFileData(final Path file, final String data) throws IOException {
        log.info("> begin to create file {}", file.getFileName().toString());
        final byte[] strToBytes = data.getBytes();
        Files.write(file, strToBytes);
    }
}
