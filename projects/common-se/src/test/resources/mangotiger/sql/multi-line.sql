-- Drop tables if they exist...
if object_id('Relationship',       'u') is not null drop table Relationship;
if object_id('Relationships',      'u') is not null drop table Relationships;
if object_id('UserActivity',       'u') is not null drop table UserActivity;
if object_id('UserDemographic',    'u') is not null drop table UserDemographic;
if object_id('UserInstantMessage', 'u') is not null drop table UserInstantMessage;
if object_id('InstantMessageType', 'u') is not null drop table InstantMessageType;
if object_id('RelationshipType',   'u') is not null drop table RelationshipType;
if object_id('Users',              'u') is not null drop table Users;
if object_id('Organization',       'u') is not null drop table Organization;

-- Users is the primary user table
-- 1048576 (1 Gigabyte) is used in the identity because the lower IDs are reserved for props.
create table Users
   (userId                     int              not null     identity(1048576,1)
   ,emailAddress               nvarchar (75)    not null     unique
   ,userName                   nvarchar (20)    not null
   ,password                   nvarchar (20)    not null
   ,active                     int              not null
   ,dateRegistered             smalldatetime    not null
   ,primary key (userId)
   );

create table UserActivity
   (userId                     int              not null    unique
   ,status                     int              not null
   ,lastLogon                  smalldatetime    not null
   ,totalLogins                int              not null
   ,timeOnline                 int              not null
   ,foreign key (userId) references Users(userId)
   );

-- Organizations that we are currently accepting registration from.
-- For example, HiNet, Shanghai Telcom
create table Organization
   (organizationCode           nvarchar (20)     not null
   ,organizationName           nvarchar (80)     not null
   ,organizationDescription    nvarchar (80)     not null
   ,primary key (organizationCode)
   );

create table UserDemographic
   (userId                     int              not null    unique
   ,realName                   nvarchar (50)    not null
   ,lastName                   nvarchar (50)    not null
   ,firstName                  nvarchar (50)    not null
   ,mi                         nvarchar (1)     not null
   ,showemail                  int              not null
   ,location                   nvarchar (50)    not null
   ,timeoffset                 int              not null
   ,notificationPreference     nvarchar (20)    not null
   ,invisible                  int              not null
   ,sendPrivateNotifications   int              not null
   ,useSignature               int              not null
   ,viewSignature              int              not null
   ,signature                  ntext            not null
   ,interests                  ntext            not null
   ,customRank                 nvarchar (50)    not null
   ,useCustomRank              int              not null
   ,organizationCode           nvarchar (20)    not null
   ,foreign key (userId) references Users(userId)
   ,foreign key (organizationCode) references Organization(organizationCode)
   );

-- RelationshipTypes enumerates all user relationship types.
-- For example, Friend, Ignore, Clan
create table RelationshipType
   (relationshipType           nvarchar (20)    not null    unique
   ,description                nvarchar (80)    not null
   ,primary key (relationshipType)
   );

create table Relationships
   (userId                     int              not null
   ,relationId                 int              not null
   ,relationshipType           nvarchar (20)    not null
   ,note                       ntext            not null
   ,check (userId <> relationId)
   ,primary key (userId, relationId)
   ,foreign key (userId) references Users(userId)
   ,foreign key (relationId) references Users(userId)
   ,foreign key (relationshipType) references RelationshipType(relationshipType)
   );

-- InstantMessageTypes enumerates the types of Instant Messangers we support.
-- For example, AIM, ICQ, MSN or Yahoo
create table InstantMessageType
   (instantMessageType        nvarchar (20)     not null
   ,description               nvarchar (80)     not null
   ,logo                      nvarchar (80)     not null
   ,primary key (instantMessageType)
   );

create table UserInstantMessage
   (userId                     int              not null
   ,instantMessageType         nvarchar (20)    not null
   ,instantMessageId           nvarchar (75)    not null
   ,primary key (userId, instantMessageType)
   ,foreign key (userId) references Users(userId)
   ,foreign key (instantMessageType) references InstantMessageType(instantMessageType)
   );