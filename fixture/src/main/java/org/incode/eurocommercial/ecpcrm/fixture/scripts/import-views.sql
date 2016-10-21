USE `crm-import`;
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`center` AS
SELECT
 `eurocommercial_center`.`id` AS `id`,
 `eurocommercial_center`.`label` AS `name`,
 ifnull(`eurocommercial_center`.`code`,concat('0',cast(`eurocommercial_center`.`id` as char(2) charset utf8))) AS `reference`
 from `crm`.`eurocommercial_center`
