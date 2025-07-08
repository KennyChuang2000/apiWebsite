# API Demo Project

This project is a Spring Boot application that uses an H2 in-memory database. Below are the SQL scripts for creating the necessary tables and indexes.

---
## Spec
| 項目         | 說明                        |
| ---------- | ------------------------- |
| Build Tool | Maven                     |
| Java 版本    | JDK 8                     |
| 框架         | Spring Boot               |
| ORM 工具     | Spring Data JPA 或 OpenJPA |
| 資料庫        | H2（記憶體資料庫）                |
| 測試框架       | JUnit             |

## 功能
### 1.幣別資料表 CRUD API
建立幣別與中文名稱對應的資料表。

    提供以下 RESTful API 功能：

        * 查詢
        * 新增
        * 修改
        * 刪除
### 2.呼叫 Coindesk API
透過 RESTTemplate 呼叫上游 Coindesk API，獲取即時匯率資訊。
將原始 JSON 內容儲存至物件中，供後續資料轉換使用。

### 3.資料轉換 API（轉換後輸出）
轉換 Coindesk API 回傳的資料，組成一個新的 API 格式：

    updatedTime：更新時間，格式為 yyyy/MM/dd HH:mm:ss
    幣別資料包含：
    幣別代碼（code）
    幣別中文名稱（從資料表讀取）
    匯率（rate）



## Table Creation SQL

### 1. `price_snapshot` Table
```sql
CREATE TABLE price_snapshot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    updated VARCHAR(255),
    updated_iso TIMESTAMP,
    updateduk VARCHAR(50),
    disclaimer VARCHAR(255),
    chart_name VARCHAR(50),
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP
);

CREATE INDEX idx_price_snapshot_created_date ON price_snapshot (created_date);
CREATE INDEX idx_price_snapshot_updated_iso ON price_snapshot (updated_iso);
```

### 2. `price` Table
```sql
CREATE TABLE price (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    snapshot_id BIGINT NOT NULL,
    code VARCHAR(10),
    symbol VARCHAR(10),
    rate VARCHAR(50),
    description VARCHAR(50),
    rate_float DECIMAL(18, 6),
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (snapshot_id) REFERENCES price_snapshot(id) ON DELETE CASCADE
);

CREATE INDEX idx_price_snapshot_id ON price (snapshot_id);
CREATE INDEX idx_price_code ON price (code);
CREATE INDEX idx_price_created_date ON price (created_date);
```

---

## How to Run the Project

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd api_demo
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the H2 Console:
   - URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: *(leave blank)*

---

## Project Configuration

### `application.properties`
Key configurations for the project:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=none
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
server.port=8080
```

---

## API Reference

### Coindesk API
The project fetches data from the Coindesk API:
- URL: `https://kengp3.github.io/blog/coindesk.json`

---
