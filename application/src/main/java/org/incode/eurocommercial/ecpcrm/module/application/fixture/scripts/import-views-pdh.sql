USE `pdh-import`;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdh-import`.`Center` AS
SELECT
 center.id AS id,
 center.name AS name,
 center.code AS code,
 "41b8184472" AS mailchimpListId,
 "accueil@passageduhavre.com" AS contactEmail
FROM `crm-import`.`Center` AS center
WHERE code = 054;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdh-import`.`User` AS
SELECT *
FROM `crm-import`.`User` AS user
WHERE user.centerCode = 054;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdh-import`.`Card` AS
SELECT *
FROM `crm-import`.`Card` AS card
WHERE card.centerCode = 054;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdh-import`.`Child` AS
SELECT child.*
FROM `crm-import`.`Child` AS child
INNER JOIN `crm-import`.`User` AS parent ON child.parentReference = parent.reference
WHERE parent.centerCode = 054;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdh-import`.`CardRequest` AS
SELECT *
FROM `crm-import`.`CardRequest` AS cardRequest
WHERE cardRequest.centerCode = 054;
