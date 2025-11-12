USE esun_shop;
GO

IF OBJECT_ID('dbo.sp_insert_product', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_insert_product;
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

IF OBJECT_ID('dbo.sp_create_order_header', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_create_order_header;
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

IF OBJECT_ID('dbo.sp_add_item_update_stock', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_add_item_update_stock;
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
    SELECT @stock = Quantity FROM dbo.Product WITH (ROWLOCK, UPDLOCK) WHERE ProductID = @ProductID;

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

    UPDATE dbo.Product SET Quantity = Quantity - @Quantity WHERE ProductID = @ProductID;
END
GO

IF OBJECT_ID('dbo.sp_mark_order_paid', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_mark_order_paid;
GO
CREATE PROCEDURE dbo.sp_mark_order_paid
    @OrderID VARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE dbo.Orders SET PayStatus = 1 WHERE OrderID = @OrderID;
END
GO
