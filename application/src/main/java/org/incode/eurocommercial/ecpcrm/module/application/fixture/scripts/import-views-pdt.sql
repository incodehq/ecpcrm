USE `pdt-import`;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`Center` AS
SELECT
 center.id AS id,
 center.name AS name,
 center.code AS code,
 "36e3192f10" AS mailchimpListId,
 "contact@lesportesdetaverny.com" AS contactEmail
FROM `crm-import`.`Center` AS center
WHERE code = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`User` AS
SELECT *
FROM `crm-import`.`User` AS user
WHERE user.centerCode = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`Card` AS
SELECT *
FROM `crm-import`.`Card` AS card
WHERE card.centerCode = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`Child` AS
SELECT child.*
FROM `crm-import`.`Child` AS child
INNER JOIN `crm-import`.`User` AS parent ON child.parentReference = parent.reference
WHERE parent.centerCode = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`CardRequest` AS
SELECT *
FROM `crm-import`.`CardRequest` AS cardRequest
WHERE cardRequest.centerCode = 096;
