package com.alioo.format.domain.base;


import com.alioo.format.domain.constant.WEB_STATUS;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ray
 * @date 10/17/15
 */
@ApiModel(value = "接口响应基础实体", description = "接口响应的基础信息，所有的响应接口都包括该类中的字段")
public class Response {

    @ApiModelProperty(value = "状态码", name = "code", example = "0")
    private int code = 0;

    @ApiModelProperty(value = "状态信息", name = "message", example = "操作成功")
    private String message;

    @ApiModelProperty(value = "错误信息集合", name = "errorMgs", example = "0")
    private List<String> errorMgs = new ArrayList<String>();

    public Response() {
    }

    public Response(int code, Object message) {
        this.code = code;
        if (null != message && (this.message = message.toString()) != null) {
            ;
        }
    }

    public Response(WEB_STATUS ws) {
        this.code = ws.getCode();
        this.message = ws.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrorMgs() {
        return errorMgs.size() > 1 ? errorMgs.subList(errorMgs.size() - 2, errorMgs.size() - 1) : errorMgs;
    }

    public void setErrorMgs(List<String> errorMgs) {
        this.errorMgs = errorMgs;
    }

    public void setMessage(Object message) {
        if (null != message && (this.message = message.toString()) != null) {
            ;
        }
    }


    public void addErrorMessage(Object message) {
        code = WEB_STATUS.INTERNAL_ERROR.code;
        errorMgs.add(null == message ? null : message.toString());
    }

    public void addErrorMessage(Integer code, Object message) {
        this.code = code;
        errorMgs.add(null == message ? null : message.toString());
    }

    public static Response returnFailed(String errorMessage) {
        return new Response(-1, errorMessage);
    }

    public static Response returnSuccess() {
        return new Response(0, "SUCCESS");
    }

    public static Response returnFailed(int code, Object errorMessage) {
        return new Response(code, errorMessage);
    }
}