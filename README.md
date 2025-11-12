# Esun Shop (MSSQL Edition) – Spring Boot 3 + Java 17

已改為 **Microsoft SQL Server**：
- Gradle 依賴：`com.microsoft.sqlserver:mssql-jdbc`
- `application.properties` 改為 SQL Server 連線
- DB 腳本為 **T-SQL**（`DB/ddl.sql`, `DB/dml.sql`, `DB/procedures.sql`）
- Repository 改為 `EXEC dbo.sp_xxx`

## 初始化資料庫（用 SSMS 依序執行）
1. `src/main/resources/DB/ddl.sql`
2. `src/main/resources/DB/dml.sql`
3. `src/main/resources/DB/procedures.sql`

## 啟動
```
gradle bootRun
# 或先產生 wrapper： gradle wrapper
# 之後可用： .\gradlew.bat bootRun （Windows）
```

## API
- GET  `/api/products/available`
- POST `/api/products`
- POST `/api/orders`
- POST `/api/orders/{orderId}/pay`

## 前端測試頁
打開 `frontend/index.html`。
