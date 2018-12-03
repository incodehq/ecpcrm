SELECT u.[User_ID], c.[Card_ID], cr.[CardRequest_ID], cg.[card_Card_ID_OID], ch.[Child_ID], cc.[ChildCare_ID]
 
into #TempTable

from [dbo].[User] u

FULL JOIN [dbo].[Card] c
	on c.[owner_User_ID_OID]=u.[User_ID]

FULL JOIN [dbo].[CardRequest] cr
	on cr.requestingUser_User_ID_OID=u.[User_ID]

FULL JOIN [dbo].[CardGame] cg
	on cg.card_Card_ID_OID=c.[Card_ID]

FULL JOIN [dbo].[Child] ch
	on ch.parent_User_ID_OID=u.[User_ID]

FULL JOIN [dbo].[ChildCare] cc
	on cc.child_Child_ID_OID=ch.[Child_ID]

Where u.[center_Center_ID_OID]='4'


DELETE cg
FROM [dbo].[CardGame] cg

INNER JOIN #TempTable tt
	on cg.card_Card_ID_OID=tt.[card_Card_ID_OID]


---------------
DELETE cr
FROM [dbo].[CardRequest] cr

INNER JOIN #TempTable tt
	on cr.CardRequest_ID=tt.[CardRequest_ID]
---------------
DELETE c
FROM [dbo].[Card] c

INNER JOIN #TempTable tt
	on c.Card_ID=tt.[Card_ID]

DELETE c
FROM [dbo].[Card] c
Where c.center_Center_ID_OID='4'

------
DELETE cc
FROM [dbo].[ChildCare] cc

INNER JOIN #TempTable tt
	on cc.ChildCare_ID=tt.ChildCare_ID
------

DELETE ch
FROM [dbo].[Child] ch

INNER JOIN #TempTable tt
	on ch.Child_ID=tt.Child_ID

-------

DELETE u
FROM [dbo].[User] u
WHERE u.center_Center_ID_OID='4'

------

Delete ce
FROM [dbo].[Center] ce
WHERE ce.Center_ID='4'

----
DROP TABLE #TempTable