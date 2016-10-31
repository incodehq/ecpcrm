USE `crm-import`;

-- Create center view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`center` AS
SELECT
 center.id AS id,
 center.label AS name,
 ifnull(center.code,concat('0',cast(center.id as char(2) charset utf8))) AS reference
 FROM `crm`.`eurocommercial_center` AS center;


-- Create user view
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`user` AS
SELECT
 u.user_id AS id,
 u.enabled AS enabled,
 CASE u.genre
 WHEN "m" THEN "MALE"
 WHEN "f" THEN "FEMALE" 
 ELSE "UNKNOWN_IMPORT"
 END AS gender,
 IFNULL(UCASE(u.title),"UNKNOWN_IMPORT") AS title,
 IFNULL(u.first_name,"UNKNOWN_IMPORT") AS firstName,
 IFNULL(u.last_name,"UNKNOWN_IMPORT") AS lastName,
 IFNULL(u.email,"UNKNOWN_IMPORT") AS email,
 IFNULL(u.card_number,"UNKNOWN_IMPORT") AS cardNumber,
 u.optin AS promotionalEmails,
 c.reference AS centerReference
 FROM `crm`.`eurocommercial_crm_user` AS u
 INNER JOIN `center` AS c ON c.id = u.center_id;
 
CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crm-import`.`center` AS
SELECT
 center.id AS id,
 center.label AS name,
 ifnull(center.code,concat('0',cast(center.id as char(2) charset utf8))) AS reference
 FROM `crm`.`eurocommercial_center` AS center;