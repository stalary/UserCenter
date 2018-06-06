package com.stalary.usercenter.handle;

import com.stalary.usercenter.data.entity.User;

/**
 * UserContextHolder
 *
 * @author lirongqian
 * @since 26/12/2017
 */
public class UserContextHolder {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static User get() {
        return userThreadLocal.get();
    }

    public static void set(User user) {
        userThreadLocal.set(user);
    }

    public static void remove() {
        userThreadLocal.remove();
    }
}