

# TestApiDTS

## 📖 Giới thiệu
Dự án **TestApiDTS** là API backend xây dựng bằng **Spring Boot**, cung cấp chức năng quản lý người dùng, vai trò, quyền, xác thực và phân quyền (JWT/OAuth2 Resource Server), cùng xử lý upload file (avatar).

---


## 🔧 Yêu cầu
- **Java 17+**
- **Maven** (hoặc sử dụng wrapper `./mvnw`, `mvnw.cmd` đã tích hợp)
- **MySQL** 
- **Biến môi trường** hoặc `application.properties` thiết lập DB URL, username, password.

---

## 🛠️ Cài đặt môi trường
1. **Cài Java**: Kiểm `java -version`. Nếu chưa, cài JDK 17.
2. **Cài Maven**
3. **MySQL**: Tạo database trên Mysql `testapidts`.
   ```bash
   CREATE DATABASE testapidto;
4. **Clone repository**:
    ```bash
    git clone https://github.com/buivandat275/TestApiDTS.git
5. **Setup visual studio code (nếu dùng)**: cài Spring Boot Extension Pack, Extension pack for java

## ⚙️ Cấu hình dự án 
Cấu hình file .application.properties:
 ```
spring.application.name=TestApiDTS
# DataSource
spring.datasource.url=jdbc:mysql://localhost:3306/testapidts
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
# JWT
buivandat.jwt.base64-secret=${JWT_SECRET:YOUR_BASE64_SECRET}
buivandat.jwt.access-token-validity-in-seconds=86400000
buivandat.jwt.refresh-token-validity-in-seconds=8640000
```
Thay password tùy theo máy bạn.

## ▶️ Chạy dự án
./mvnw clean package -DskipTests
./mvnw spring-boot:run

Nên để database trắng dữ liệu, khi chạy dự án lên sẽ tự tạo các bảng trong database đồng thời sẽ chạy vào file DatabaseInitializer.java để tự động tạo data mẫu. 

Đồng thời tạo 1 tài khoản admin full quyền.
admin@gmail.com
123456

Test các API qua postman:
[https://postman/testapidto](https://lively-sunset-969196.postman.co/workspace/SpringBoot~eda77f3b-88ab-4807-b012-bb4cbb9cda25/collection/39471641-7cd8df0a-3fac-445b-aed0-eb7a53cffd2a?action=share&creator=39471641)  
