package com.fensibox.cms.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductListOut {

    private int gid;

    private int cid;

    private String name;

    private String image;

    @JsonProperty("model_id")
    private int modelId;

    private int close;

}
