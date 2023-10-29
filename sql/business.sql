drop table if exists `station`;
create table `station`
(
    `id`          bigint      not null comment 'id',
    `name`        varchar(20) not null comment '站名',
    `name_pinyin` varchar(50) not null comment '站名拼音',
    `name_py`     varchar(50) not null comment '站名拼音首字母',
    `create_time` datetime(3) comment '新增时间',
    `update_time` datetime(3) comment '修改时间',
    primary key (`id`),
    unique key `name_unique` (`name`)
) engine = innodb
  default charset = utf8mb4 comment ='车站';

drop table if exists `train`;
create table `train`
(
    `id`           bigint      not null comment 'id',
    `code`         varchar(20) not null comment '车次编号',
    `type`         char(1)     not null comment '车次类型|枚举[TrainTypeEnum]',
    `start`        varchar(20) not null comment '始发站',
    `start_pinyin` varchar(50) not null comment '始发站拼音',
    `start_time`   time        not null comment '出发时间',
    `end`          varchar(20) not null comment '终点站',
    `end_pinyin`   varchar(50) not null comment '终点站拼音',
    `end_time`     time        not null comment '到站时间',
    `create_time`  datetime(3) comment '新增时间',
    `update_time`  datetime(3) comment '修改时间',
    primary key (`id`),
    unique key `code_unique` (`code`)
) engine = innodb
  default charset = utf8mb4 comment ='车次';

# 开发规范：如果是从表，则表名前加"主表名_"
drop table if exists `train_station`;
create table `train_station`
(
    `id`          bigint        not null comment 'id',
    # 开发规范：两表关联，如果有唯一键，用唯一键关联，没有再考虑用ID关联
    # 若字段与其他表关联，则在字段名前加"关联表名_"
    `train_code`  varchar(20)   not null comment '车次编号',
    `index`       int           not null comment '站序',
    `name`        varchar(20)   not null comment '站名',
    `name_pinyin` varchar(50)   not null comment '站名拼音',
    `in_time`     time comment '进站时间',
    `out_time`    time comment '出站时间',
    # 是否加冗余字段，看使用场景是查多还是写多，查多就可以考虑加冗余，12306就是典型的查多
    `stop_time`   time comment '停站时长',
    `km`          decimal(8, 2) not null comment '里程（公里）|从上一站到本站的距离',
    `create_time` datetime(3) comment '新增时间',
    `update_time` datetime(3) comment '修改时间',
    primary key (`id`),
    # 我们在保存数据时，应该先查是否已存在，而不能认为反正有唯一键，不用查了，直接插入。唯一键是最后一道屏障，保证数据准确性。
    unique key `train_code_index_unique` (`train_code`, `index`),
    unique key `train_code_name_unique` (`train_code`, `name`)
) engine = innodb
  default charset = utf8mb4 comment ='火车车站';

drop table if exists `train_carriage`;
create table `train_carriage` (
  `id` bigint not null comment 'id',
  `train_code` varchar(20) not null comment '车次编号',
  `index` int not null comment '厢号',
  `seat_type` char(1) not null comment '座位类型|枚举[SeatTypeEnum]',
  `seat_count` int not null comment '座位数',
  `row_count` int not null comment '排数',
  `col_count` int not null comment '列数',
  `create_time` datetime(3) comment '新增时间',
  `update_time` datetime(3) comment '修改时间',
  unique key `train_code_index_unique` (`train_code`, `index`),
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='火车车厢';