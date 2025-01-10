package edu.qingchenjia.heimacomments.common;

import lombok.Data;

@Data
public class R<T> {
    private Boolean success;

    private String errorMsg;

    private T data;

    private Long total;

    /**
     * 创建一个表示成功响应的对象
     * <p>
     * 此方法用于生成一个简单的成功响应对象，常用于API响应结果的封装
     * 它创建一个R类型的对象，将成功标志设置为true，表示操作成功
     *
     * @return R 成功响应对象
     */
    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.success = true;
        return r;
    }

    /**
     * 创建一个包含指定数据的成功响应对象
     * 此方法用于快速构建一个表示成功操作的响应对象，并附带相关数据
     * 它封装了响应对象的创建过程，简化了调用方的代码
     *
     * @param data 响应中携带的数据，可以是任意类型
     * @return 返回一个包含指定数据的成功响应对象
     */
    public static <T> R<T> ok(T data) {
        // 创建一个新的响应对象实例
        R<T> r = new R<>();
        // 将方法参数中的数据赋值给响应对象的data字段
        r.data = data;
        // 返回构建好的响应对象
        return r;
    }

    /**
     * 创建一个包含指定数据和总记录数的R对象
     * 该方法主要用于快速构建一个R对象，用于返回分页查询的结果
     *
     * @param data  查询到的数据列表，可以是任意类型的List
     * @param total 总记录数，用于分页统计
     * @return 返回一个填充了数据和总记录数的R对象
     */
    public static <T> R<T> ok(T data, Long total) {
        R<T> r = new R<>();
        r.data = data;
        r.total = total;
        return r;
    }

    /**
     * 创建一个表示失败响应的对象，并设置错误信息
     *
     * @param errorMsg 错误信息，用于描述失败的原因
     * @return 返回一个包含错误信息的R对象，表示失败的响应
     */
    public static <T> R<T> fail(String errorMsg) {
        R<T> r = new R<>();
        r.errorMsg = errorMsg;
        return r;
    }
}
