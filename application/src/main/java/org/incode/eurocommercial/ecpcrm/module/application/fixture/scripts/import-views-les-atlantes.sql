USE `les-atlantes-import`;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`center` AS
SELECT
 center.id AS id,
 center.name AS name,
 center.code AS code,
 "45816adb0a" AS mailchimpListId,
 "accueil@les-atlantes.fr" AS contactEmail
FROM `crm-import`.`center` AS center
WHERE code = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`user` AS
SELECT *
FROM `crm-import`.`user` AS user
WHERE user.centerCode = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`card` AS
SELECT *
FROM `crm-import`.`card` AS card
WHERE card.centerCode = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`child` AS
SELECT child.*
FROM `crm-import`.`child` AS child
INNER JOIN `crm-import`.`user` AS parent ON child.parentReference = parent.reference
WHERE parent.centerCode = 037;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `les-atlantes-import`.`cardRequest` AS
SELECT *
FROM `crm-import`.`cardRequest` AS cardRequest
WHERE cardRequest.centerCode = 037;
