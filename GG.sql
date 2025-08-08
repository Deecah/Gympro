IF DB_ID('Gympro') IS NOT NULL
BEGIN
    ALTER DATABASE [Gympro] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [Gympro];
END
GO

CREATE DATABASE [Gympro];
GO

USE [Gympro];
GO

CREATE TABLE [dbo].[Users](
    [Id] [int] IDENTITY(1,1) NOT NULL,
    [Name] [nvarchar](255) NOT NULL,
    [Gender] [nvarchar](10) NULL,
    [Email] [nvarchar](255) NOT NULL UNIQUE,
    [Phone] [nchar](10) NULL,
    [Address] [nvarchar](255) NULL,
    [AvatarUrl] [nvarchar](255) NULL,
    [Role] [nvarchar](20) NOT NULL,
    [Status] [nvarchar](20) NULL DEFAULT ('Normal'),
    [Password] [varbinary](64) NOT NULL,
PRIMARY KEY CLUSTERED ([Id] ASC)
);
GO

ALTER TABLE [dbo].[Users] ADD CONSTRAINT [CHK_Users_PhoneFormat] CHECK (([Phone] LIKE '0[0-9]%' AND LEN([Phone])=(10) AND NOT [Phone] LIKE '%[^0-9]%'));
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [CHK_Users_Role] CHECK (([Role]='Trainer' OR [Role]='Customer' OR [Role]='Admin'));
ALTER TABLE [dbo].[Users] ADD CONSTRAINT [CHK_Users_Status] CHECK (([Status]='Banned' OR [Status]='Normal'));
GO

CREATE TABLE [dbo].[Customer](
    [Id] [int] NOT NULL primary key CLUSTERED,
    [Weight] [float] NULL,
    [Height] [float] NULL,
    [Goal] [text] NULL,
    [MedicalConditions] [text] NULL,
	FOREIGN KEY (Id) REFERENCES Users(Id)
);
INSERT Customer(Id, Weight, Height, Goal, MedicalConditions) VALUES (5, 60,180,'good body', 'none');
GO

ALTER TABLE [dbo].[Customer] ADD FOREIGN KEY([Id]) REFERENCES [dbo].[Users] ([Id]);
GO

CREATE TABLE [dbo].[Trainer](
    [Id] [int] NOT NULL,
    [ExperienceYears] [int] NULL,
    [Description] [nvarchar](max) NULL,
    [Specialization] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED ([Id] ASC)
);
GO

ALTER TABLE [dbo].[Trainer] ADD FOREIGN KEY([Id]) REFERENCES [dbo].[Users] ([Id]);
GO
DBCC CHECKIDENT ('Users', RESEED, 0);
GO

CREATE TABLE [dbo].[Certification](
    [CertificationID] [int] IDENTITY(1,1) NOT NULL,
    [Name] [nvarchar](255) NOT NULL,
    [Description] [nvarchar](max) NULL,
    [ExpireDate] [date] NULL,
PRIMARY KEY CLUSTERED ([CertificationID] ASC)
);
GO

CREATE TABLE [dbo].[Trainer_Certification](
    [TrainerID] [int] NOT NULL,
    [CertificationID] [int] NOT NULL,
PRIMARY KEY CLUSTERED ([TrainerID] ASC, [CertificationID] ASC)
);
GO

ALTER TABLE [dbo].[Trainer_Certification] ADD FOREIGN KEY([CertificationID]) REFERENCES [dbo].[Certification] ([CertificationID]);
ALTER TABLE [dbo].[Trainer_Certification] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
GO

CREATE TABLE [dbo].[Package](
    [PackageID] [int] IDENTITY(1,1) NOT NULL,
    [PackageName] [nvarchar](255) NOT NULL,
    [TrainerID] [int] NOT NULL,
    [Description] [nvarchar](max) NULL,
    [Price] [decimal](10, 2) NULL,
    [Duration] [int] NULL,
    [ImageUrl] [nvarchar](255) NULL,
	FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]),
	PRIMARY KEY CLUSTERED ([PackageID] ASC)
);
GO

CREATE TABLE [dbo].[Contracts](
    [Id] [int] IDENTITY(1,1) NOT NULL,
    [TrainerID] [int] NOT NULL,
    [CustomerID] [int] NOT NULL,
    [PackageID] [int] NOT NULL,
    [StartDate] [date] NOT NULL,
    [EndDate] [date] NULL,
    [Status] [nvarchar](20) NULL DEFAULT ('pending'),
PRIMARY KEY CLUSTERED ([Id] ASC)
);

ALTER TABLE [dbo].[Contracts] ADD FOREIGN KEY([PackageID]) REFERENCES [dbo].[Package] ([PackageID]);
ALTER TABLE [dbo].[Contracts] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
ALTER TABLE [dbo].[Contracts] ADD FOREIGN KEY([CustomerID]) REFERENCES [dbo].[Customer] ([Id]);
ALTER TABLE [dbo].[Contracts] ADD CHECK (([Status]='cancelled' OR [Status]='completed' OR [Status]='active' OR [Status]='pending'));
GO

CREATE TABLE [dbo].[Transaction](
    [TransactionID] [int] IDENTITY(1,1) NOT NULL,
    [ContractID] [int] NOT NULL,
    [Amount] [decimal](10, 2) NOT NULL,
    [CreatedTime] [datetime] NULL DEFAULT (getdate()),
    [Status] [nvarchar](20) NULL DEFAULT ('pending'),
    [Description] [nvarchar](max) NULL,
	[CustomerID] [int] NOT NULL,
	PRIMARY KEY CLUSTERED ([TransactionID] ASC),
	FOREIGN KEY (CustomerID) REFERENCES Customer(Id)
);
GO
ALTER TABLE [dbo].[Transaction] ADD FOREIGN KEY([ContractID]) REFERENCES [dbo].[Contracts] ([Id]);
ALTER TABLE [dbo].[Transaction] ADD CHECK (([Status]='fail' OR [Status]='success' OR [Status]='pending'));
GO

GO
ALTER TABLE [dbo].[Schedule] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
ALTER TABLE [dbo].[Schedule] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
ALTER TABLE [dbo].[Schedule] ADD CHECK (([Weekday]='Sunday' OR [Weekday]='Saturday' OR [Weekday]='Friday' OR [Weekday]='Thursday' OR [Weekday]='Wednesday' OR [Weekday]='Tuesday' OR [Weekday]='Monday'));
GO

CREATE TABLE Progress (
    ProgressID INT IDENTITY(1,1) PRIMARY KEY,
    CustomerProgramID INT NOT NULL,
    ProgressPercent FLOAT NOT NULL,
    RecordedAt DATETIME NOT NULL,
    FOREIGN KEY (CustomerProgramID) REFERENCES CustomerProgram(Id)
);
GO

ALTER TABLE [dbo].[Progress] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
GO

CREATE TABLE [dbo].[Chats](
    [ChatID] [int] IDENTITY(1,1) NOT NULL,
    [UserID1] [int] NOT NULL,
    [UserID2] [int] NOT NULL,
    [StartedAt] [datetime] NULL DEFAULT (getdate()),
    [LastMessageAt] [datetime] NULL DEFAULT (getdate()),
PRIMARY KEY CLUSTERED ([ChatID] ASC)
);
GO

ALTER TABLE [dbo].[Chats] ADD FOREIGN KEY([Participant1UserID]) REFERENCES [dbo].[Users] ([Id]);
ALTER TABLE [dbo].[Chats] ADD FOREIGN KEY([Participant2UserID]) REFERENCES [dbo].[Users] ([Id]);
GO

CREATE TABLE [dbo].[Messages](
    [MessageID] [int] IDENTITY(1,1) NOT NULL,
    [ChatID] [int] NOT NULL,
    [SenderUserID] [int] NOT NULL,
    [MessageContent] [nvarchar](max) NOT NULL,
    [SentAt] [datetime] NULL DEFAULT (getdate()),
    [IsRead] [bit] NULL DEFAULT ((0)),
	[ImageUrl] NVARCHAR(MAX) NULL,
    [FileUrl] NVARCHAR(MAX) NULL,
PRIMARY KEY CLUSTERED ([MessageID] ASC)
);
GO

ALTER TABLE [dbo].[Messages] ADD FOREIGN KEY([ChatID]) REFERENCES [dbo].[Chats] ([ChatID]);
ALTER TABLE [dbo].[Messages] ADD FOREIGN KEY([SenderUserID]) REFERENCES [dbo].[Users] ([Id]);
GO

CREATE TABLE [dbo].[Notification](
    [NotificationID] [int] IDENTITY(1,1) NOT NULL,
    [UserID] [int] NOT NULL,
    [NotificationTitle] [nvarchar](255) NULL,
    [NotificationContent] [nvarchar](max) NULL,
    [CreatedTime] [datetime] NULL DEFAULT (getdate()),
PRIMARY KEY CLUSTERED ([NotificationID] ASC)
);
GO

ALTER TABLE [dbo].[Notification] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
GO

CREATE TABLE [dbo].[UserToken](
    [Id] [int] IDENTITY(1,1) NOT NULL,
    [UserID] [int] NOT NULL,
    [Token] [nvarchar](255) NOT NULL UNIQUE,
    [TokenType] [nvarchar](50) NOT NULL,
    [Expiry] [datetime] NOT NULL,
    [IsUsed] [bit] NULL DEFAULT ((0)),
    [CreatedAt] [datetime] NULL DEFAULT (getdate()),
PRIMARY KEY CLUSTERED ([Id] ASC)
);
GO

ALTER TABLE [dbo].[UserToken] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
ALTER TABLE [dbo].[UserToken] ADD CHECK (([TokenType]='password_reset' OR [TokenType]='email_verification'));
GO

CREATE TABLE [dbo].[TrainerRequest](
    [TrainerRequestID] [int] IDENTITY(1,1) NOT NULL,
    [TrainerID] [int] NOT NULL,
    [RequestContent] [nvarchar](max) NULL,
    [SubmittedAt] [datetime] NULL DEFAULT (getdate()),
    [Status] [nvarchar](20) NULL,
    [AdminID] [int] NULL,
    [ProcessedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED ([TrainerRequestID] ASC)
);
GO

ALTER TABLE [dbo].[TrainerRequest] ADD FOREIGN KEY([AdminID]) REFERENCES [dbo].[Users] ([Id]);
ALTER TABLE [dbo].[TrainerRequest] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
GO

CREATE TABLE [dbo].[Feedback](
    [FeedbackID] [int] IDENTITY(1,1) NOT NULL,
    [UserID] [int] NOT NULL,
    [FeedbackType] [nvarchar](20) NULL,
    [FeedbackContent] [nvarchar](max) NULL,
    [Point] [int] NULL,
    [TrainerID] [int] NULL,
    [PackageID] [int] NULL,
PRIMARY KEY CLUSTERED ([FeedbackID] ASC)
);
GO

ALTER TABLE [dbo].[Feedback] ADD FOREIGN KEY([PackageID]) REFERENCES [dbo].[Package] ([PackageID]);
ALTER TABLE [dbo].[Feedback] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
ALTER TABLE [dbo].[Feedback] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
ALTER TABLE [dbo].[Feedback] ADD CHECK (([FeedbackType]='package' OR [FeedbackType]='trainer'));
ALTER TABLE [dbo].[Feedback] ADD CHECK (([Point]>=(0) AND [Point]<=(5)));
GO

CREATE TABLE [dbo].[ViolationReport](
    [ViolationID] [int] IDENTITY(1,1) NOT NULL,
    [ReportedUserID] [int] NOT NULL,
    [UserID] [int] NOT NULL,
    [Reason] [nvarchar](max) NULL,
    [CreatedAt] [datetime] NULL DEFAULT (getdate()),
PRIMARY KEY CLUSTERED ([ViolationID] ASC)
);
GO

ALTER TABLE [dbo].[ViolationReport] ADD FOREIGN KEY([ReportedUserID]) REFERENCES [dbo].[Users] ([Id]);
ALTER TABLE [dbo].[ViolationReport] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
GO

CREATE TABLE Program (
    ProgramID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),
    TrainerID INT NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE(),
	PackageID INT NOT NULL,
    FOREIGN KEY (TrainerID) REFERENCES Trainer(Id),
	FOREIGN KEY (PackageID) REFERENCES Package(PackageID),
);
GO

CREATE TABLE Workout (
    WorkoutID INT IDENTITY(1,1) PRIMARY KEY,   -- Tự tăng
    [Date] DATE NOT NULL,                        -- FK tới ngày trong chương trình
    StartTime TIME,                            -- Thời gian bắt đầu
    EndTime TIME,                              -- Thời gian kết thúc
	TrainerID INT NOT NULL,
	[Status] [nvarchar](20) null default ('pending'),
	[CustomerProgramID] int not null,
	[ScheduleID] int not null,
	FOREIGN KEY (TrainerID) REFERENCES Trainer(Id),
	CHECK ([Status] IN ('pending', 'completed', 'missed')),
	foreign key (CustomerProgramID) references CustomerProgram(Id),
	foreign key ([ScheduleID]) references CustomerWorkoutSchedule(ScheduleID)
);

CREATE TABLE [CustomerWorkoutSchedule](
    [ScheduleID] [int] IDENTITY(1,1) NOT NULL,
    [CustomerProgramID] [int] NOT NULL,
    [StartAt] DATE NOT NULL,
    [EndAt] DATE NOT NULL,
    [Status] [nvarchar](20) NULL DEFAULT ('pending'),
PRIMARY KEY CLUSTERED ([ScheduleID] ASC),
FOREIGN KEY ([CustomerProgramID]) REFERENCES [dbo].[CustomerProgram] ([Id]),
CHECK ([Status] IN ('pending', 'completed'))
);
GO

CREATE TABLE ExerciseLibrary (
    ExerciseLibraryID INT IDENTITY(1,1) PRIMARY KEY,  -- Tự tăng
    ExerciseName NVARCHAR(255) NOT NULL,       -- Tên bài tập
    Sets INT NOT NULL,                         -- Số set
    Reps INT NOT NULL,                         -- Số rep
    RestTimeSeconds INT DEFAULT 0,             -- Thời gian nghỉ giữa set
    VideoUrl NVARCHAR(MAX),
	Description NVARCHAR(500),
	MuscleGroup NVARCHAR(200),
	Equipment NVARCHAR(200),
	TrainerID INT NOT NULL,
	FOREIGN KEY (TrainerID) REFERENCES Trainer(Id),
);
GO
CREATE TABLE ExerciseProgram (
    ExerciseProgramID INT IDENTITY(1,1) PRIMARY KEY,  -- Tự tăng
	ExerciseLibraryID INT NOT NULL,
	TrainerID INT NOT NULL,
	FOREIGN KEY (TrainerID) REFERENCES Trainer(Id),
	ProgramID INT NOT NULL,
	FOREIGN KEY (ProgramID) REFERENCES Program(ProgramID),
	FOREIGN KEY (ExerciseLibraryID) REFERENCES ExerciseLibrary(ExerciseLibraryID),
);
GO

CREATE TABLE Exercise (
    ExerciseID INT IDENTITY(1,1) PRIMARY KEY,  -- Tự tăng
    WorkoutID INT NOT NULL,                    -- FK tới buổi tập
	ExerciseProgramID INT NOT NULL,	
    CONSTRAINT FK_Exercise_Workout FOREIGN KEY (WorkoutID) REFERENCES Workout(WorkoutID),
	FOREIGN KEY (ExerciseProgramID) REFERENCES ExerciseProgram(ExerciseProgramID)
);
GO

CREATE TABLE CustomerProgram (
    Id INT PRIMARY KEY IDENTITY(1,1),
    ProgramID INT NOT NULL,
    CustomerID INT NOT NULL, 
    AssignedAt DATETIME DEFAULT GETDATE(),
    StartDate DATE,
	EndDate DATE,
    FOREIGN KEY (ProgramID) REFERENCES Program(ProgramID),
    FOREIGN KEY (CustomerID) REFERENCES [Users](Id)
);
GO


CREATE TABLE Content (
    id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255),
    body TEXT
);
GO