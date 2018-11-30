ALTER TABLE [dbo].[Center]
drop constraint Center_FK1

drop index Center_N50
on [dbo].[Center]

delete
 FROM [dbo].[Numerator]
 WHERE name like 'fake%'

ALTER TABLE [dbo].[Center]
drop column fakeNumerator_Numerator_ID_OID
