package cn.caimatou.canting.base;

import java.io.Serializable;

public class GLBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 是否处理成功.
     */
    private boolean success;

    /**
     * 错误码.
     */
    private int errorCode;
    /**
     * 出错信息.
     */
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "errorCode=" + errorCode + ",success=" + success + ",message=" + message;
    }
}
