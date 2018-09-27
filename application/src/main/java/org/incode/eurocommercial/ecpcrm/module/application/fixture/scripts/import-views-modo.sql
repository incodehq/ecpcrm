USE `modo-import`;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `modo-import`.`Center` AS
SELECT
 center.id AS id,
 center.name AS name,
 center.code AS code,
 "ef80ab638b" AS mailchimpListId,
 "accueil@modo-shopping.fr" AS contactEmail
FROM `crm-import`.`Center` AS center
WHERE code = 095;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `modo-import`.`User` AS
SELECT *
FROM `crm-import`.`User` AS user
WHERE user.centerCode = 095;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `modo-import`.`Card` AS
SELECT *
FROM `crm-import`.`Card` AS card
WHERE card.centerCode = 095;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `modo-import`.`Child` AS
SELECT child.*
FROM `crm-import`.`Child` AS child
INNER JOIN `crm-import`.`User` AS parent ON child.parentReference = parent.reference
WHERE parent.centerCode = 095;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `modo-import`.`CardRequest` AS
SELECT *
FROM `crm-import`.`CardRequest` AS cardRequest
WHERE cardRequest.centerCode = 095;
