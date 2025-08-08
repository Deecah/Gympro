-- Xóa database nếu tồn tại để có thể chạy lại script
IF DB_ID('Gympro') IS NOT NULL
BEGIN
    ALTER DATABASE [Gympro] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [Gympro];
END
GO

-- Tạo Database với các thiết lập mặc định
CREATE DATABASE [Gympro];
GO

USE [Gympro];
GO

-- Bảng Users
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

-- Bảng Customer
CREATE TABLE [dbo].[Customer](
    [Id] [int] NOT NULL,
    [Weight] [float] NULL,
    [Height] [float] NULL,
    [Goal] [text] NULL,
    [MedicalConditions] [text] NULL,
PRIMARY KEY CLUSTERED ([Id] ASC)
);
GO

ALTER TABLE [dbo].[Customer] ADD FOREIGN KEY([Id]) REFERENCES [dbo].[Users] ([Id]);
GO

-- Bảng Trainer
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

-- Insert sample data into Users
DELETE FROM Customer;
DELETE FROM Trainer;
DELETE FROM Users;
DBCC CHECKIDENT ('Users', RESEED, 0);
GO

-- Người dùng 1 là Customer
INSERT INTO Users (Name, Gender, Email, Phone, Address, AvatarUrl, Role, Password)
VALUES (
    N'Trần Thị B', N'Nữ', 'customer1@example.com', '0901234567',
    N'123 Đường ABC, Hà Nội', N'img/avatar/avatar4.jpg', N'Customer',
    HASHBYTES('SHA2_256', '12345')
);
GO

UPDATE Users
SET Email = 'customer1@gmail.com'
WHERE Name = N'Trần Thị B' AND Email = 'customer1@example.com';
GO

-- Người dùng 2 là Trainer
INSERT INTO Users (Name, Gender, Email, Phone, Address, AvatarUrl, Role, Password)
VALUES (
    N'Nguyễn Hà Tiến Quang', N'Nam', 'quang230805@gmail.com', '0935986526',
    N'456 Đường XYZ, TP.HCM', N'img/avatar/avatar1.jpg', N'Trainer',
    HASHBYTES('SHA2_256', '2308')
);
GO

INSERT INTO Users (Name, Gender, Email, Phone, Address, AvatarUrl, Role, Password)
VALUES (
    N'Trần Quang B', N'Nữ', 'customer3@gmail.com', '0901234567',
    N'123 Đường ABC, Hà Nội', N'img/avatar/avatar4.jpg', N'Customer',
    HASHBYTES('SHA2_256', '12345')
);
GO

INSERT INTO Users (Name, Gender, Email, Phone, Address, AvatarUrl, Role, Password)
VALUES (
    N'Duy Vinh Quang Hai', N'Nam', 'admin@gmail.com', '0901234567',
    N'123 Đường ABC, Hà Nội', N'img/avatar/avatar1.jpg', N'Admin',
    HASHBYTES('SHA2_256', '12345')
);
GO

INSERT INTO Users (Name, Gender, Email, Phone, Address, AvatarUrl, Role, Password)
VALUES (
    N'Nguyen Hoang Minh', N'Nam', 'cus@gmail.com', '0923478967',
    N'123 Đường ABC, Hà Nội', N'img/avatar/avatar1.jpg', N'Trainer',
    HASHBYTES('SHA2_256', '67890')
);
GO

SELECT * FROM Users;
GO

-- Insert sample data into Customer
INSERT INTO Customer (Id, Weight, Height, Goal, Medical_conditions)
VALUES (
    1, 68.5, 170,
    N'Giảm cân và tăng sức bền',
    N'Không có bệnh lý'
);
GO

-- Insert sample data into Trainer
INSERT INTO Trainer (Id, ExperienceYears, Description, Specialization)
VALUES (
    2, 5,
    N'Trainer chuyên về thể hình và dinh dưỡng, từng huấn luyện hơn 100 học viên.',
    N'Fitness, Nutrition'
);
GO

SELECT * FROM Customer;
SELECT * FROM Trainer;
GO

-- Bảng Certification
CREATE TABLE [dbo].[Certification](
    [CertificationID] [int] IDENTITY(1,1) NOT NULL,
    [Name] [nvarchar](255) NOT NULL,
    [Description] [nvarchar](max) NULL,
    [ExpireDate] [date] NULL,
PRIMARY KEY CLUSTERED ([CertificationID] ASC)
);
GO

INSERT INTO Certification (Name, Description, ExpireDate)
VALUES 
(N'CPR Certification', N'Certified in CPR and AED usage', '2026-12-31'),
(N'Personal Trainer License', N'Nationally recognized personal trainer license', '2025-08-15'),
(N'Yoga Instructor Certificate', N'Completion of 200-hour yoga training program', '2027-03-01');
GO

SELECT * FROM Certification;
GO

-- Bảng Trainer_Certification
CREATE TABLE [dbo].[Trainer_Certification](
    [TrainerID] [int] NOT NULL,
    [CertificationID] [int] NOT NULL,
PRIMARY KEY CLUSTERED ([TrainerID] ASC, [CertificationID] ASC)
);
GO

ALTER TABLE [dbo].[Trainer_Certification] ADD FOREIGN KEY([CertificationID]) REFERENCES [dbo].[Certification] ([CertificationID]);
ALTER TABLE [dbo].[Trainer_Certification] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
GO

-- Bảng Package
CREATE TABLE [dbo].[Package](
    [PackageID] [int] IDENTITY(1,1) NOT NULL,
    [PackageName] [nvarchar](255) NOT NULL,
    [TrainerID] [int] NOT NULL,
    [Description] [nvarchar](max) NULL,
    [Price] [decimal](10, 2) NULL,
    [Duration] [int] NULL,
    [ImageUrl] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED ([PackageID] ASC)
);
GO

ALTER TABLE [dbo].[Package] ADD FOREIGN KEY([Trainer_ID]) REFERENCES [dbo].[Trainer] ([Id]);
GO

INSERT INTO Package (PackageName, TrainerID, Description, Price, Duration, ImageUrl)
VALUES 
(
    N'Basic Gym Training Package',
    2,
    N'A 30-day training program with fundamental exercises suitable for beginners.',
    1500000,
    30,
    'images/packages/basic.jpg'
),
(
    N'Advanced Strength Program',
    2,
    N'A 60-day intensive strength training package designed for experienced gym-goers.',
    3000000,
    60,
    'images/packages/advanced_strength.jpg'
),
(
    N'Weight Loss Bootcamp',
    2,
    N'A 45-day bootcamp focusing on fat loss, cardio, and meal planning.',
    2500000,
    45,
    'images/packages/weight_loss.jpg'
),
(
    N'Flexibility & Mobility Package',
    2,
    N'A 30-day plan aimed at improving flexibility, mobility, and injury prevention.',
    1800000,
    30,
    'images/packages/flexibility.jpg'
);
GO

SELECT * FROM Package;
SELECT * FROM Package WHERE PackageName LIKE '%Basic%' OR Description LIKE '%Basic%';
SELECT TOP 5 * FROM Package;
GO

-- Bảng Contracts
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
GO

ALTER TABLE [dbo].[Contracts] ADD FOREIGN KEY([PackageID]) REFERENCES [dbo].[Package] ([PackageID]);
ALTER TABLE [dbo].[Contracts] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
ALTER TABLE [dbo].[Contracts] ADD FOREIGN KEY([CustomerID]) REFERENCES [dbo].[Customer] ([Id]);
ALTER TABLE [dbo].[Contracts] ADD CHECK (([Status]='cancelled' OR [Status]='completed' OR [Status]='active' OR [Status]='pending'));
GO

-- Bảng Transaction
CREATE TABLE [dbo].[Transaction](
    [TransactionID] [int] IDENTITY(1,1) NOT NULL,
    [ContractID] [int] NOT NULL,
    [Amount] [decimal](10, 2) NOT NULL,
    [CreatedTime] [datetime] NULL DEFAULT (getdate()),
    [Status] [nvarchar](20) NULL DEFAULT ('pending'),
    [Description] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED ([TransactionID] ASC)
);
GO

ALTER TABLE [dbo].[Transaction] ADD FOREIGN KEY([ContractID]) REFERENCES [dbo].[Contracts] ([Id]);
ALTER TABLE [dbo].[Transaction] ADD CHECK (([Status]='fail' OR [Status]='success' OR [Status]='pending'));
GO

-- Bảng Schedule
CREATE TABLE [dbo].[Schedule](
    [ScheduleID] [int] IDENTITY(1,1) NOT NULL,
    [TrainerID] [int] NOT NULL,
    [UserID] [int] NOT NULL,
    [Weekday] [varchar](10) NOT NULL,
    [Duration] [int] NULL,
    [StartTime] [time](7) NOT NULL,
    [EndTime] [time](7) NOT NULL,
PRIMARY KEY CLUSTERED ([ScheduleID] ASC)
);
GO

ALTER TABLE [dbo].[Schedule] ADD FOREIGN KEY([TrainerID]) REFERENCES [dbo].[Trainer] ([Id]);
ALTER TABLE [dbo].[Schedule] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
ALTER TABLE [dbo].[Schedule] ADD CHECK (([Weekday]='Sunday' OR [Weekday]='Saturday' OR [Weekday]='Friday' OR [Weekday]='Thursday' OR [Weekday]='Wednesday' OR [Weekday]='Tuesday' OR [Weekday]='Monday'));
GO

-- Bảng Progress
CREATE TABLE [dbo].[Progress](
    [ProgressID] [int] IDENTITY(1,1) NOT NULL,
    [UserID] [int] NOT NULL,
    [RecordedAt] [datetime] NULL DEFAULT (getdate()),
    [weight] [float] NULL,
    [body_fat_percent] [float] NULL,
    [muscle_mass] [float] NULL,
    [Notes] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED ([ProgressID] ASC)
);
GO

ALTER TABLE [dbo].[Progress] ADD FOREIGN KEY([UserID]) REFERENCES [dbo].[Users] ([Id]);
GO

INSERT INTO Progress (UserID, RecordedAt, weight, body_fat_percent, muscle_mass, Notes)
VALUES 
(1, '2025-07-29 10:00:00', 68.5, 18.2, 45.1, N'Training tuần đầu tiên'),
(2, '2025-07-29 10:00:00', 68.5, 18.2, 45.1, N'Kiểm tra định kỳ - ghi bởi Trainer');
GO

SELECT * FROM Progress;
GO

-- Bảng Chats
drop table Chats;
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

-- Bảng Notification
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

-- Bảng UserToken
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

-- Bảng TrainerRequest
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

-- Bảng Feedback
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

-- Bảng ViolationReport
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

-- Bảng Program
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

-- Bảng ProgramWeek
CREATE TABLE ProgramWeek (
    WeekID INT PRIMARY KEY IDENTITY(1,1),
    ProgramID INT NOT NULL,
    WeekNumber INT NOT NULL,
    FOREIGN KEY (ProgramID) REFERENCES Program(ProgramID)
);
GO

-- Bảng ProgramDay
CREATE TABLE ProgramDay (
    DayID INT PRIMARY KEY IDENTITY(1,1),
    WeekID INT NOT NULL,
    DayNumber INT NOT NULL,
    FOREIGN KEY (WeekID) REFERENCES ProgramWeek(WeekID)
);
GO
CREATE TABLE Workout (
    WorkoutID INT IDENTITY(1,1) PRIMARY KEY,   -- Tự tăng
    DayID INT NOT NULL,                        -- FK tới ngày trong chương trình
    StartTime TIME,                            -- Thời gian bắt đầu
    EndTime TIME,                              -- Thời gian kết thúc
	Title NVARCHAR(255),              -- Tên buổi tập
    Notes NVARCHAR(500),                       -- Ghi chú
    CreatedAt DATETIME DEFAULT GETDATE(),       -- Ngày tạo
	TrainerID INT NOT NULL,
	FOREIGN KEY (TrainerID) REFERENCES Trainer(Id),
	FOREIGN KEY (DayID) REFERENCES ProgramDay(DayID),
);
GO
-- Bảng Exercises
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
	ExerciseLibraryID INT NOT NULL,	
    CONSTRAINT FK_Exercise_Workout FOREIGN KEY (WorkoutID) REFERENCES Workout(WorkoutID),
	FOREIGN KEY (ExerciseLibraryID) REFERENCES ExerciseLibrary(ExerciseLibraryID)
);
GO
-- Bảng CustomerProgram
CREATE TABLE CustomerProgram (
    Id INT PRIMARY KEY IDENTITY(1,1),
    ProgramID INT NOT NULL,
    CustomerID INT NOT NULL, 
    AssignedAt DATETIME DEFAULT GETDATE(),
    StartDate DATE,
    FOREIGN KEY (ProgramID) REFERENCES Program(ProgramID),
    FOREIGN KEY (CustomerID) REFERENCES [Users](Id)
);
GO

-- Bảng Content
CREATE TABLE Content (
    id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255),
    body TEXT
);
GO

SELECT * FROM Content;
GO