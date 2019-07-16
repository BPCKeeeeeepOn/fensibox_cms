package com.fensibox.cms.controller.vm.announcement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnnouncementOutVM {

    private String id;

    private String title;

    private String content;

    @JsonProperty("created_time")
    private String createdTime;
}
