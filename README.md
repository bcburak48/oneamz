# OneAMZ Inventory Management System

## Gereksinimler

- Java JDK 22
- Maven
- PostgreSQL

## Kurulum ve Çalıştırma

### PostgreSQL Veritabanı Ayarı

1. PostgreSQL üzerinde `inventory_db` adında bir veritabanı oluşturun:
   ```sql
   CREATE DATABASE inventory_db;
2. src/main/resources/application.properties dosyanızı aşağıdaki gibi güncelleyin:
   
  spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_db
  spring.datasource.username=postgres
  spring.datasource.password=yourpassword
  spring.jpa.hibernate.ddl-auto=update
  
3.git clone https://github.com/bcburak48/oneamz.git
  
  cd oneamz

4.mvn spring-boot:run

5.http://localhost:8080/swagger-ui/index.html
