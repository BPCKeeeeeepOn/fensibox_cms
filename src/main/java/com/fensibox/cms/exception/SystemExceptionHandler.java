package com.fensibox.cms.exception;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class SystemExceptionHandler {
    
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseResult handlerGlobalException(HttpServletRequest req, HttpServletResponse res, BizException ex) {
        return ex.getContent();
    }

    /**
     * 参数校验不通过拦截
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult methodArgumentNotValidHandler(HttpServletRequest req, HttpServletResponse res, MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);

        Map<String, Object> errors = BindingResultUtil.getErrors(exception.getBindingResult());
    
        ResponseResult result = ResponseResult.fail(ResultCode.PARAMS_ERROR).body(errors);
        
        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult handlerException(HttpServletRequest req, HttpServletResponse res, Exception e) {
        log.error(e.getMessage(), e);
    
        ResponseResult result = ResponseResult.fail(ResultCode.INTERNAL_SERVER_ERROR).body(e.getMessage());

        return result;
    }
}
