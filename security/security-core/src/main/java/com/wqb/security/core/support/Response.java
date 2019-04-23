package com.wqb.security.core.support;

/**
 * 自定义响应消息体
 *
 * @author Shoven
 * @since  2018-11-09
 */
public class Response<T> {

    /**
     * 成功标志
     */
    private Boolean success = true;

    /**
     * 响应业务状态
     */
    private Integer status = 200;

    /**
     * 响应消息
     */
    private String message = "ok";

    /**
     * 时间撮
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 响应中的数据
     */
    private T data = null;

    public Response() {
    }

    public Response(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Response<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Response<T> setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Response<T> setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Response{");
        sb.append("success=").append(success);
        sb.append(", status=").append(status);
        sb.append(", message='").append(message).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
