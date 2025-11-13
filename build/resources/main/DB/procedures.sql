USE esun_shop;
GO

/* =========================
   商品 / 訂單相關
   ========================= */

IF OBJECT_ID('dbo.sp_insert_product', 'P') IS NOT NULL 
    DROP PROCEDURE dbo.sp_insert_product;
GO
CREATE PROCEDURE dbo.sp_insert_product
    @ProductID   VARCHAR(10),
    @ProductName NVARCHAR(100),
    @Price       INT,
    @Quantity    INT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.Product(ProductID, ProductName, Price, Quantity)
    VALUES(@ProductID, @ProductName, @Price, @Quantity);
END
GO


IF OBJECT_ID('dbo.sp_create_order_header', 'P') IS NOT NULL 
    DROP PROCEDURE dbo.sp_create_order_header;
GO
CREATE PROCEDURE dbo.sp_create_order_header
    @OrderID    VARCHAR(20),
    @MemberID   VARCHAR(20),
    @TotalPrice INT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.Orders(OrderID, MemberID, Price, PayStatus)
    VALUES(@OrderID, @MemberID, @TotalPrice, 0);
END
GO


IF OBJECT_ID('dbo.sp_add_item_update_stock', 'P') IS NOT NULL 
    DROP PROCEDURE dbo.sp_add_item_update_stock;
GO
CREATE PROCEDURE dbo.sp_add_item_update_stock
    @OrderID    VARCHAR(20),
    @ProductID  VARCHAR(10),
    @Quantity   INT,
    @StandPrice INT,
    @ItemPrice  INT
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE @stock INT;

    SELECT @stock = Quantity 
    FROM dbo.Product WITH (ROWLOCK, UPDLOCK) 
    WHERE ProductID = @ProductID;

    IF @stock IS NULL
    BEGIN
        THROW 50001, 'Product not found', 1;
    END

    IF @stock < @Quantity
    BEGIN
        THROW 50002, 'Insufficient stock', 1;
    END

    INSERT INTO dbo.OrderDetail(OrderID, ProductID, Quantity, StandPrice, ItemPrice)
    VALUES(@OrderID, @ProductID, @Quantity, @StandPrice, @ItemPrice);

    UPDATE dbo.Product 
    SET Quantity = Quantity - @Quantity 
    WHERE ProductID = @ProductID;
END
GO


IF OBJECT_ID('dbo.sp_mark_order_paid', 'P') IS NOT NULL 
    DROP PROCEDURE dbo.sp_mark_order_paid;
GO
CREATE PROCEDURE dbo.sp_mark_order_paid
    @OrderID VARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.Orders 
    SET PayStatus = 1 
    WHERE OrderID = @OrderID;
END
GO


/* =========================
   Users / Auth 相關
   ========================= */

-- 依帳號查使用者（AuthRepository.findByUsername）
IF OBJECT_ID('dbo.usp_FindUserByUsername', 'P') IS NOT NULL
    DROP PROCEDURE dbo.usp_FindUserByUsername;
GO
CREATE PROCEDURE dbo.usp_FindUserByUsername
    @Username NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT TOP 1
        [UserID],
        [Username],
        [PasswordHash],
        [Role],
        [IsActive],
        [CreatedAt]
    FROM [dbo].[Users]
    WHERE [Username] = @Username;
END
GO


-- 使用者總數（DataInitializer 用來 seed 預設帳號）
IF OBJECT_ID('dbo.usp_CountUsers', 'P') IS NOT NULL
    DROP PROCEDURE dbo.usp_CountUsers;
GO
CREATE PROCEDURE dbo.usp_CountUsers
AS
BEGIN
    SET NOCOUNT ON;

    SELECT COUNT(1) AS TotalUsers
    FROM [dbo].[Users];
END
GO


-- 建立使用者（註冊 / seed 預設帳號）
IF OBJECT_ID('dbo.usp_CreateUser', 'P') IS NOT NULL
    DROP PROCEDURE dbo.usp_CreateUser;
GO
CREATE PROCEDURE dbo.usp_CreateUser
    @Username      NVARCHAR(50),
    @PasswordHash  NVARCHAR(200),
    @Role          NVARCHAR(20),
    @IsActive      BIT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO [dbo].[Users](
        [Username],
        [PasswordHash],
        [Role],
        [IsActive],
        [CreatedAt]
    )
    VALUES(
        @Username,
        @PasswordHash,
        @Role,
        @IsActive,
        GETDATE()
    );
END
GO
