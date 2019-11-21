package com.wqlm.boot.user.handler;

import com.wqlm.boot.user.exception.ApplicationException;
import com.wqlm.boot.user.vo.result.FailResult;
import com.wqlm.boot.user.vo.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 * 如果一个异常能匹配多个 @ExceptionHandler 时，选择匹配深度最小的Exception(即最匹配的Exception)
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationException.class);

    /**
     * 处理自定义 ApplicationException 异常
     * ExceptionHandler 用于指定异常的类型
     * ResponseStatus 用于指定http的返回状态
     *
     * @param e
     * @return
     */
    @ExceptionHandler({ApplicationException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleApplicationException(ApplicationException e) {
        return new FailResult(e.getApplicationEnum());
    }


    /**
     * 其他运行时异常的处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handleRuntimeException(RuntimeException e) {
        logger.error("程序出错", e);
        return new FailResult();
    }
}
