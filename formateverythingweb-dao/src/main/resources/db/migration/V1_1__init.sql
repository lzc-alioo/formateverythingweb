/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : 127.0.0.1
 Source Database       : luodi_dev

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : utf-8

 Date: 07/09/2018 17:20:20 PM
*/

SET NAMES utf8;

-- -- ----------------------------
-- --  Table structure for `project`
-- -- ----------------------------
-- DROP TABLE IF EXISTS `project`;
-- CREATE TABLE `project` (
--   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
--   `name` varchar(1024) DEFAULT NULL COMMENT '项目名称',
--   `description` text COMMENT '项目描述',
--   `business_direction` int(11) DEFAULT NULL COMMENT '业务方向',
--   `priority` int(11) DEFAULT NULL COMMENT '优先级',
--   `budget` double(25,2) DEFAULT NULL COMMENT '预算',
--   `state` int(11) DEFAULT NULL COMMENT '状态',
--   `status` int(11) DEFAULT '0' COMMENT '项目状态0正常-1删除',
--   `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
--   `ctime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   `mtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
--   PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(500) DEFAULT '' COMMENT '标题',
  `content` longblob COMMENT '内容，属于富文本',
  `readCount` int(11) DEFAULT '0' COMMENT '在csdn阅读量',
  `commentCount` int(11) DEFAULT '0' COMMENT '在csdn评论量',
  `csdnLink` varchar(2000) DEFAULT '' COMMENT '在csdn中的原始链接',
  `graspTime` varchar(14) DEFAULT '' COMMENT '抓取时间戳，格式yyyyMMddHHmmss',
  `contentType` int(1) DEFAULT '1' COMMENT '内容类型1：原创；2，转载',
  `contentDesc` varchar(2000) DEFAULT '' COMMENT '内容简介',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本号',
  `yn` tinyint(4) DEFAULT '0' COMMENT '删除标示',
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8