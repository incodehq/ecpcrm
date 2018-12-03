--Deletes childcare
DELETE cc
FROM [dbo].[ChildCare] cc 
FULL JOIN [dbo].[Child] 
	on cc.child_Child_ID_OID=[dbo].[Child].[Child_ID]
FULL JOIN [dbo].[User]
	on [dbo].[Child].[parent_User_ID_OID]=[dbo].[User].[User_ID]
Where [dbo].[User].[center_Center_ID_OID]='4'


--Deletes children 
DELETE cld
FROM [dbo].[Child] cld 
FULL JOIN [dbo].[User] 
	on cld.parent_User_ID_OID=[dbo].[User].[User_ID]
Where [dbo].[User].[center_Center_ID_OID]='4'

--deletes cardGame
DELETE cg
FROM [dbo].[CardGame] cg  
	INNER JOIN [dbo].[Card] ON cg.card_Card_ID_OID=[dbo].[Card].[Card_ID]
	INNER JOIN [dbo].[User] ON [dbo].[User].[User_ID]=[dbo].[Card].[owner_User_ID_OID]
WHERE [dbo].[User].[center_Center_ID_OID] ='4'

--Deletes cardRequests
ALTER TABLE [dbo].[Card]
drop constraint Card_FK2

ALTER TABLE [dbo].[CardRequest]
drop constraint CardRequest_FK2

DELETE cr
FROM [dbo].[CardRequest] cr 
INNER JOIN [dbo].[User] 
	on cr.requestingUser_User_ID_OID=[dbo].[User].[User_ID]

WHERE [dbo].[User].[center_Center_ID_OID] ='4'

--deletes cards
DELETE c
FROM [dbo].[Card] c 
	INNER JOIN [dbo].[User] ON c.[owner_User_ID_OID]=[dbo].[User].[User_ID]
WHERE [dbo].[User].[center_Center_ID_OID] ='4'

--deletes users
Delete 
FROM [dbo].[User]
Where [dbo].[User].center_Center_ID_OID='4'

--delete center
ALTER TABLE [dbo].[Card]
drop constraint Card_FK1

Delete
FROm [dbo].[Center]
Where [dbo].[Center].[Center_ID]='4'