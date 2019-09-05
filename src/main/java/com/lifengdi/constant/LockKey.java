package com.lifengdi.constant;

public class LockKey {

    public static final String JOB_CENTER_EXECUTE_JOB_ID = "job_center_execute_job_id_%s";

    /**
     * 单节点锁
     */
    public static final String JOB_CENTER_EXECUTE_JOB_SERVER_LOCK = "job_center_execute_job_server_id_%s";


    /**
     * 根据String.format获取key值
     * @param key key
     * @param format format
     * @return lockKey
     */
    public static String getKeyByFormat(String key, String format) {
        return String.format(format, key);
    }

    public static void main(String []args) {
        System.out.println(getKeyByFormat("123", JOB_CENTER_EXECUTE_JOB_ID));
    }

}
