package com.wqb.supports.exceptions;

import com.wqb.supports.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器层异常处理器
 *
 * @author Shoven
 * @since  2018-11-09
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private final static String DEFAULT_MESSAGE = "Oops! Something went wrong. Please try again later";


    /**
     * 处理数据绑定校验异常
     *
     * @param e BindException
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity handleBindException(BindException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<Map<String, Object>> errors = new ArrayList<>();

        for (FieldError fielderror : fieldErrors) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("field", fielderror.getField());
            error.put("rejectedValue", String.valueOf(fielderror.getRejectedValue()));
            error.put("description", fielderror.getDefaultMessage());
            errors.add(error);
        }

        return ResponseUtils.badRequest("Bad Request, Field Error", errors);
    }

    /**
     * 处理方法参数绑定异常
     *
     * @param e MethodArgumentTypeMismatchException
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity handleMethodArgumentBindException(MethodArgumentTypeMismatchException e) {
        String errorMsg = null;
        if (e.getCause() != null) {
            if (e.getCause() instanceof NumberFormatException) {
                errorMsg = "：require number";
            }
        }

        logger.error(e.getMessage(), e);
        return ResponseUtils.badRequest("Bad Request, Argument type mismatch: " + errorMsg);
    }

    /**
     * 处理业务异常
     *
     * @param e BizException
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseEntity handleBizException(BizException e) {
        String errorMsg = e.getMessage() != null ? e.getMessage() : DEFAULT_MESSAGE;
        return ResponseUtils.unprocesable(errorMsg);
    }


    /**
     * 处理url未匹配
     *
     * @param e Exception
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity handleNoHandlerException(NoHandlerFoundException e) {
        return ResponseUtils.notFound();
    }

    /**
     * 处理数据访问异常
     *
     * @param e Exception
     * @return
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ResponseEntity handleDataAccessException(DataAccessException e) {
        logger.error(e.getMessage(), e);
        return ResponseUtils.error(DEFAULT_MESSAGE);
    }

    /**
     * 处理上传限制异常
     *
     * @param e Exception
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        String errorMsg = "Upload file size more than "+ getSize(e.getMaxUploadSize()) +" limit";
        return ResponseUtils.error(errorMsg);
    }

    /**
     * 处理系统级异常
     *
     * @param e Exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity handleException(Exception e) {
        String errorMsg = e.getMessage() != null ? "Internal Server Error: " + e.getMessage() : DEFAULT_MESSAGE;
        logger.error(e.getMessage(), e);
        return ResponseUtils.error(errorMsg);
    }

    /**
     * 获取文件大小
     *
     * @param b  字节
     * @return
     */
    private String getSize(long b) {
        if (b == 0) {
            return "0B";
        } else if (b < 1024) {
            return b +"B";
        } else if (b > 1024 && b < 1024 * 1024) {
            return b / 1024 + "KB";
        } else {
            return b / 1024 * 1024 + "MB";
        }
    }
}
