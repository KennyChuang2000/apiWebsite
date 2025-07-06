# API Demo Project

This project is a Spring Boot application that uses an H2 in-memory database. Below are the SQL scripts for creating the necessary tables and indexes.

---

## Table Creation SQL

### 1. `price_snapshot` Table
```sql
CREATE TABLE price_snapshot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary key for price_snapshot table',
    updated VARCHAR(255) COMMENT 'Last updated time as a string',
    updated_iso TIMESTAMP COMMENT 'Last updated time in ISO format',
    updateduk VARCHAR(50) COMMENT 'Last updated time in UK format',
    disclaimer VARCHAR(255) COMMENT 'Disclaimer text for the snapshot',
    chart_name VARCHAR(50) COMMENT 'Name of the chart (e.g., currency)',
    created_date TIMESTAMP NOT NULL COMMENT 'Record creation timestamp',
    last_modified_date TIMESTAMP COMMENT 'Last modification timestamp'
) COMMENT = 'Table to store price snapshot data';

-- Create indexes for `price_snapshot`
CREATE INDEX idx_price_snapshot_created_date ON price_snapshot (created_date);
CREATE INDEX idx_price_snapshot_updated_iso ON price_snapshot (updated_iso);
```

### 2. `price` Table
```sql
CREATE TABLE price (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary key for price table',
    snapshot_id BIGINT NOT NULL COMMENT 'Foreign key referencing price_snapshot table',
    code VARCHAR(10) COMMENT 'Currency code (e.g., USD, GBP)',
    symbol VARCHAR(10) COMMENT 'Currency symbol (e.g., $, £)',
    rate VARCHAR(50) COMMENT 'Exchange rate as a string',
    description VARCHAR(50) COMMENT 'Description of the currency',
    rate_float DECIMAL(18, 6) COMMENT 'Exchange rate as a decimal value',
    created_date TIMESTAMP NOT NULL COMMENT 'Record creation timestamp',
    last_modified_date TIMESTAMP COMMENT 'Last modification timestamp',
    FOREIGN KEY (snapshot_id) REFERENCES price_snapshot(id) ON DELETE CASCADE
) COMMENT = 'Table to store price details for each snapshot';

-- Create indexes for `price`
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