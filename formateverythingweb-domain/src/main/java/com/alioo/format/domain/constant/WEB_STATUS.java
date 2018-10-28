package com.alioo.format.domain.constant;

/**
 * @author yujiayuan
 *         Date: 2016年7月14日 下午7:47:26
 */
/**
 * 接口请求
 */
public enum WEB_STATUS {

    SUCCESS(0, "操作成功"),
    NOT_LOGIN(1001, "未登录"),
    PERMITION_DENY(1002, "抱歉,您没有权限,请联系管理员开通权限"),
    PARAM_ERROR(1003, "参数传递错误"),
    LOCALE_DENY(1004, "不支持该语种"),
    UPLOADFILE_EMPTY_ERROR(1005, "上传文件不能为空"),
    PARAM_MISSING(1006, "请传入参数"),
    FILE_NAME_DUPLICATION(1007,"文件名重复"),
    UPLOADFILE_SUFFIX_ERROR(1008,"文件格式不支持"),

    INTERNAL_ERROR(2000, "系统内部错误"),
    RECORD_NOT_FOUND(2001, "记录不存在"),
    DUPLICATE(2002, "记录重复添加"),
    USER_PERMITION_DENY(2004, "抱歉该用户并不是面板成员,请先为他添加权限"),
    FIRST_MANAGER_DENY(2005, "抱歉,初始管理员的权限不可修改"),
    GUEST_ASSIGN_TASK(2010, "当前成员已被分配任务，无法切换至访客身份"),

    ISSUE_LIST_EMPTY(2006, "参数错误，列不存在！"),
    ISSUE_BOARD_EMPTY(2007, "参数错误，面板不存在！"),

    API_AUTH_ERROR(2009, "你没有该接口的访问权限，请联系管理员开通！"),
    MANAGER_REQUIRED(2010, "抱歉,管理员不可以为空"),

    ;

    public int code;

    private String msg;

    WEB_STATUS(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}