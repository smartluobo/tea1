CREATE TABLE `t_carousel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_url` varchar(256) DEFAULT NULL,
  `carousel_type` int(2) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `t_category` (
  `id` int(11) NOT NULL COMMENT '主键id',
  `category_name` varchar(16) NOT NULL COMMENT '分类名称',
  `status` int(2) DEFAULT '0' COMMENT '上架状态 0-下架 1-上架',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
  `weight` int(2) DEFAULT NULL COMMENT '权重  用于排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_category_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `category_id` int(11) DEFAULT NULL COMMENT '分类id',
  `goods_weight` int(8) DEFAULT NULL COMMENT '权重，用于排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_coupons_pool` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `coupons_code` varchar(16) DEFAULT NULL COMMENT '券码，保留字段',
  `coupons_start_time` DATETIME NOT NULL  COMMENT '券生效时间',
  `coupons_end_time` DATETIME NOT NULL  COMMENT '券过期时间',
  `coupons_type` int(2) DEFAULT NULL COMMENT '券类型，1-折扣券，2-满减券',
  `coupons_codition` int(5) DEFAULT NULL COMMENT '满减券使用条件，即消费金额满多少可用，消费满100可用该值为100',
  `coupons_ratio` varchar(8) DEFAULT NULL COMMENT '折扣比例，八折券该值为0.8',
  `coupons_amount` int(5) DEFAULT NULL COMMENT '满减券减免金额，消费满100立减20 该值为20',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_name` varchar(32) DEFAULT NULL COMMENT '商品名称',
  `goods_poster` varchar(128) DEFAULT NULL COMMENT '商品海报',
  `goods_price` varchar(16) DEFAULT NULL COMMENT '商品价格',
  `goods_activity_price` varchar(16) DEFAULT NULL COMMENT '商品活动价格',
  `show_activity_price` int(11) DEFAULT NULL COMMENT '是否显示活动价格',
  `activity_start_time` DATETIME NOT NULL  COMMENT '活动开始时间',
  `activity_end_time` DATETIME NOT NULL  COMMENT '活动结束时间',
  `detail1_img_url` varchar(128) DEFAULT NULL COMMENT '详情图1',
  `detail2_img_url` varchar(128) DEFAULT NULL COMMENT '详情图2',
  `detail3_img_url` varchar(128) DEFAULT NULL COMMENT '详情图3',
  `detail4_img_url` varchar(128) DEFAULT NULL COMMENT '详情图4',
  `detail5_img_url` varchar(128) DEFAULT NULL COMMENT '详情图5',
  `have_sku` int(2) DEFAULT NULL COMMENT '是否有sku 0-无sku 1-有sku',
  `goods_status` int(2) DEFAULT NULL COMMENT '上下架状态 0-下架 1-上架',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_goods_sku` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sku_name` varchar(16) DEFAULT NULL COMMENT 'sku名称',
  `sku_price` varchar(8) DEFAULT NULL COMMENT 'sku价格',
  `sku_type_id` int(11) DEFAULT NULL COMMENT 'sku分类id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_goods_sku_record` (
  `id` int(11) DEFAULT NULL COMMENT 'id',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `sku_id` int(11) DEFAULT NULL COMMENT 'skuId',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_goods_sku_type` (
  `id` int(11) DEFAULT NULL COMMENT 'id',
  `name` varchar(16) DEFAULT NULL COMMENT '类型名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `wechat_nick_name` varchar(64) DEFAULT NULL COMMENT '微信昵称',
  `wechat_number` varchar(64) DEFAULT NULL COMMENT '微信号',
  `wechat_oppen_id` varchar(128) DEFAULT NULL COMMENT '微信oppenId',
  `wechat_phone` varchar(16) DEFAULT NULL COMMENT '微信绑定电话号码',
  `phone` varchar(16) DEFAULT NULL COMMENT '电话号码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user_coupons` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `coupons_id` int(11) DEFAULT NULL COMMENT '券id',
  `status` int(2) DEFAULT '0' COMMENT '使用状态 0-未使用，1-已使用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT  '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

