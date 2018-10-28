package com.alioo.exception;

import com.alioo.format.domain.base.RestApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午5:18
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private void logErrorInfo(HttpServletRequest httpRequest, Exception ex, HttpStatus httpStatus) {
        logger.error("Error Request:\t{} {}", httpRequest.getMethod(), httpRequest.getServletPath());
        logger.error("Url Params: \t{}", getParams(httpRequest));
        if (StringUtils.containsIgnoreCase(httpRequest.getContentType(), "json")) {
            logger.error("Body Params: \t{}", getParams(httpRequest));
        }
        logger.error("Exception catched:", ex);
    }


    // 400
    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class,
        JSONException.class, HttpRequestMethodNotSupportedException.class,
        MissingServletRequestParameterException.class,
        InvalidParameterException.class,ParamErrorException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiResponse handleBadRequestException(HttpServletRequest request, Exception ex) {
        logErrorInfo(request, ex, HttpStatus.BAD_REQUEST);
        return RestApiResponse.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }


    // 401
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public RestApiResponse handleUnauthorizedException(HttpServletRequest request, NotLoginException ex) {
        logErrorInfo(request, ex, HttpStatus.UNAUTHORIZED);
        return RestApiResponse.fail(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    // 403
    @ExceptionHandler(OperationForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestApiResponse handleOperationForbiddenException(HttpServletRequest request,
                                                             OperationForbiddenException ex) {
        logErrorInfo(request, ex, HttpStatus.FORBIDDEN);
        return RestApiResponse.fail(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestApiResponse handleResourceNotFoundException(HttpServletRequest request,
                                                           ResourceNotFoundException ex) {
        logErrorInfo(request, ex, HttpStatus.NOT_FOUND);
        return RestApiResponse.fail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }


    // 500
    @ExceptionHandler({AppExecutionException.class, RuntimeException.class, Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestApiResponse handleAppExecutionException(HttpServletRequest request, Exception ex) {
        logErrorInfo(request, ex, HttpStatus.INTERNAL_SERVER_ERROR);
        String message=ex.getMessage();
        if(ex instanceof NullPointerException){
            message = "服务器吃撑了，请稍候再试一下吧[000000]";
        };
        return RestApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }


    /**
     * 获取request body 中的json格式参数。
     * 如果取过一次request的inputstream，再取会抛出异常，建议使用requestWrapper。
     */
    private String getParams(HttpServletRequest httpRequest) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = httpRequest.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            logger.warn("Params have been read by @RequestBody");
        }
        return builder.toString();
    }
}
