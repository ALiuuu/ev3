package com.aliu.ev3.ev3;

/**
 * 权限结果监听
 * <p>
 * Created by liudong on 2017/9/26.
 */

public interface OnPermissionResultListener {
    /**
     * 权限返回结果
     *
     * @param isSuccess 是否获取成功
     */
    void permissionResult(boolean isSuccess);
}
