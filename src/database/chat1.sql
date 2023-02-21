SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for tbl_accountinfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_accountinfo`;
CREATE TABLE `tbl_accountinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `account` varchar(255) CHARACTER SET utf8   NOT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8  NOT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '昵称（中文显示名）',
  `type` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '账号类型（-1:超级用户，0:管理员，不能登录客户端；1：操作员，可登录客户端，运营后台暂时没有权限）（类型后续可扩展）',
  `cid` int(11) DEFAULT NULL COMMENT '所属的客户id',
  `level` int(11) DEFAULT '1' COMMENT '账号等级',
  `state` int(11) DEFAULT '1' COMMENT '当前账号状态1正常2禁用',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4  COMMENT='账号表';

-- ----------------------------
-- Records of tbl_accountinfo
-- ----------------------------
INSERT INTO `tbl_accountinfo` VALUES ('1', 'root', '123456', 'root', '-1', null, '1', '1', '2019-03-11 09:49:24');
INSERT INTO `tbl_accountinfo` VALUES ('5', 'admin', '123456', 'admin', '0', '7', '1', '1', '2019-03-11 09:49:24');
INSERT INTO `tbl_accountinfo` VALUES ('6', 'pctest', '123456', 'pctest', '1', '7', '1', '1', '2019-03-11 09:49:22');

-- ----------------------------
-- Table structure for tbl_commontaginfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_commontaginfo`;
CREATE TABLE `tbl_commontaginfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL COMMENT '客户id',
  `name` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '标签名称',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='术语分类表';

 
-- ----------------------------
-- Table structure for tbl_commontermtype
-- ----------------------------
DROP TABLE IF EXISTS `tbl_commontermtype`;
CREATE TABLE `tbl_commontermtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL COMMENT '客户id',
  `name` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '术语分类名称',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='术语分类表';


-- ----------------------------
-- Table structure for tbl_commonterminfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_commonterminfo`;
CREATE TABLE `tbl_commonterminfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL COMMENT '客户id',
  `tid` int(11) DEFAULT NULL COMMENT '术语分类id',
  `ctype` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '类型',
  `name` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '标题',
  `content` varchar(512) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4  COMMENT='术语表';

-- ----------------------------
-- Table structure for tbl_customerinfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_customerinfo`;
CREATE TABLE `tbl_customerinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '客户id',
  `suppliername` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '客户名称（中文显示名）',
  `account_num` int(11) DEFAULT NULL COMMENT '最大账号（操作员）数量（依客户等级而定）',
  `device_num` int(11) DEFAULT NULL COMMENT '最大设备数量（依客户等级而定）',
  `validity` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '服务期限',
  `state` int(11) DEFAULT NULL COMMENT '当前客户状态',
  `admin` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '管理员账号',
  `contact` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '联系人姓名',
  `phone` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '联系人电话',
  `description` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4  COMMENT='客户表';

-- ----------------------------
-- Records of tbl_customerinfo
-- ----------------------------
INSERT INTO `tbl_customerinfo` VALUES ('7', '售后客服部', '100', '1000', '2018-09-20 14:52:47', '1', 'admin', 'admin', '', '', '2018-09-20 14:52:47');


-- ----------------------------
-- Table structure for tbl_wx_accountinfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_accountinfo`;
CREATE TABLE `tbl_wx_accountinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wechatid` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '微信id',
  `wechatno` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '微信账号',
  `wechatnick` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '微信昵称',
  `gender` int(11) DEFAULT NULL COMMENT '性别',
  `avatar` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '头像url',
  `country` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '国家',
  `province` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '省份',
  `city` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '城市',
  `isonline` int(11) DEFAULT '1' COMMENT '是否在线   0上线   1下线',
  `deviceid` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '微信登录的设备',
  `cid` int(11) DEFAULT NULL COMMENT '客户id',
  `groupid` int(11) DEFAULT NULL COMMENT '分组id',
  `devnickname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '设备昵称',
  `brand` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '手机品牌',
  `module` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '手机型号',
  `snumber` int(11) DEFAULT NULL COMMENT '排序id',
  `accountid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Pc客户端正在操作该微信的操作员账号id',
  `login_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `deviceid_wechatid` (`deviceid`,`wechatid`) USING BTREE,
  UNIQUE KEY `deviceid` (`deviceid`) USING BTREE,
  UNIQUE KEY `wechatid` (`wechatid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8mb4  COMMENT='设备账号表';


-- ----------------------------
-- Records of tbl_wx_accountinfo
-- ----------------------------

-- ----------------------------
-- Table structure for tbl_wx_devicegroup
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_devicegroup`;
CREATE TABLE `tbl_wx_devicegroup` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL COMMENT '客户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组名',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4  COMMENT='设备分组表';


-- ----------------------------
-- Table structure for tbl_wx_contactinfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_contactinfo`;
CREATE TABLE `tbl_wx_contactinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL COMMENT '所属客户id',
  `wechatid` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '所属的微信id',
  `friendid` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '联系人微信id',
  `friend_wechatno` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '联系人微信账号',
  `nickname` varchar(512) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '联系人微信昵称',
  `remark` varchar(512) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '备注',
  `gender` int(255) DEFAULT NULL COMMENT '性别',
  `avatar` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '头像url',
  `country` varchar(512) CHARACTER SET utf8  DEFAULT NULL COMMENT '国家',
  `province` varchar(512) CHARACTER SET utf8  DEFAULT NULL COMMENT '省份',
  `city` varchar(512) CHARACTER SET utf8  DEFAULT NULL COMMENT '城市',
  `memo` varchar(512) CHARACTER SET utf8  DEFAULT NULL COMMENT '备注',
  `type` int(11) DEFAULT NULL COMMENT '0通讯录 1群聊',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据更新时间',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cid_wechatid_friendid` (`cid`,`wechatid`,`friendid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18346 DEFAULT CHARSET=utf8mb4  COMMENT='通讯录表';

-- ----------------------------
-- Records of tbl_wx_contactinfo
-- ----------------------------


-- ----------------------------
-- Table structure for tbl_version_controls
-- ----------------------------
DROP TABLE IF EXISTS `tbl_version_controls`;
CREATE TABLE `tbl_version_controls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL COMMENT 'cid',
  `version` varchar(255) DEFAULT NULL COMMENT '版本名称',
  `vernumber` int(11) DEFAULT NULL COMMENT '版本号',
  `packagename` varchar(255) DEFAULT NULL COMMENT '包名称',
  `packageurl` varchar(255) DEFAULT NULL COMMENT '软件包地址',
  `flag` int(11) DEFAULT NULL COMMENT '是否推送   0已推送   -1未推送',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4  COMMENT='app版本控制表';



-- ----------------------------
-- Table structure for tbl_wx_message
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_message`;
CREATE TABLE `tbl_wx_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `wechatId` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '当前微信id',
  `wechatno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `wechatnick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `friendId` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '好友微信id',
  `friendno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `friendnick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `msgSvrId` varchar(255) DEFAULT NULL COMMENT '唯一消息id',
  `isSend` varchar(255) DEFAULT NULL COMMENT '是否发送',
  `contentType` int(11) DEFAULT NULL COMMENT '消息类型',
  `content` varchar(5000) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '消息内容',
  `type` int(11) DEFAULT NULL COMMENT '0个人 1群聊',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37206 DEFAULT CHARSET=utf8mb4  COMMENT='微信消息表';

-- ----------------------------
-- Records of tbl_wx_message
-- ----------------------------


-- ----------------------------
-- Table structure for tbl_timetask
-- ----------------------------
DROP TABLE IF EXISTS `tbl_timetask`;
CREATE TABLE `tbl_timetask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) DEFAULT NULL COMMENT '操作员id',
  `wechatid` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '微信id',
  `cid` int(11) DEFAULT NULL,
  `tasktype` int(11) DEFAULT NULL COMMENT '任务类型1群发消息2发朋友圈',
  `state` int(11) DEFAULT '1' COMMENT '状态  -1暂停     0已完成    1开启中     2取消',
  `execute_time` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT '执行时间',
  `restype` int(11) DEFAULT NULL COMMENT '资源类型',
  `content` text CHARACTER SET utf8mb4 COMMENT '文案内容',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '备注',
  `remark2` varchar(5000) DEFAULT NULL COMMENT '内容备份',  
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `state` (`state`,`execute_time`)
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8mb4   COMMENT='定时任务表';

-- ----------------------------
-- Table structure for tbl_timetask_details
-- ----------------------------
DROP TABLE IF EXISTS `tbl_timetask_details`;
CREATE TABLE `tbl_timetask_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) DEFAULT NULL COMMENT '主任务id',
  `json_content`  text CHARACTER SET utf8mb4 COMMENT '序列化的json字符串',
  `execute_time` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '子任务执行时间',
  `state` int(11) DEFAULT '1' COMMENT '状态  -1暂停     0已完成    1开启中     2取消',
  `msgid` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
  `results` varchar(200) CHARACTER SET utf8mb4  DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `msgid` (`msgid`),
  KEY `tid` (`tid`,`state`),
  KEY `state` (`state`,`results`)
) ENGINE=InnoDB AUTO_INCREMENT=190923 DEFAULT CHARSET=utf8mb4  COMMENT='定时任务详情表';

-- ----------------------------
-- Table structure for tbl_sys_autosettings
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sys_autosettings`;
CREATE TABLE `tbl_sys_autosettings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `auto_type` int(11) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `remarks` varchar(5000) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4  COMMENT='自动配置表';


-- ----------------------------
-- Table structure for tbl_wechat_circleinfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_circleinfo`;
CREATE TABLE `tbl_wx_circleinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sourcewechatid` varchar(255)  DEFAULT NULL,
  `wechatid` varchar(128)  DEFAULT NULL,
  `wechatnickname` varchar(255)  DEFAULT NULL,
  `circleid` varchar(128)  DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 ,
  `publishtime` varchar(255)  DEFAULT NULL,
  `thumbimages` varchar(512)  DEFAULT NULL,
  `images` varchar(512)  DEFAULT NULL,
  `link` varchar(512)  DEFAULT NULL,
  `videothumbimg` varchar(512)  DEFAULT NULL,
  `videourl` varchar(512)  DEFAULT NULL,
  `videodescription` varchar(255)  DEFAULT NULL,
  `videomediaid` varchar(255)  DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wechatid` (`wechatid`,`circleid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=112255 DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈表';



-- ----------------------------
-- Table structure for tbl_wx_friendaddtask
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_friendaddtask`;
CREATE TABLE `tbl_wx_friendaddtask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `execute_time` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `accountid` int(11) DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  `between_time` int(11) DEFAULT NULL,
  `totalsize` int(11) DEFAULT NULL COMMENT '总次数',
  `doingsize` int(11) DEFAULT NULL COMMENT '剩余次数',
  `successsize` int(11) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批量加好友任务';

-- ----------------------------
-- Table structure for tbl_wx_friendaddtask_details
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_friendaddtask_details`;
CREATE TABLE `tbl_wx_friendaddtask_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) DEFAULT NULL,
  `json_content` varchar(2000) DEFAULT NULL,
  `execute_time` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `phonenumber` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='批量加好友任务详情';

-- ----------------------------
-- Table structure for tbl_wx_phonenumber
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_phonenumber`;
CREATE TABLE `tbl_wx_phonenumber` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `tid` int(11) DEFAULT NULL,
  `phonenumber` varchar(128) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL COMMENT '0已使用 1为使用',
  `task_result` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phonenumber` (`phonenumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='电话号码表';


-- ----------------------------
-- Table structure for tbl_callmessage
-- ----------------------------
DROP TABLE IF EXISTS `tbl_callmessage`;
CREATE TABLE `tbl_callmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `date` bigint(20) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `record` varchar(255) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `simid` int(11) DEFAULT NULL,
  `blocktype` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4  COMMENT='手机通话记录';

-- ----------------------------
-- Table structure for tbl_smsmessage
-- ----------------------------
DROP TABLE IF EXISTS `tbl_smsmessage`;
CREATE TABLE `tbl_smsmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `date` bigint(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `readz` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `simid` int(11) DEFAULT NULL,
  `blocktype` int(11) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `imei` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4  COMMENT='手机短信记录';

-- ----------------------------
-- Table structure for tbl_wx_keywords
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_keywords`;
CREATE TABLE `tbl_wx_keywords` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cid` int(11) DEFAULT NULL COMMENT '客户id',
  `wechatid` varchar(255) DEFAULT NULL COMMENT '微信id',
  `key_type` int(4) DEFAULT '0' COMMENT '匹配类型 0精准完全匹配  1模糊包含匹配',
  `key_word` varchar(255) DEFAULT NULL COMMENT '关键词',
  `resource_type` int(4) COMMENT '资源类型 与消息类型一致',
  `return_string` varchar(512) DEFAULT NULL COMMENT '回复词',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4  COMMENT='关键词匹配';



-- ----------------------------
-- Table structure for tbl_resources
-- ----------------------------
DROP TABLE IF EXISTS `tbl_resources`;
CREATE TABLE `tbl_resources` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cid` int(11) DEFAULT NULL COMMENT '客户id',
  `type` int(11) DEFAULT NULL COMMENT '资源类型',
  `remarks` varchar(512) DEFAULT NULL COMMENT '备注',
  `content` text COMMENT '资源数据',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='素材资源库';


-- ----------------------------
-- Table structure for tbl_wx_circletask
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_circletask`;
CREATE TABLE `tbl_wx_circletask` (
  `id` int NOT NULL AUTO_INCREMENT,
  `accountid` int DEFAULT NULL COMMENT '操作员id',
  `cid` int DEFAULT NULL,
  `state` int DEFAULT '1' COMMENT '状态  -1暂停     0已完成    1开启中     2取消',
  `execute_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行时间',
  `restype` int DEFAULT NULL COMMENT '资源类型',
  `content` varchar(5000) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '文案内容',
  `likesize` int DEFAULT NULL COMMENT '点赞数',
  `commentsize` int DEFAULT NULL COMMENT '评论数',
  `totalsize` int DEFAULT NULL,
  `remarks` text CHARACTER SET utf8mb4  COMMENT '备注',
  `wechats` varchar(5000) DEFAULT NULL COMMENT '微信id集合',
  `deleted` varchar(50) DEFAULT NULL COMMENT '删除状态',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=utf8mb4  COMMENT='发朋友圈主任务表';


-- ----------------------------
-- Table structure for tbl_wx_circletask_details
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_circletask_details`;
CREATE TABLE `tbl_wx_circletask_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tid` int DEFAULT NULL COMMENT '主任务id',
  `wechatid` varchar(255) DEFAULT NULL,
  `json_content` varchar(5000) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '序列化的json字符串',
  `execute_time` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '子任务执行时间',
  `state` int DEFAULT '1' COMMENT '状态  -1暂停     0已完成    1开启中     2取消',
  `msgid` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `results` varchar(200) CHARACTER SET utf8mb4  DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `msgid` (`msgid`)
) ENGINE=InnoDB AUTO_INCREMENT=191044 DEFAULT CHARSET=utf8mb4  COMMENT='发朋友圈子任务表';


-- ----------------------------
-- Table structure for tbl_wx_circletask_comment
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_circletask_comment`;
CREATE TABLE `tbl_wx_circletask_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL,
  `circle_wechatid` varchar(255) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `circleid` varchar(255) DEFAULT NULL,
  `comment` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `fromwechatid` varchar(255) DEFAULT NULL,
  `fromname` varchar(255) DEFAULT NULL,
  `towechatid` varchar(255) DEFAULT NULL,
  `toname` varchar(255) DEFAULT NULL,
  `commentid` varchar(255) DEFAULT NULL,
  `replycommentid` varchar(255) DEFAULT NULL,
  `publishtime` varchar(255) DEFAULT NULL,
  `flag` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191044 DEFAULT CHARSET=utf8mb4  COMMENT='朋友圈评论表';

-- ----------------------------
-- Table structure for tbl_wx_circletask_like
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_circletask_like`;
CREATE TABLE `tbl_wx_circletask_like` (
  `id` int NOT NULL AUTO_INCREMENT,
  `circle_wechatid` varchar(255) DEFAULT NULL,
  `wechatid` varchar(255) DEFAULT NULL,
  `circleid` varchar(255) DEFAULT NULL,
  `friendid` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `publishtime` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191044 DEFAULT CHARSET=utf8mb4  COMMENT='朋友圈点赞表';

-- ----------------------------
-- Table structure for tbl_wx_devicegroup
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_devicegroup`;
CREATE TABLE `tbl_wx_devicegroup` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL COMMENT '客户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组名',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COMMENT='设备分组表';


-- ----------------------------
-- Table structure for tbl_friendadd_log
-- ----------------------------
DROP TABLE IF EXISTS `tbl_friendadd_log`;
CREATE TABLE `tbl_friendadd_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL COMMENT '客户id',
  `groupid` int DEFAULT NULL COMMENT '分组id',
  `count` int DEFAULT NULL COMMENT '新增数量', 
  `wechatid` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT 'wechatid',
  `nickname` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT 'nickname',
  `snumber` int(11) DEFAULT NULL COMMENT '排序id',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='好友新增记录表';


-- ----------------------------
-- Table structure for tbl_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tbl_login_log`;
CREATE TABLE `tbl_login_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL COMMENT '客户id',
  `account` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT 'account',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='登录日志记录表';



DROP TABLE IF EXISTS `tbl_wx_luckymoney`;
CREATE TABLE `tbl_wx_luckymoney` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `wechatid` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '当前微信id',
  `friendid` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '好友微信id',
  `friendname` varchar(255) DEFAULT NULL,
  `msgid` varchar(255) DEFAULT NULL COMMENT '唯一消息id',
  `amount` int(11) DEFAULT NULL COMMENT '金额',
  `content` varchar(700) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '消息内容',
  `type` int(11) DEFAULT NULL COMMENT '0红包 1转账',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content` (`content`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4  COMMENT='红包转账收款记录表';


-- ----------------------------
-- Table structure for tbl_wx_friendchange_log
-- ----------------------------
DROP TABLE IF EXISTS `tbl_wx_friendchange_log`;
CREATE TABLE `tbl_wx_friendchange_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL,
  `accountid` int DEFAULT NULL,
  `type` int DEFAULT NULL COMMENT '功能  1增加好友  2删除好友',
  `wechatid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信id',
  `friendid` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '好友微信id',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='好友变更记录';



-- ----------------------------
-- Table structure for tbl_msgdel_log
-- ----------------------------
DROP TABLE IF EXISTS `tbl_msgdel_log`;
CREATE TABLE `tbl_msgdel_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL,
  `wechatid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信id',
  `wechatno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信号',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信昵称',
  `friendid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '好友微信id',
  `friendnick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '好友昵称',
  `issend` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '收发消息标识',
  `contenttype` int DEFAULT NULL COMMENT '消息类型',
  `content` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容',
  `msgid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'msgid',
  `msgsvrid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'msgsvrid',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='消息删除记录';


-- ----------------------------
-- Table structure for tbl_convdel_log
-- ----------------------------
DROP TABLE IF EXISTS `tbl_convdel_log`;
CREATE TABLE `tbl_convdel_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL,
  `wechatid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信id',
  `wechatno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信号',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信昵称',
  `convid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '会话id',
  `convname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '会话名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='会话删除记录';



-- ----------------------------
-- Table structure for tbl_sensitive_words
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sensitive_words`;
CREATE TABLE `tbl_sensitive_words`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `cid` int NULL DEFAULT NULL,
  `words` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4  COMMENT = '敏感词表';


-- ----------------------------
-- Table structure for tbl_chatgpt_conversation
-- ----------------------------
DROP TABLE IF EXISTS `tbl_chatgpt_conversation`;
CREATE TABLE `tbl_chatgpt_conversation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `wechatid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信id',
  `friendid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '好友微信id',
  `conversation` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `wechatid`(`wechatid` ASC, `friendid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4  COMMENT = 'chatgpt会话ID表';

