package com.lifengdi.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 通知（账户）类型枚举类
 * @author 李锋镝
 * @date Create at 15:22 2018/12/7
 */
@Getter
public enum NotificationTypeEnum {

    EMAIL(1, 1, "email", 1<<0, "邮件")
    , WORK_WEI_XIN(2, 2, "work_wx", 1<<2, "企业微信")
    ;

    /**
     * 二进制值-从右数第几位
     */
    private Integer position;

    /**
     * 通知类型
     */
    private Integer notificationType;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * flag
     */
    private Integer flag;

    /**
     * 通知名称
     */
    private String name;

    NotificationTypeEnum(Integer position, Integer notificationType, String accountType, Integer flag, String name) {
        this.position = position;
        this.notificationType = notificationType;
        this.accountType = accountType;
        this.flag = flag;
        this.name = name;
    }

    public static int add(int notificationType, int flag) {
        return notificationType | flag;
    }

    public static int remove(int notificationType, int flag) {
        return notificationType & ~flag;
    }

    private static int getValueOfPosition(int position) {
        return 1 << position - 1;
    }

    /**
     * 解析第position位是否为1
     *
     * @param notificationType notificationType
     * @param position 第几位
     * @return boolean
     */
    public static boolean analysis(int notificationType, Integer position) {
        if (Objects.isNull(position)) {
            return false;
        }
        int valueOfPosition = getValueOfPosition(position);
        boolean result = false;
        if ((notificationType & valueOfPosition) > 0) {   // 按位与 大于0表示 position 位是1
            result = true;
        }
        return result;
    }

    /**
     * 根据notificationType获取NotificationTypeEnum
     * @param notificationType notificationType
     * @return NotificationTypeEnum
     */
    public static NotificationTypeEnum get(int notificationType) {
        for (NotificationTypeEnum notificationTypeEnum : values()) {
            if (notificationTypeEnum.getNotificationType() == notificationType) {
                return notificationTypeEnum;
            }
        }
        return null;
    }

    /**
     * 根据accountType获取NotificationTypeEnum
     * @param accountType accountType
     * @return NotificationTypeEnum
     */
    public static NotificationTypeEnum get(String accountType) {
        for (NotificationTypeEnum notificationTypeEnum : values()) {
            if (notificationTypeEnum.getAccountType().equals(accountType)) {
                return notificationTypeEnum;
            }
        }
        return null;
    }

    /**
     * 判断账户类型是否有效
     * @param accountType accountType
     * @return boolean
     */
    public static boolean judgeAccountType(String accountType) {
        for (NotificationTypeEnum notificationTypeEnum : values()) {
            if (notificationTypeEnum.getAccountType().equals(accountType)) {
                return true;
            }
        }
        return false;
    }

}
