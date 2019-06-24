package com.fensibox.cms.controller.vm.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserAchievementOutVM {

    @JsonProperty("last_day")
    private int lastDay;

    private int month;

    @JsonProperty("last_month")
    private int lastMonth;

    private int total;
}
