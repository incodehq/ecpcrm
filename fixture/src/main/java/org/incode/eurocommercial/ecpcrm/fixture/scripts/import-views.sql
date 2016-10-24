USE `crm-import`;

-- Create center view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`center` AS
SELECT
 `eurocommercial_center`.`id` AS `id`,
 `eurocommercial_center`.`label` AS `name`,
 ifnull(`eurocommercial_center`.`code`,concat('0',cast(`eurocommercial_center`.`id` as char(2) charset utf8))) AS `reference`
 FROM `crm`.`eurocommercial_center`;


-- Create user view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`user` AS
SELECT
 u.user_id AS `id`,
 u.`enabled` AS `enabled`,
 
 CASE u.`genre`
 WHEN "m" THEN "MALE"
 WHEN "f" THEN "FEMALE" 
 ELSE "UNKNOWN"
 END AS `gender`,
 IFNULL(UCASE(u.`title`),"UNKNOWN") AS `title`,
 u.`first_name` AS `firstName`,
 u.`last_name` AS `lastName`,
 u.`email` AS `email`,
 u.`card_number` AS `cardNumber`,
 u.`optin` AS `promotionalEmails`,
 c.reference AS `centerReference`
 FROM `crm`.`eurocommercial_crm_user` AS u
 INNER JOIN `center` AS c ON c.id = u.center_id;