# 📦 eSun Shop ｜電商購物中心

**Spring Boot + MSSQL + Stored Procedures + 前端**

此專案為一個示範 **三層式架構 + 前後端分離 ** 的電商購物中心系統。

包含：

- 顧客 / 管理員角色登入
- 註冊功能
- 商品查詢與下單（顧客）
- 商品新增（管理員）
- Session-based Auth（後端 Session）
- 所有 DB 操作皆透過 **Stored Procedure**

---

## 系統需求

- Java 17+
- Spring Boot 3.3+
- MSSQL 2019+
- Vue.js 3（CDN 版本）

---

# ✨ 功能列表（Features）

## 👤 Authentication（登入 / 註冊 / 登出）

- 帳號 / 密碼登入（密碼使用 BCrypt 雜湊）
- 註冊可選擇角色（顧客 / 管理員）
- Session-based 身分驗證
- `/api/auth/me` 作為前端守門

---

## 🛒 顧客功能

- 查詢商品（僅顯示庫存 > 0）
- 勾選商品 → 輸入數量（不得超過庫存）
- 建立訂單 POST `/api/orders`
- 自動計算小計 / 總金額
- 建立訂單後自動扣庫存（透過 Stored Procedure）

---

## 🛠 管理員功能

- 新增商品（商品編號 / 名稱 / 數量 / 售價）
- 呼叫 Stored Procedure `sp_insert_product`

---

# 🗂 資料表設計（Database Schema）

## `Users`

| 欄位         | 型別          |
| ------------ | ------------- |
| UserID       | BIGINT        |
| Username     | NVARCHAR(64)  |
| PasswordHash | NVARCHAR(100) |
| Role         | VARCHAR(20)   |
| IsActive     | BIT           |
| CreatedAt    | DATETIME      |

---

## `Product`

| 欄位        | 型別          |
| ----------- | ------------- |
| ProductID   | VARCHAR(10)   |
| ProductName | NVARCHAR(100) |
| Price       | INT           |
| Quantity    | INT           |

---

## `Orders`

| 欄位      | 型別        |
| --------- | ----------- |
| OrderID   | VARCHAR(20) |
| MemberID  | VARCHAR(20) |
| Price     | INT         |
| PayStatus | BIT         |

---

## `OrderDetail`

| 欄位        | 型別        |
| ----------- | ----------- |
| OrderItemSN | INT         |
| OrderID     | VARCHAR(20) |
| ProductID   | VARCHAR(10) |
| Quantity    | INT         |
| StandPrice  | INT         |
| ItemPrice   | INT         |

---

## 🚀 初始化步驟（Setup Guide）

以下說明如何啟動後端 Spring Boot、前端 Vue（CDN 版無須 build），以及如何匯入 MSSQL 資料庫。

---

## 1️⃣ 匯入資料庫（Stored Procedure + 初始資料）

請依序在 **Microsoft SQL Server** 中執行以下腳本：

sql
-- 建立資料表
:r src\main\resources\DB\ddl.sql

-- 插入初始商品 / 訂單資料
:r src\main\resources\DB\dml.sql

-- 建立 Stored Procedures
:r Dsrc\main\resources\DB\procedures.sql

說明： :r 為 SQLCMD 格式，若使用 SSMS 手動執行，請分別開啟三個檔案分段執行即可。

2️⃣ 設定後端連線

修改 src/main/resources/application.properties：
使用下列連線設定：

spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=esun_shop;encrypt=false
spring.datasource.username=esun
spring.datasource.password=123456

spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

3️⃣ 啟動後端
在專案根目錄執行：

./gradlew bootRun

http://localhost:8080
開啟瀏覽器進入上述網址即可使用系統。

資料庫腳本說明
📁 src\main\resources\DB\ddl.sql

建立資料表：

Users ：系統使用者（顧客/管理員）
Product ：商品主檔
Orders ：訂單主檔
OrderDetail ：訂單明細

📁 src\main\resources\DB\dml.sql

插入系統初始資料：

3 個商品

3 筆訂單

4 筆訂單明細

📁 src\main\resources\DB\procedures.sql

建立 Stored Procedure：
sp_insert_product： 管理員新增商品
sp_create_order_header： 建立訂單主檔
sp_add_item_update_stock： 新增訂單明細並扣庫存
usp_CreateUser： 建立使用者

API 說明
Method Path 功能
POST /api/auth/login 使用者登入
POST /api/auth/register 註冊新帳號
POST /api/auth/logout 登出
GET /api/auth/me 取得登入者資訊

GET /api/products/available 顧客取得可售商品
POST /api/products 管理員新增商品

POST /api/orders 顧客建立訂單

```

```
