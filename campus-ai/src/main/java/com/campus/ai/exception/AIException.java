package com.campus.ai.exception;

public class AIException extends RuntimeException {
    // 无参构造函数
    public AIException() {
        super();
    }

    // 带有错误消息的构造函数
    public AIException(String message) {
        super(message);
    }

    // 带有错误消息和原始异常的构造函数
    public AIException(String message, Throwable cause) {
        super(message, cause);
    }

    // 带有原始异常的构造函数
    public AIException(Throwable cause) {
        super(cause);
    }

    // 带有详细信息、原始异常、启用抑制和可覆盖栈跟踪的构造函数
    protected AIException(String message, Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}