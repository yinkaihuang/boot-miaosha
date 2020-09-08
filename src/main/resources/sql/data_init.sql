-- 添加商品数据
replace into ad_item(`id`,`name`,`title`,`price`,`sales`,`describe`,`image_url`,`create_time`,`update_time`) values(1,'iphone','iphone x 最新款',70000,10,'支持双卡双待，支持联通电话移动','http://www.baidu.com','2019-7-22 14:09:43','2019-7-22 14:09:43');
replace into ad_item(`id`,`name`,`title`,`price`,`sales`,`describe`,`image_url`,`create_time`,`update_time`) values(2,'mi2','小米2手机',20000,10,'支持双卡双待，支持联通电话移动','http://www.baidu.com','2019-7-22 14:09:43','2019-7-22 14:09:43');
replace into ad_item(`id`,`name`,`title`,`price`,`sales`,`describe`,`image_url`,`create_time`,`update_time`) values(3,'荣耀5','最新荣耀，王者归来',10000,10,'支持双卡双待，支持联通电话移动','http://www.baidu.com','2019-7-22 14:09:43','2019-7-22 14:09:43');
-- 添加库存数据
replace into ad_item_stock(`id`,`stock`,`item_id`,`create_time`,`update_time`) values(1,100,1,'2019-7-22 14:09:43','2019-7-22 14:09:43');
replace into ad_item_stock(`id`,`stock`,`item_id`,`create_time`,`update_time`) values(2,300,2,'2019-7-22 14:09:43','2019-7-22 14:09:43');
replace into ad_item_stock(`id`,`stock`,`item_id`,`create_time`,`update_time`) values(3,99,3,'2019-7-22 14:09:43','2019-7-22 14:09:43');
-- 添加活动数据
replace into ad_promo(`id`,`name`,`item_id`,`start_time`,`end_time`,`promo_item_price`,`create_time`,`update_time`)values(1,'iphone双11抢购活动',1,'2019-7-22 14:09:43','2019-7-27 14:09:43',20000,'2019-7-22 14:09:43','2019-7-22 14:09:43');