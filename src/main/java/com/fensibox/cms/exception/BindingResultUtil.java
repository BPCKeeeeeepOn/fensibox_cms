package com.fensibox.cms.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingResultUtil {

    public static Map<String, Object> getErrors(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }

        Map<String, Object> errors = new HashMap<>();
        List<ObjectError> list = bindingResult.getAllErrors();
        list.stream().forEach(objectError -> {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                return;
            }
            errors.put(objectError.getObjectName(), objectError.getDefaultMessage());
        });

        return errors;
    }

}
