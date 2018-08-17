/****** Object:  Table [dbo].[AuthenticationDevice]    Script Date: 17-8-2018 10:34:07 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[AuthenticationDevice](
	[AuthenticationDevice_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[active] [bit] NOT NULL,
	[center_Center_ID_OID] [bigint] NOT NULL,
	[name] [varchar](255) NOT NULL,
	[secret] [varchar](255) NOT NULL,
	[type] [varchar](255) NOT NULL,
 CONSTRAINT [AuthenticationDevice_PK] PRIMARY KEY CLUSTERED
(
	[AuthenticationDevice_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Card]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Card](
	[Card_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[center_Center_ID_OID] [bigint] NOT NULL,
	[createdAt] [datetime2](7) NOT NULL,
	[givenToUserAt] [datetime2](7) NULL,
	[number] [varchar](255) NOT NULL,
	[owner_User_ID_OID] [bigint] NULL,
	[sentToUserAt] [datetime2](7) NULL,
	[status] [varchar](255) NOT NULL,
 CONSTRAINT [Card_PK] PRIMARY KEY CLUSTERED
(
	[Card_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [Card_U1] UNIQUE NONCLUSTERED
(
	[number] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[CardGame]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CardGame](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[card_Card_ID_OID] [bigint] NOT NULL,
	[date] [date] NOT NULL,
	[outcome] [bit] NOT NULL,
	[version] [datetime2](7) NOT NULL,
 CONSTRAINT [CardGame_PK] PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[CardRequest]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CardRequest](
	[CardRequest_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[approved] [bit] NULL,
	[assignedCard_Card_ID_OID] [bigint] NULL,
	[handleDate] [datetime2](7) NULL,
	[issueDate] [datetime2](7) NOT NULL,
	[requestingUser_User_ID_OID] [bigint] NOT NULL,
	[type] [varchar](255) NOT NULL,
 CONSTRAINT [CardRequest_PK] PRIMARY KEY CLUSTERED
(
	[CardRequest_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Center]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Center](
	[Center_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[atPath] [varchar](255) NOT NULL,
	[code] [varchar](255) NOT NULL,
	[fakeNumerator_Numerator_ID_OID] [bigint] NOT NULL,
	[id] [varchar](255) NOT NULL,
	[name] [varchar](255) NOT NULL,
	[numerator_Numerator_ID_OID] [bigint] NOT NULL,
	[contactEmail] [varchar](255) NULL,
	[mailchimpListId] [varchar](255) NULL,
 CONSTRAINT [Center_PK] PRIMARY KEY CLUSTERED
(
	[Center_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Child]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Child](
	[Child_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[birthdate] [date] NULL,
	[gender] [varchar](255) NOT NULL,
	[name] [varchar](255) NOT NULL,
	[notes] [varchar](255) NULL,
	[parent_User_ID_OID] [bigint] NOT NULL,
 CONSTRAINT [Child_PK] PRIMARY KEY CLUSTERED
(
	[Child_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[ChildCare]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChildCare](
	[ChildCare_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[checkIn] [datetime2](7) NOT NULL,
	[checkOut] [datetime2](7) NULL,
	[child_Child_ID_OID] [bigint] NOT NULL,
 CONSTRAINT [ChildCare_PK] PRIMARY KEY CLUSTERED
(
	[ChildCare_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Numerator]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Numerator](
	[Numerator_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[format] [varchar](255) NOT NULL,
	[lastIncrement] [bigint] NOT NULL,
	[name] [varchar](255) NOT NULL,
 CONSTRAINT [Numerator_PK] PRIMARY KEY CLUSTERED
(
	[Numerator_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[User]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[User_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[address] [varchar](255) NULL,
	[birthDate] [date] NULL,
	[center_Center_ID_OID] [bigint] NOT NULL,
	[city] [varchar](255) NULL,
	[email] [varchar](255) NOT NULL,
	[enabled] [bit] NOT NULL,
	[firstName] [varchar](40) NOT NULL,
	[hasCar] [bit] NULL,
	[lastName] [varchar](40) NOT NULL,
	[phoneNumber] [varchar](255) NULL,
	[promotionalEmails] [bit] NOT NULL,
	[reference] [varchar](255) NOT NULL,
	[title] [varchar](255) NOT NULL,
	[zipcode] [varchar](255) NULL,
 CONSTRAINT [User_PK] PRIMARY KEY CLUSTERED
(
	[User_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [isispublishmq].[PublishedEvent]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isispublishmq].[PublishedEvent](
	[sequence] [int] NOT NULL,
	[transactionId] [varchar](36) NOT NULL,
	[eventType] [varchar](20) NOT NULL,
	[memberIdentifier] [varchar](255) NULL,
	[serializedForm] [text] NULL,
	[targetAction] [varchar](50) NULL,
	[targetClass] [varchar](50) NULL,
	[target] [varchar](2000) NULL,
	[timestamp] [datetime2](7) NOT NULL,
	[title] [varchar](255) NOT NULL,
	[user] [varchar](50) NOT NULL,
 CONSTRAINT [PublishedEvent_PK] PRIMARY KEY CLUSTERED
(
	[sequence] ASC,
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [isispublishmq].[StatusMessage]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isispublishmq].[StatusMessage](
	[timestamp] [datetime2](7) NOT NULL,
	[transactionId] [varchar](36) NOT NULL,
	[detail] [text] NULL,
	[message] [varchar](255) NOT NULL,
	[oid] [varchar](2000) NULL,
	[status] [int] NULL,
	[uri] [varchar](2000) NULL,
 CONSTRAINT [StatusMessage_PK] PRIMARY KEY CLUSTERED
(
	[timestamp] ASC,
	[transactionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [isissecurity].[ApplicationPermission]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isissecurity].[ApplicationPermission](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[featureFqn] [varchar](255) NOT NULL,
	[featureType] [varchar](255) NOT NULL,
	[mode] [varchar](255) NOT NULL,
	[roleId] [bigint] NOT NULL,
	[rule] [varchar](255) NOT NULL,
	[version] [bigint] NOT NULL,
 CONSTRAINT [ApplicationPermission_PK] PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [ApplicationPermission_role_feature_rule_UNQ] UNIQUE NONCLUSTERED
(
	[roleId] ASC,
	[featureType] ASC,
	[featureFqn] ASC,
	[rule] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [isissecurity].[ApplicationRole]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isissecurity].[ApplicationRole](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[description] [varchar](254) NULL,
	[name] [varchar](50) NOT NULL,
 CONSTRAINT [ApplicationRole_PK] PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [ApplicationRole_name_UNQ] UNIQUE NONCLUSTERED
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [isissecurity].[ApplicationTenancy]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isissecurity].[ApplicationTenancy](
	[path] [varchar](255) NOT NULL,
	[name] [varchar](40) NOT NULL,
	[parentPath] [varchar](255) NULL,
	[version] [bigint] NOT NULL,
 CONSTRAINT [ApplicationTenancy_PK] PRIMARY KEY CLUSTERED
(
	[path] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [ApplicationTenancy_name_UNQ] UNIQUE NONCLUSTERED
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [isissecurity].[ApplicationUser]    Script Date: 17-8-2018 10:34:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isissecurity].[ApplicationUser](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[accountType] [varchar](255) NOT NULL,
	[atPath] [varchar](255) NULL,
	[emailAddress] [varchar](50) NULL,
	[encryptedPassword] [varchar](255) NULL,
	[familyName] [varchar](50) NULL,
	[faxNumber] [varchar](25) NULL,
	[givenName] [varchar](50) NULL,
	[knownAs] [varchar](20) NULL,
	[phoneNumber] [varchar](25) NULL,
	[status] [varchar](255) NOT NULL,
	[username] [varchar](30) NOT NULL,
	[version] [bigint] NOT NULL,
 CONSTRAINT [ApplicationUser_PK] PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [ApplicationUser_username_UNQ] UNIQUE NONCLUSTERED
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [isissecurity].[ApplicationUserRoles]    Script Date: 17-8-2018 10:34:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isissecurity].[ApplicationUserRoles](
	[userId] [bigint] NOT NULL,
	[roleId] [bigint] NOT NULL,
 CONSTRAINT [ApplicationUserRoles_PK] PRIMARY KEY CLUSTERED
(
	[userId] ASC,
	[roleId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [isissettings].[ApplicationSetting]    Script Date: 17-8-2018 10:34:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isissettings].[ApplicationSetting](
	[key] [varchar](128) NOT NULL,
	[description] [varchar](254) NULL,
	[type] [varchar](20) NOT NULL,
	[valueRaw] [varchar](255) NOT NULL,
 CONSTRAINT [ApplicationSetting_PK] PRIMARY KEY CLUSTERED
(
	[key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [isissettings].[UserSetting]    Script Date: 17-8-2018 10:34:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [isissettings].[UserSetting](
	[key] [varchar](128) NOT NULL,
	[user] [varchar](50) NOT NULL,
	[description] [varchar](254) NULL,
	[type] [varchar](20) NOT NULL,
	[valueRaw] [varchar](255) NOT NULL,
 CONSTRAINT [UserSetting_PK] PRIMARY KEY CLUSTERED
(
	[key] ASC,
	[user] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Index [AuthenticationDevice_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [AuthenticationDevice_N49] ON [dbo].[AuthenticationDevice]
(
	[center_Center_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [Card_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [Card_N49] ON [dbo].[Card]
(
	[owner_User_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [Card_N50]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [Card_N50] ON [dbo].[Card]
(
	[center_Center_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [CardGame_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [CardGame_N49] ON [dbo].[CardGame]
(
	[card_Card_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [CardRequest_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [CardRequest_N49] ON [dbo].[CardRequest]
(
	[assignedCard_Card_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [CardRequest_N50]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [CardRequest_N50] ON [dbo].[CardRequest]
(
	[requestingUser_User_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [Center_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [Center_N49] ON [dbo].[Center]
(
	[fakeNumerator_Numerator_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [Center_N50]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [Center_N50] ON [dbo].[Center]
(
	[numerator_Numerator_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [Child_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [Child_N49] ON [dbo].[Child]
(
	[parent_User_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [ChildCare_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [ChildCare_N49] ON [dbo].[ChildCare]
(
	[child_Child_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [User_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [User_N49] ON [dbo].[User]
(
	[center_Center_ID_OID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [ApplicationPermission_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [ApplicationPermission_N49] ON [isissecurity].[ApplicationPermission]
(
	[roleId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [ApplicationTenancy_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [ApplicationTenancy_N49] ON [isissecurity].[ApplicationTenancy]
(
	[parentPath] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [ApplicationUserRoles_N49]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [ApplicationUserRoles_N49] ON [isissecurity].[ApplicationUserRoles]
(
	[userId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [ApplicationUserRoles_N50]    Script Date: 17-8-2018 10:34:09 ******/
CREATE NONCLUSTERED INDEX [ApplicationUserRoles_N50] ON [isissecurity].[ApplicationUserRoles]
(
	[roleId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[AuthenticationDevice]  WITH CHECK ADD  CONSTRAINT [AuthenticationDevice_FK1] FOREIGN KEY([center_Center_ID_OID])
REFERENCES [dbo].[Center] ([Center_ID])
GO
ALTER TABLE [dbo].[AuthenticationDevice] CHECK CONSTRAINT [AuthenticationDevice_FK1]
GO
ALTER TABLE [dbo].[Card]  WITH CHECK ADD  CONSTRAINT [Card_FK1] FOREIGN KEY([center_Center_ID_OID])
REFERENCES [dbo].[Center] ([Center_ID])
GO
ALTER TABLE [dbo].[Card] CHECK CONSTRAINT [Card_FK1]
GO
ALTER TABLE [dbo].[Card]  WITH CHECK ADD  CONSTRAINT [Card_FK2] FOREIGN KEY([owner_User_ID_OID])
REFERENCES [dbo].[User] ([User_ID])
GO
ALTER TABLE [dbo].[Card] CHECK CONSTRAINT [Card_FK2]
GO
ALTER TABLE [dbo].[CardGame]  WITH CHECK ADD  CONSTRAINT [CardGame_FK1] FOREIGN KEY([card_Card_ID_OID])
REFERENCES [dbo].[Card] ([Card_ID])
GO
ALTER TABLE [dbo].[CardGame] CHECK CONSTRAINT [CardGame_FK1]
GO
ALTER TABLE [dbo].[CardRequest]  WITH CHECK ADD  CONSTRAINT [CardRequest_FK1] FOREIGN KEY([assignedCard_Card_ID_OID])
REFERENCES [dbo].[Card] ([Card_ID])
GO
ALTER TABLE [dbo].[CardRequest] CHECK CONSTRAINT [CardRequest_FK1]
GO
ALTER TABLE [dbo].[CardRequest]  WITH CHECK ADD  CONSTRAINT [CardRequest_FK2] FOREIGN KEY([requestingUser_User_ID_OID])
REFERENCES [dbo].[User] ([User_ID])
GO
ALTER TABLE [dbo].[CardRequest] CHECK CONSTRAINT [CardRequest_FK2]
GO
ALTER TABLE [dbo].[Center]  WITH CHECK ADD  CONSTRAINT [Center_FK1] FOREIGN KEY([fakeNumerator_Numerator_ID_OID])
REFERENCES [dbo].[Numerator] ([Numerator_ID])
GO
ALTER TABLE [dbo].[Center] CHECK CONSTRAINT [Center_FK1]
GO
ALTER TABLE [dbo].[Center]  WITH CHECK ADD  CONSTRAINT [Center_FK2] FOREIGN KEY([numerator_Numerator_ID_OID])
REFERENCES [dbo].[Numerator] ([Numerator_ID])
GO
ALTER TABLE [dbo].[Center] CHECK CONSTRAINT [Center_FK2]
GO
ALTER TABLE [dbo].[Child]  WITH CHECK ADD  CONSTRAINT [Child_FK1] FOREIGN KEY([parent_User_ID_OID])
REFERENCES [dbo].[User] ([User_ID])
GO
ALTER TABLE [dbo].[Child] CHECK CONSTRAINT [Child_FK1]
GO
ALTER TABLE [dbo].[ChildCare]  WITH CHECK ADD  CONSTRAINT [ChildCare_FK1] FOREIGN KEY([child_Child_ID_OID])
REFERENCES [dbo].[Child] ([Child_ID])
GO
ALTER TABLE [dbo].[ChildCare] CHECK CONSTRAINT [ChildCare_FK1]
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD  CONSTRAINT [User_FK1] FOREIGN KEY([center_Center_ID_OID])
REFERENCES [dbo].[Center] ([Center_ID])
GO
ALTER TABLE [dbo].[User] CHECK CONSTRAINT [User_FK1]
GO
ALTER TABLE [isissecurity].[ApplicationPermission]  WITH CHECK ADD  CONSTRAINT [ApplicationPermission_FK1] FOREIGN KEY([roleId])
REFERENCES [isissecurity].[ApplicationRole] ([id])
GO
ALTER TABLE [isissecurity].[ApplicationPermission] CHECK CONSTRAINT [ApplicationPermission_FK1]
GO
ALTER TABLE [isissecurity].[ApplicationTenancy]  WITH CHECK ADD  CONSTRAINT [ApplicationTenancy_FK1] FOREIGN KEY([parentPath])
REFERENCES [isissecurity].[ApplicationTenancy] ([path])
GO
ALTER TABLE [isissecurity].[ApplicationTenancy] CHECK CONSTRAINT [ApplicationTenancy_FK1]
GO
ALTER TABLE [isissecurity].[ApplicationUserRoles]  WITH CHECK ADD  CONSTRAINT [ApplicationUserRoles_FK1] FOREIGN KEY([userId])
REFERENCES [isissecurity].[ApplicationUser] ([id])
GO
ALTER TABLE [isissecurity].[ApplicationUserRoles] CHECK CONSTRAINT [ApplicationUserRoles_FK1]
GO
ALTER TABLE [isissecurity].[ApplicationUserRoles]  WITH CHECK ADD  CONSTRAINT [ApplicationUserRoles_FK2] FOREIGN KEY([roleId])
REFERENCES [isissecurity].[ApplicationRole] ([id])
GO
ALTER TABLE [isissecurity].[ApplicationUserRoles] CHECK CONSTRAINT [ApplicationUserRoles_FK2]
GO
