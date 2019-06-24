package com.fensibox.cms.controller.vm.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.fensibox.cms.common.CmsConstants.FULL_DATE_TIME;

@Data
public class OrderContentVM {


    @JsonProperty("username")
    private String username;

    @JsonProperty("status")
    private Integer status;
    /**
     * 起始时间
     */
    @JsonFormat(pattern = FULL_DATE_TIME)
    @JsonProperty("period_start")
    private String periodStart;
    /**
     * 截止时间
     */
    @JsonFormat(pattern = FULL_DATE_TIME)
    @JsonProperty("period_end")
    private String periodEnd;
}
