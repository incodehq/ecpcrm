USE `les-atlantes-import`;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`Center` AS
SELECT
 center.id AS id,
 center.name AS name,
 center.code AS code,
 "45816adb0a" AS mailchimpListId,
 "accueil@les-atlantes.fr" AS contactEmail
FROM `crm-import`.`Center` AS center
WHERE code = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`User` AS
SELECT *
FROM `crm-import`.`User` AS user
WHERE user.centerCode = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`Card` AS
SELECT *
FROM `crm-import`.`Card` AS card
WHERE card.centerCode = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`Child` AS
SELECT child.*
FROM `crm-import`.`Child` AS child
INNER JOIN `crm-import`.`User` AS parent ON child.parentReference = parent.reference
WHERE parent.centerCode = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`CardRequest` AS
SELECT *
FROM `crm-import`.`CardRequest` AS cardRequest
WHERE cardRequest.centerCode = 037;
