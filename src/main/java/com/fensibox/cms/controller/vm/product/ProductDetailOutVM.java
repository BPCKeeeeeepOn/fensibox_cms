package com.fensibox.cms.controller.vm.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fensibox.cms.entity.Product;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.time.LocalDateTime;

@Data
public class ProductDetailOutVM {

    private Long id;

    private Integer gid;

    private Integer cid;

    @JsonProperty("model_id")
    private Integer modelId;

    private String name;

    private String desc;

    private String image;

    private String price;

    @JsonProperty("limit_min")
    private int limitMin;

    @JsonProperty("limit_max")
    private int limitMax;

    private int rate;

    private int close;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public Product buildProduct() {
        Product product = new Product();
        product.setCid(cid);
        product.setClose(close);
        product.setCreatedTime(LocalDateTime.now());
        product.setpDesc(desc);
        product.setGid(Long.valueOf(gid));
        product.setImage(image);
        product.setLimitMax(limitMax);
        product.setLimitMin(limitMin);
        product.setModelId(modelId);
        product.setPrice(Double.valueOf(price));
        product.setMemberPrice(Double.valueOf(price));
        product.setRate(rate);
        product.setpName(name);
        return product;

    }

}
