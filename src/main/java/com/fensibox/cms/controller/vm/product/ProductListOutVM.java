package com.fensibox.cms.controller.vm.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductListOutVM {

    private String id;

    private String gid;

    private String name;

    private Double price;

    @JsonProperty("member_price")
    private Double memberPrice;
}
