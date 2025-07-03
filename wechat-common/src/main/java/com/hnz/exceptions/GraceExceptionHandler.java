package com.hnz.exceptions;

import com.hnz.result.R;
import com.hnz.result.ResponseStatusEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GraceExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public R returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return R.exception(ResponseStatusEnum.FILE_MAX_SIZE_500KB_ERROR);
    }

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public R returnMyCustomException(MyCustomException e) {
        e.printStackTrace();
        return R.exception(e.getResponseStatusEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R returnNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> errors = getErrors(result);
        return R.errorMap(errors);
    }

    public Map<String, String> getErrors(BindingResult result) {

        Map<String, String> map = new HashMap<>();

        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError fe : errorList) {
            // 错误所对应的属性字段名
            String field = fe.getField();
            // 错误信息
            String message = fe.getDefaultMessage();

            map.put(field, message);
        }

        return map;
    }

}
