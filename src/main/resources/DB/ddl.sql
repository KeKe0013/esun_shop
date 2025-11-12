IF DB_ID(N'esun_shop') IS NULL
BEGIN
    CREATE DATABASE esun_shop;
END
GO

USE esun_shop;
GO

IF OBJECT_ID('dbo.OrderDetail', 'U') IS NOT NULL DROP TABLE dbo.OrderDetail;
IF OBJECT_ID('dbo.Orders', 'U') IS NOT NULL DROP TABLE dbo.Orders;
IF OBJECT_ID('dbo.Product', 'U') IS NOT NULL DROP TABLE dbo.Product;
GO

CREATE TABLE dbo.Product (
    ProductID   VARCHAR(10)   NOT NULL PRIMARY KEY,
    ProductName NVARCHAR(100) NOT NULL,
    Price       INT           NOT NULL,
    Quantity    INT           NOT NULL
);
GO

CREATE TABLE dbo.Orders (
    OrderID   VARCHAR(20) NOT NULL PRIMARY KEY,
    MemberID  VARCHAR(20) NOT NULL,
    Price     INT         NOT NULL,
    PayStatus BIT         NOT NULL DEFAULT (0)
);
GO

CREATE TABLE dbo.OrderDetail (
    OrderItemSN INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    OrderID     VARCHAR(20) NOT NULL,
    ProductID   VARCHAR(10) NOT NULL,
    Quantity    INT NOT NULL,
    StandPrice  INT NOT NULL,
    ItemPrice   INT NOT NULL,
    CONSTRAINT FK_OrderDetail_Orders  
        FOREIGN KEY (OrderID)  
        REFERENCES dbo.Orders(OrderID),
    CONSTRAINT FK_OrderDetail_Product 
        FOREIGN KEY (ProductID) 
        REFERENCES dbo.Product(ProductID)
);
GO

IF OBJECT_ID(N'dbo.Users', N'U') IS NULL
BEGIN
    CREATE TABLE [dbo].[Users](
        [UserID]      BIGINT IDENTITY(1,1) PRIMARY KEY,
        [Username]    NVARCHAR(64)  NOT NULL,
        [PasswordHash] NVARCHAR(100) NOT NULL,
        [Role]        VARCHAR(20)   NOT NULL,
        [IsActive]    BIT           NOT NULL CONSTRAINT DF_Users_IsActive DEFAULT(1),
        [CreatedAt]   DATETIME2     NOT NULL CONSTRAINT DF_Users_CreatedAt DEFAULT(SYSDATETIME())
    );
    CREATE UNIQUE INDEX UX_Users_Username ON [dbo].[Users]([Username]);
END
GO
