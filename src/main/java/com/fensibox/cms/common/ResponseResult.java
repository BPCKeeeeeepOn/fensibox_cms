package com.fensibox.cms.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pagehelper.Page;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.fensibox.cms.common.CmsConstants.DATE_FORMATTER.DATETIME_IN_CHINESE;

public class ResponseResult {
    @Getter
    private Integer code;
    @Getter
    private String msg;
    @Getter
    private Object data;
    
    private ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    private ResponseResult(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }
    
    public static ResponseResult success(ResultCode code) {
        return new ResponseResult(code);
    }
    
    public static ResponseResult success() {
        return new ResponseResult(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }
    
    public static ResponseResult fail() {
        return new ResponseResult(ResultCode.DEFAULT_ERROR);
    }
    
    public static ResponseResult fail(ResultCode code) {
        return new ResponseResult(code.getCode(), code.getMsg());
    }
    
    public ResponseResult body(Object data) {
        if (data instanceof Page) {
            Page<?> ref = (Page<?>) data;
            ResponseData inner = new ResponseData();
            inner.items = ref.getResult();
            inner.page = new ResultPage(ref.getPageNum(), ref.getPageSize(), ref.getPages(), ref.getTotal(), ref.getOrderBy());
            this.data = inner;
        } else {
            this.data = data;
        }
        return this;
    }
    
    class ResponseData {
        @JsonProperty("items")
        private Object items;
        @JsonProperty("page")
        private ResultPage page;
        
        @JsonProperty("last_updated_time")
        public String getLastUpdatedTime() {
            return LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(DATETIME_IN_CHINESE));
        }
        
    }
}
