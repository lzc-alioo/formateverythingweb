mysql -h 114.115.211.71 -P 13306 -u root -paabbcc05** -D alioo

create database alioo  default character set 'utf8';

use alioo

drop table article;
CREATE TABLE `article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(500) DEFAULT '' COMMENT '标题',
  `content` longblob DEFAULT '' COMMENT '内容，属于富文本',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ;


insert into Article(title,content,readCount,commentCount,csdnLink,graspTime,create_time,create_pin,update_time,update_pin)
values ('aaaa','bbbbbb',4,444,'http://blog.csdn.net/hl_java/article/details/78344667','20171028121212',
        now(),'lzc',now(),'llzzcc');








