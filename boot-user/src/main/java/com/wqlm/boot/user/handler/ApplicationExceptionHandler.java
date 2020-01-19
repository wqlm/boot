package com.wqlm.boot.user.handler;

import com.wqlm.boot.user.enums.ApplicationEnum;
import com.wqlm.boot.user.exception.ApplicationException;
import com.wqlm.boot.user.vo.result.FailResult;
import com.wqlm.boot.user.vo.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一异常处理
 * 如果一个异常能匹配多个 @ExceptionHandler 时，选择匹配深度最小的Exception(即最匹配的Exception)
 */
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    /**
     * 处理自定义 ApplicationException 异常
     * ExceptionHandler 用于指定异常的类型
     * ResponseStatus 用于指定http的返回状态
     *
     * @param e
     * @return
     */
    @ExceptionHandler({ApplicationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result handleApplicationException(ApplicationException e) {
        logger.error("自定义异常", e);
        return new FailResult<>(e.getApplicationEnum());
    }


    /**
     * 其他异常的处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result handleRuntimeException(Exception e) {
        logger.error("程序出错", e);
        return new FailResult<>();
    }


    /**
     * 普通参数(非 java bean)校验出错时抛出 ConstraintViolationException 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        logger.error("参数校验失败", e);
        List<Map<String, String>> list = new ArrayList<>();
        // e.getMessage() 的格式为 getUser.id: id不能为空, getUser.name: name不能为空
        String[] msgs = e.getMessage().split(", ");
        for(String msg : msgs){
            String[] fieldAndMsg = msg.split(": ");
            String field = fieldAndMsg[0].split("\\.")[1];
            String message = fieldAndMsg[1];

            Map<String, String> map = new HashMap<>(2);
            map.put("field", field);
            map.put("message", message);
            list.add(map);
        }
        return new FailResult<>(ApplicationEnum.PARAMETER_BIND_FAIL, list);
    }


    /**
     * 表单绑定到 java bean 出错时抛出 BindException 异常
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {
        logger.error("参数绑定失败", ex);
        if (ex.hasErrors()) {
            List<Map<String, String>> list = getFieldAndMessage(ex.getAllErrors());
            FailResult failResult = new FailResult<>(ApplicationEnum.PARAMETER_BIND_FAIL, list);
            return new ResponseEntity<>(failResult, headers, status);
        }
        return super.handleBindException(ex, headers, status, request);
    }


    /**
     * 将请求体解析并绑定到 java bean 时，如果出错，则抛出 MethodArgumentNotValidException 异常
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        logger.error("请求体绑定失败", ex);
        if (ex.getBindingResult().hasErrors()) {
            List<Map<String, String>> list = getFieldAndMessage(ex.getBindingResult().getAllErrors());
            FailResult failResult = new FailResult<>(ApplicationEnum.PARAMETER_BIND_FAIL, list);
            return new ResponseEntity<>(failResult, headers, status);
        } else {
            return super.handleMethodArgumentNotValid(ex, headers, status, request);
        }
    }


    /**
     * 将 ObjectError 转换成 FieldError 并取出其中的 Field 和 Message
     *
     * @param objectErrorList
     * @return
     */
    private List<Map<String, String>> getFieldAndMessage(List<ObjectError> objectErrorList) {
        List<Map<String, String>> list = new ArrayList<>();
        for (ObjectError objectError : objectErrorList) {
            Map<String, String> map = new HashMap<>(2);
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                map.put("field", fieldError.getField());
                map.put("message", fieldError.getDefaultMessage());
            } else {
                map.put("field", objectError.getObjectName());
                map.put("message", objectError.getDefaultMessage());
            }
            list.add(map);
        }
        return list;
    }
}
