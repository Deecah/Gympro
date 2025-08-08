-- Database Fix Script for Gympro
-- Run this script to fix common database issues

-- 1. Check if Content table exists, if not create it
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Content' AND xtype='U')
BEGIN
    CREATE TABLE Content (
        id INT IDENTITY(1,1) PRIMARY KEY,
        title NVARCHAR(255) NOT NULL,
        body NTEXT
    );
    PRINT 'Content table created successfully';
END
ELSE
BEGIN
    PRINT 'Content table already exists';
END

-- 2. Check Customer table structure
IF EXISTS (SELECT * FROM sysobjects WHERE name='Customer' AND xtype='U')
BEGIN
    -- Check if Id column exists
    IF NOT EXISTS (SELECT * FROM syscolumns WHERE id = OBJECT_ID('Customer') AND name = 'Id')
    BEGIN
        PRINT 'Customer table exists but Id column is missing';
        -- Add Id column if missing
        ALTER TABLE Customer ADD Id INT IDENTITY(1,1) PRIMARY KEY;
        PRINT 'Id column added to Customer table';
    END
    ELSE
    BEGIN
        PRINT 'Customer table and Id column exist';
    END
END
ELSE
BEGIN
    PRINT 'Customer table does not exist - this is a critical error';
END

-- 3. Check Contracts table structure
IF EXISTS (SELECT * FROM sysobjects WHERE name='Contracts' AND xtype='U')
BEGIN
    PRINT 'Contracts table exists';
    
    -- Check foreign key constraints
    IF EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK__Contract__Custom__619B8048')
    BEGIN
        PRINT 'Foreign key constraint FK__Contract__Custom__619B8048 exists';
    END
    ELSE
    BEGIN
        PRINT 'Foreign key constraint FK__Contract__Custom__619B8048 is missing';
    END
END
ELSE
BEGIN
    PRINT 'Contracts table does not exist';
END

-- 4. Sample data for testing
-- Insert sample customer if none exists
IF NOT EXISTS (SELECT * FROM Customer)
BEGIN
    INSERT INTO Customer (Name, Email, Phone, Role) 
    VALUES ('Test Customer', 'test@example.com', '123456789', 'Customer');
    PRINT 'Sample customer inserted';
END

-- Insert sample content if none exists
IF NOT EXISTS (SELECT * FROM Content)
BEGIN
    INSERT INTO Content (title, body) 
    VALUES ('Welcome to GymPro', 'Welcome to our fitness platform!');
    PRINT 'Sample content inserted';
END

PRINT 'Database check completed'; 