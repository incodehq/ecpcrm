USE `pdt-import`;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`center` AS
SELECT
 center.id AS id,
 center.name AS name,
 center.code AS code,
 "36e3192f10" AS mailchimpListId,
 "contact@lesportesdetaverny.com" AS contactEmail
FROM `crm-import`.`center` AS center
WHERE code = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`user` AS
SELECT *
FROM `crm-import`.`user` AS user
WHERE user.centerCode = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`card` AS
SELECT *
FROM `crm-import`.`card` AS card
WHERE card.centerCode = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`child` AS
SELECT child.*
FROM `crm-import`.`child` AS child
INNER JOIN `crm-import`.`user` AS parent ON child.parentReference = parent.reference
WHERE parent.centerCode = 096;

CREATE OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pdt-import`.`cardrequest` AS
SELECT *
FROM `crm-import`.`cardrequest` AS cardRequest
WHERE cardRequest.centerCode = 096;
