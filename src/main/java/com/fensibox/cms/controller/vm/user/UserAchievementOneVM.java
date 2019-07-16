package com.fensibox.cms.controller.vm.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserAchievementOneVM {

    private Integer count;

    @JsonProperty("total_money")
    private Double totalMoney;
}
