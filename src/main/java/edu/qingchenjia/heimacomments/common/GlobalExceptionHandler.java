package edu.qingchenjia.heimacomments.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理器方法
     * 用于处理控制器层未捕获的异常，统一返回给客户端错误信息
     *
     * @param e 异常对象，类型为Exception，包括所有未被捕获的异常
     * @return 返回一个封装了错误信息的R对象，告知客户端操作失败
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        // 打印异常堆栈跟踪信息，便于开发者定位问题
        e.printStackTrace();
        // 返回操作失败的信息给客户端
        return R.fail("错误操作");
    }
}
