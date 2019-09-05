/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost
 Source Database       : job_center

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : utf-8

 Date: 04/03/2019 13:38:51 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_file`
-- ----------------------------
CREATE TABLE `t_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `obj_id` varchar(255) NOT NULL,
  `obj_type` int(11) DEFAULT NULL,
  `file_url` varchar(500) DEFAULT NULL,
  `file_cdn` varchar(500) DEFAULT NULL COMMENT '文件CDN路径',
  `file_type` varchar(255) DEFAULT NULL,
  `file_analysis_model` varchar(255) DEFAULT NULL COMMENT '文件解析模型',
  `status` varchar(255) DEFAULT NULL,
  `deleted` int(1) DEFAULT '0' COMMENT '是否删除',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `obj_id` (`obj_id`,`obj_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件';

-- ----------------------------
--  Table structure for `t_job`
-- ----------------------------
CREATE TABLE `t_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `job_id` varchar(255) NOT NULL COMMENT '任务ID',
  `job_type_id` varchar(255) NOT NULL COMMENT 'job_type_id',
  `job_title` varchar(255) DEFAULT NULL COMMENT '任务标题',
  `job_type_desc` varchar(500) DEFAULT NULL COMMENT '任务类型描述',
  `send_notification` int(1) DEFAULT '0' COMMENT '是否发送通知 0-否，1-是',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user_id` varchar(255) DEFAULT NULL COMMENT '创建人ID',
  `created_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_user_phone` varchar(255) DEFAULT NULL COMMENT '创建人手机号',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  `updated_user_id` varchar(255) DEFAULT NULL COMMENT '最后一次修改人ID',
  `updated_user_name` varchar(255) DEFAULT NULL COMMENT '最后一次修改人',
  `updated_user_phone` varchar(255) DEFAULT NULL COMMENT '最后一次修改人phone',
  `execute_status` varchar(255) DEFAULT NULL COMMENT '任务执行状态',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `deleted` int(1) DEFAULT '0' COMMENT '是否被删除 0-否 1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `job_id` (`job_id`),
  KEY `created_user_id` (`created_user_id`),
  KEY `job_type_id` (`job_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务类型';

-- ----------------------------
--  Table structure for `t_job_log`
-- ----------------------------
CREATE TABLE `t_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `job_id` varchar(255) NOT NULL COMMENT '任务ID',
  `job_type_id` varchar(255) DEFAULT NULL,
  `operate_type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `operate_content` varchar(1000) DEFAULT NULL COMMENT '操作内容',
  `operate_result` varchar(255) DEFAULT NULL COMMENT '操作结果',
  `operator_id` varchar(255) DEFAULT NULL COMMENT '操作人的ID',
  `operator_name` varchar(255) DEFAULT NULL,
  `operator_phone` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `job_id` (`job_id`),
  KEY `operate_type` (`operate_type`),
  KEY `job_type_id` (`job_type_id`),
  KEY `operator_phone` (`operator_phone`),
  KEY `operator_id` (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务操作日志';

-- ----------------------------
--  Table structure for `t_job_type`
-- ----------------------------
CREATE TABLE `t_job_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `job_type_id` varchar(255) NOT NULL COMMENT '任务ID',
  `job_type_desc` varchar(500) DEFAULT NULL COMMENT '任务类型描述',
  `generate_file` int(1) DEFAULT '0' COMMENT '是否生成文件 0-否，1-是',
  `upload_file` int(1) DEFAULT '0' COMMENT '是否上传文件 0-否 1-是',
  `send_notification` int(11) DEFAULT '0' COMMENT '是否发送通知 0-否，1-是',
  `notification_type` bigint(20) DEFAULT '0' COMMENT '通知类型 二进制',
  `request_url` varchar(500) NOT NULL COMMENT '请求接口地址',
  `request_method` varchar(255) DEFAULT 'get' COMMENT '请求方式',
  `enable_access` int(1) DEFAULT '0' COMMENT '是否开启允许操作人 0-否，1-是',
  `response_data_entity` varchar(255) DEFAULT NULL COMMENT '接口响应体数据结构',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user_id` varchar(255) DEFAULT NULL COMMENT '创建人ID',
  `created_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_user_phone` varchar(255) DEFAULT NULL COMMENT '创建人手机号',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  `updated_user_id` varchar(255) DEFAULT NULL COMMENT '最后一次修改人ID',
  `updated_user_name` varchar(255) DEFAULT NULL COMMENT '最后一次修改人',
  `updated_user_phone` varchar(255) DEFAULT NULL COMMENT '最后一次修改人phone',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `deleted` int(1) DEFAULT '0' COMMENT '是否被删除 0-否 1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `job_id` (`job_type_id`),
  KEY `request_method` (`request_method`),
  KEY `created_user_id` (`created_user_id`),
  KEY `created_user_phone` (`created_user_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务类型';

-- ----------------------------
--  Table structure for `t_job_type_log`
-- ----------------------------
CREATE TABLE `t_job_type_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `job_type_id` varchar(255) DEFAULT NULL COMMENT '任务类型ID',
  `operate_type` varchar(255) DEFAULT NULL COMMENT '操作类型',
  `old_content` text COMMENT '操作前内容',
  `new_content` text COMMENT '操作后内容',
  `operate_desc` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `operator_id` varchar(255) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(255) DEFAULT NULL COMMENT '操作人姓名',
  PRIMARY KEY (`id`),
  KEY `job_type_id` (`job_type_id`),
  KEY `operate_type` (`operate_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务类型操作日志';

-- ----------------------------
--  Table structure for `t_request_param`
-- ----------------------------
CREATE TABLE `t_request_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `obj_id` varchar(255) NOT NULL COMMENT '任务ID',
  `obj_type` int(11) DEFAULT NULL,
  `param_name` varchar(200) NOT NULL COMMENT '参数名称',
  `param_title` varchar(500) DEFAULT NULL COMMENT '参数title',
  `param_type` varchar(255) DEFAULT NULL COMMENT '参数类型',
  `param_value` varchar(500) DEFAULT NULL COMMENT '参数默认值',
  `param_format` varchar(255) DEFAULT NULL COMMENT '参数格式（主要针对datetime类型的）',
  `required` int(1) DEFAULT '0' COMMENT '是否必填 0-否，1-是',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user_id` varchar(255) DEFAULT NULL COMMENT '创建人ID',
  `created_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `created_user_phone` varchar(255) DEFAULT NULL COMMENT '创建人手机号',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  `updated_user_id` varchar(255) DEFAULT NULL COMMENT '最后一次修改人ID',
  `updated_user_name` varchar(255) DEFAULT NULL COMMENT '最后一次修改人',
  `updated_user_phone` varchar(255) DEFAULT NULL COMMENT '最后一次修改人phone',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `deleted` int(1) DEFAULT '0' COMMENT '是否被删除 0-否 1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `obj_id` (`obj_id`,`obj_type`,`param_name`),
  KEY `obj_id_2` (`obj_id`,`obj_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='请求参数';

SET FOREIGN_KEY_CHECKS = 1;
