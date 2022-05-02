package org.springseed.activiti.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 实体
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@Slf4j
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,property = "@class")
public class Content {
    private String body;
    private boolean approved;
    private List<String> tags;

    @JsonCreator
    public Content(@JsonProperty("body")String body, @JsonProperty("approved")boolean approved, @JsonProperty("tags")List<String> tags){
        this.body = body;
        this.approved = approved;
        this.tags = tags;
        if(this.tags == null){
            this.tags = new ArrayList<>();
        }
        log.info(">> body:{}, approved:{}, tags:{}", body, approved, tags);
    }  
}
