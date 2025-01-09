package edu.qingchenjia.heimacomments.common;

import edu.qingchenjia.heimacomments.dto.UserDto;

public class BaseContext {
    private static final ThreadLocal<UserDto> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     * 该方法用于将用户信息保存到ThreadLocal中，以实现线程安全的用户上下文传递
     * 主要应用于跨多个方法或组件的情况下，保持用户信息的唯一性和一致性
     *
     * @param userDto 用户数据传输对象，包含用户的相关信息
     */
    public static void setCurrentUser(UserDto userDto) {
        threadLocal.set(userDto);
    }

    /**
     * 获取当前线程的用户信息
     * <p>
     * 本方法通过ThreadLocal来存储每个线程独有的用户信息，确保用户信息在多线程环境下不会被共享
     * 这样做可以避免传统方式（如使用HttpSession）在并发情况下可能导致的用户信息错乱问题
     *
     * @return UserDto 当前线程关联的用户信息对象如果当前线程没有绑定用户信息，则返回null
     */
    public static UserDto getCurrentUser() {
        return threadLocal.get();
    }

    /**
     * 移除当前线程的用户信息
     * <p>
     * 本方法旨在从当前线程中移除之前存储的用户信息主要用于线程安全的上下文管理
     * 通过移除ThreadLocal中的用户信息，可以避免内存泄漏问题，特别是在使用线程池时
     */
    public static void removeCurrentUser() {
        threadLocal.remove();
    }
}
