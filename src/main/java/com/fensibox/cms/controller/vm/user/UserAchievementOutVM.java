package com.fensibox.cms.controller.vm.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserAchievementOutVM {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer role;
    //用户统计
    @JsonProperty("last_day_count")
    private Integer lastDayCount;

    @JsonProperty("month_count")
    private Integer monthCount;

    @JsonProperty("last_month_count")
    private Integer lastMonthCount;

    @JsonProperty("total_count")
    private Integer totalCount;
    //充值记录统计
    @JsonProperty("last_day_money")
    private Double lastDayMoney;

    @JsonProperty("month_money")
    private Double monthMoney;

    @JsonProperty("last_month_money")
    private Double lastMonthMoney;

    @JsonProperty("total_money")
    private Double totalMoney;
}
