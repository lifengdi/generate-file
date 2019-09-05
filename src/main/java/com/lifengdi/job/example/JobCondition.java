package com.lifengdi.job.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务类型
 * 
 * @author goddess
 * 2019-09-05
 */
public class JobCondition {
    /**
     * 排序字段
     */
    protected String orderByClause;

    /**
     * 过滤重复数据
     */
    protected boolean distinct;

    /**
     * 查询条件
     */
    protected List<Criteria> oredCriteria;

    /**
     * 构造查询条件
     */
    public JobCondition() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * 设置排序字段
     *
     * @param orderByClause String 排序字段
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * 获取排序字段
     * 
     * @return String
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * 设置过滤重复数据
     *
     * @param distinct boolean 是否过滤重复数据
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * 是否过滤重复数据
     * 
     * @return boolean
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * 获取当前的查询条件实例
     * 
     * @return List<Criteria>
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *
     *
     * @param criteria Criteria 过滤条件实例
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     *
     * 
     * @return Criteria
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * 创建一个查询条件
     * 
     * @return Criteria
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * 内部构建查询条件对象
     * 
     * @return Criteria
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * 清除查询条件
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     *t_job 2019-09-05
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andJobIdIsNull() {
            addCriterion("job_id is null");
            return (Criteria) this;
        }

        public Criteria andJobIdIsNotNull() {
            addCriterion("job_id is not null");
            return (Criteria) this;
        }

        public Criteria andJobIdEqualTo(String value) {
            addCriterion("job_id =", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotEqualTo(String value) {
            addCriterion("job_id <>", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThan(String value) {
            addCriterion("job_id >", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThanOrEqualTo(String value) {
            addCriterion("job_id >=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThan(String value) {
            addCriterion("job_id <", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThanOrEqualTo(String value) {
            addCriterion("job_id <=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLike(String value) {
            addCriterion("job_id like", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotLike(String value) {
            addCriterion("job_id not like", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdIn(List<String> values) {
            addCriterion("job_id in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotIn(List<String> values) {
            addCriterion("job_id not in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdBetween(String value1, String value2) {
            addCriterion("job_id between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotBetween(String value1, String value2) {
            addCriterion("job_id not between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdIsNull() {
            addCriterion("job_type_id is null");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdIsNotNull() {
            addCriterion("job_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdEqualTo(String value) {
            addCriterion("job_type_id =", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdNotEqualTo(String value) {
            addCriterion("job_type_id <>", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdGreaterThan(String value) {
            addCriterion("job_type_id >", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdGreaterThanOrEqualTo(String value) {
            addCriterion("job_type_id >=", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdLessThan(String value) {
            addCriterion("job_type_id <", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdLessThanOrEqualTo(String value) {
            addCriterion("job_type_id <=", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdLike(String value) {
            addCriterion("job_type_id like", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdNotLike(String value) {
            addCriterion("job_type_id not like", value, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdIn(List<String> values) {
            addCriterion("job_type_id in", values, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdNotIn(List<String> values) {
            addCriterion("job_type_id not in", values, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdBetween(String value1, String value2) {
            addCriterion("job_type_id between", value1, value2, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTypeIdNotBetween(String value1, String value2) {
            addCriterion("job_type_id not between", value1, value2, "jobTypeId");
            return (Criteria) this;
        }

        public Criteria andJobTitleIsNull() {
            addCriterion("job_title is null");
            return (Criteria) this;
        }

        public Criteria andJobTitleIsNotNull() {
            addCriterion("job_title is not null");
            return (Criteria) this;
        }

        public Criteria andJobTitleEqualTo(String value) {
            addCriterion("job_title =", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotEqualTo(String value) {
            addCriterion("job_title <>", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleGreaterThan(String value) {
            addCriterion("job_title >", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleGreaterThanOrEqualTo(String value) {
            addCriterion("job_title >=", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleLessThan(String value) {
            addCriterion("job_title <", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleLessThanOrEqualTo(String value) {
            addCriterion("job_title <=", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleLike(String value) {
            addCriterion("job_title like", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotLike(String value) {
            addCriterion("job_title not like", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleIn(List<String> values) {
            addCriterion("job_title in", values, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotIn(List<String> values) {
            addCriterion("job_title not in", values, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleBetween(String value1, String value2) {
            addCriterion("job_title between", value1, value2, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotBetween(String value1, String value2) {
            addCriterion("job_title not between", value1, value2, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescIsNull() {
            addCriterion("job_type_desc is null");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescIsNotNull() {
            addCriterion("job_type_desc is not null");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescEqualTo(String value) {
            addCriterion("job_type_desc =", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescNotEqualTo(String value) {
            addCriterion("job_type_desc <>", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescGreaterThan(String value) {
            addCriterion("job_type_desc >", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescGreaterThanOrEqualTo(String value) {
            addCriterion("job_type_desc >=", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescLessThan(String value) {
            addCriterion("job_type_desc <", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescLessThanOrEqualTo(String value) {
            addCriterion("job_type_desc <=", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescLike(String value) {
            addCriterion("job_type_desc like", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescNotLike(String value) {
            addCriterion("job_type_desc not like", value, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescIn(List<String> values) {
            addCriterion("job_type_desc in", values, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescNotIn(List<String> values) {
            addCriterion("job_type_desc not in", values, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescBetween(String value1, String value2) {
            addCriterion("job_type_desc between", value1, value2, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andJobTypeDescNotBetween(String value1, String value2) {
            addCriterion("job_type_desc not between", value1, value2, "jobTypeDesc");
            return (Criteria) this;
        }

        public Criteria andSendNotificationIsNull() {
            addCriterion("send_notification is null");
            return (Criteria) this;
        }

        public Criteria andSendNotificationIsNotNull() {
            addCriterion("send_notification is not null");
            return (Criteria) this;
        }

        public Criteria andSendNotificationEqualTo(Integer value) {
            addCriterion("send_notification =", value, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationNotEqualTo(Integer value) {
            addCriterion("send_notification <>", value, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationGreaterThan(Integer value) {
            addCriterion("send_notification >", value, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_notification >=", value, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationLessThan(Integer value) {
            addCriterion("send_notification <", value, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationLessThanOrEqualTo(Integer value) {
            addCriterion("send_notification <=", value, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationIn(List<Integer> values) {
            addCriterion("send_notification in", values, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationNotIn(List<Integer> values) {
            addCriterion("send_notification not in", values, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationBetween(Integer value1, Integer value2) {
            addCriterion("send_notification between", value1, value2, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andSendNotificationNotBetween(Integer value1, Integer value2) {
            addCriterion("send_notification not between", value1, value2, "sendNotification");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("created_time is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("created_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(Instant value) {
            addCriterion("created_time =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(Instant value) {
            addCriterion("created_time <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(Instant value) {
            addCriterion("created_time >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(Instant value) {
            addCriterion("created_time >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(Instant value) {
            addCriterion("created_time <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(Instant value) {
            addCriterion("created_time <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<Instant> values) {
            addCriterion("created_time in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<Instant> values) {
            addCriterion("created_time not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(Instant value1, Instant value2) {
            addCriterion("created_time between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(Instant value1, Instant value2) {
            addCriterion("created_time not between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdIsNull() {
            addCriterion("created_user_id is null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdIsNotNull() {
            addCriterion("created_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdEqualTo(String value) {
            addCriterion("created_user_id =", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotEqualTo(String value) {
            addCriterion("created_user_id <>", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdGreaterThan(String value) {
            addCriterion("created_user_id >", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("created_user_id >=", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdLessThan(String value) {
            addCriterion("created_user_id <", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdLessThanOrEqualTo(String value) {
            addCriterion("created_user_id <=", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdLike(String value) {
            addCriterion("created_user_id like", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotLike(String value) {
            addCriterion("created_user_id not like", value, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdIn(List<String> values) {
            addCriterion("created_user_id in", values, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotIn(List<String> values) {
            addCriterion("created_user_id not in", values, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdBetween(String value1, String value2) {
            addCriterion("created_user_id between", value1, value2, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserIdNotBetween(String value1, String value2) {
            addCriterion("created_user_id not between", value1, value2, "createdUserId");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameIsNull() {
            addCriterion("created_user_name is null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameIsNotNull() {
            addCriterion("created_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameEqualTo(String value) {
            addCriterion("created_user_name =", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameNotEqualTo(String value) {
            addCriterion("created_user_name <>", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameGreaterThan(String value) {
            addCriterion("created_user_name >", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("created_user_name >=", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameLessThan(String value) {
            addCriterion("created_user_name <", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameLessThanOrEqualTo(String value) {
            addCriterion("created_user_name <=", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameLike(String value) {
            addCriterion("created_user_name like", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameNotLike(String value) {
            addCriterion("created_user_name not like", value, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameIn(List<String> values) {
            addCriterion("created_user_name in", values, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameNotIn(List<String> values) {
            addCriterion("created_user_name not in", values, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameBetween(String value1, String value2) {
            addCriterion("created_user_name between", value1, value2, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserNameNotBetween(String value1, String value2) {
            addCriterion("created_user_name not between", value1, value2, "createdUserName");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneIsNull() {
            addCriterion("created_user_phone is null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneIsNotNull() {
            addCriterion("created_user_phone is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneEqualTo(String value) {
            addCriterion("created_user_phone =", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneNotEqualTo(String value) {
            addCriterion("created_user_phone <>", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneGreaterThan(String value) {
            addCriterion("created_user_phone >", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("created_user_phone >=", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneLessThan(String value) {
            addCriterion("created_user_phone <", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneLessThanOrEqualTo(String value) {
            addCriterion("created_user_phone <=", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneLike(String value) {
            addCriterion("created_user_phone like", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneNotLike(String value) {
            addCriterion("created_user_phone not like", value, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneIn(List<String> values) {
            addCriterion("created_user_phone in", values, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneNotIn(List<String> values) {
            addCriterion("created_user_phone not in", values, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneBetween(String value1, String value2) {
            addCriterion("created_user_phone between", value1, value2, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andCreatedUserPhoneNotBetween(String value1, String value2) {
            addCriterion("created_user_phone not between", value1, value2, "createdUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIsNull() {
            addCriterion("updated_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIsNotNull() {
            addCriterion("updated_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeEqualTo(Instant value) {
            addCriterion("updated_time =", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotEqualTo(Instant value) {
            addCriterion("updated_time <>", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeGreaterThan(Instant value) {
            addCriterion("updated_time >", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeGreaterThanOrEqualTo(Instant value) {
            addCriterion("updated_time >=", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeLessThan(Instant value) {
            addCriterion("updated_time <", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeLessThanOrEqualTo(Instant value) {
            addCriterion("updated_time <=", value, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeIn(List<Instant> values) {
            addCriterion("updated_time in", values, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotIn(List<Instant> values) {
            addCriterion("updated_time not in", values, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeBetween(Instant value1, Instant value2) {
            addCriterion("updated_time between", value1, value2, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedTimeNotBetween(Instant value1, Instant value2) {
            addCriterion("updated_time not between", value1, value2, "updatedTime");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdIsNull() {
            addCriterion("updated_user_id is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdIsNotNull() {
            addCriterion("updated_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdEqualTo(String value) {
            addCriterion("updated_user_id =", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdNotEqualTo(String value) {
            addCriterion("updated_user_id <>", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdGreaterThan(String value) {
            addCriterion("updated_user_id >", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("updated_user_id >=", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdLessThan(String value) {
            addCriterion("updated_user_id <", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdLessThanOrEqualTo(String value) {
            addCriterion("updated_user_id <=", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdLike(String value) {
            addCriterion("updated_user_id like", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdNotLike(String value) {
            addCriterion("updated_user_id not like", value, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdIn(List<String> values) {
            addCriterion("updated_user_id in", values, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdNotIn(List<String> values) {
            addCriterion("updated_user_id not in", values, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdBetween(String value1, String value2) {
            addCriterion("updated_user_id between", value1, value2, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserIdNotBetween(String value1, String value2) {
            addCriterion("updated_user_id not between", value1, value2, "updatedUserId");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameIsNull() {
            addCriterion("updated_user_name is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameIsNotNull() {
            addCriterion("updated_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameEqualTo(String value) {
            addCriterion("updated_user_name =", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameNotEqualTo(String value) {
            addCriterion("updated_user_name <>", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameGreaterThan(String value) {
            addCriterion("updated_user_name >", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("updated_user_name >=", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameLessThan(String value) {
            addCriterion("updated_user_name <", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameLessThanOrEqualTo(String value) {
            addCriterion("updated_user_name <=", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameLike(String value) {
            addCriterion("updated_user_name like", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameNotLike(String value) {
            addCriterion("updated_user_name not like", value, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameIn(List<String> values) {
            addCriterion("updated_user_name in", values, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameNotIn(List<String> values) {
            addCriterion("updated_user_name not in", values, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameBetween(String value1, String value2) {
            addCriterion("updated_user_name between", value1, value2, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserNameNotBetween(String value1, String value2) {
            addCriterion("updated_user_name not between", value1, value2, "updatedUserName");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneIsNull() {
            addCriterion("updated_user_phone is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneIsNotNull() {
            addCriterion("updated_user_phone is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneEqualTo(String value) {
            addCriterion("updated_user_phone =", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneNotEqualTo(String value) {
            addCriterion("updated_user_phone <>", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneGreaterThan(String value) {
            addCriterion("updated_user_phone >", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("updated_user_phone >=", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneLessThan(String value) {
            addCriterion("updated_user_phone <", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneLessThanOrEqualTo(String value) {
            addCriterion("updated_user_phone <=", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneLike(String value) {
            addCriterion("updated_user_phone like", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneNotLike(String value) {
            addCriterion("updated_user_phone not like", value, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneIn(List<String> values) {
            addCriterion("updated_user_phone in", values, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneNotIn(List<String> values) {
            addCriterion("updated_user_phone not in", values, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneBetween(String value1, String value2) {
            addCriterion("updated_user_phone between", value1, value2, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andUpdatedUserPhoneNotBetween(String value1, String value2) {
            addCriterion("updated_user_phone not between", value1, value2, "updatedUserPhone");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusIsNull() {
            addCriterion("execute_status is null");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusIsNotNull() {
            addCriterion("execute_status is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusEqualTo(String value) {
            addCriterion("execute_status =", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusNotEqualTo(String value) {
            addCriterion("execute_status <>", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusGreaterThan(String value) {
            addCriterion("execute_status >", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusGreaterThanOrEqualTo(String value) {
            addCriterion("execute_status >=", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusLessThan(String value) {
            addCriterion("execute_status <", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusLessThanOrEqualTo(String value) {
            addCriterion("execute_status <=", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusLike(String value) {
            addCriterion("execute_status like", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusNotLike(String value) {
            addCriterion("execute_status not like", value, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusIn(List<String> values) {
            addCriterion("execute_status in", values, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusNotIn(List<String> values) {
            addCriterion("execute_status not in", values, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusBetween(String value1, String value2) {
            addCriterion("execute_status between", value1, value2, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andExecuteStatusNotBetween(String value1, String value2) {
            addCriterion("execute_status not between", value1, value2, "executeStatus");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Integer value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Integer value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Integer value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Integer value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Integer value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Integer value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Integer> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Integer> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Integer value1, Integer value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Integer value1, Integer value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
            return (Criteria) this;
        }
    }

    /**
     * t_job
     * @author goddess 2019-09-05
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     *t_job 2019-09-05
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}