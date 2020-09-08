create table if not exists ad_item (
`id` bigint primary key auto_increment,
`name` varchar(50),
`title` varchar(50),
`price` int,
`describe` varchar(255),
`sales` int,
`image_url` varchar(50),
`create_time` datetime,
`update_time` datetime,
`remark` varchar(255)
)engine=innodb;

create table if not exists ad_item_stock (
`id` bigint primary key auto_increment,
`stock` int,
`item_id` bigint,
`create_time` datetime,
`update_time` datetime,
`remark` varchar(255)
)engine=innodb;

alter table ad_item_stock add index index_item_id (item_id);

create table if not exists ad_order (
`id` varchar(255) primary key ,
`user_id` bigint,
`item_id` bigint,
`item_price` int,
`order_price` int,
`amount` int,
`promo_id` bigint,
`create_time` datetime,
`update_time` datetime,
`remark` varchar(255)
)engine=innodb;

create table if not exists ad_promo (
`id` bigint primary key auto_increment,
`name` varchar(255),
start_time datetime,
end_time datetime,
item_id bigint,
promo_item_price int,
create_time datetime,
update_time datetime,
remark varchar(255)
)engine=innodb;

alter table ad_promo add index index_item_id (item_id);

create table if not exists ad_stock_log(
`id` bigint primary key auto_increment,
`item_id` bigint,
`amount` int,
`user_id` bigint,
`update_time` datetime,
`create_time` datetime,
`status` int not null default 0 comment `0 等待执行 2执行成功 -2执行失败`,
`promo_id` bigint,
execute_num int not null default 0,
`remark` varchar(255)
)engine=innodb;
