# ĐỒ ÁN TỐT NGHIỆP: NUTRIBUDGET
## Ứng Dụng Di Động Đề Xuất Thực Đơn Dựa Trên Mục Tiêu Dinh Dưỡng Và Ngân Sách

- **Sinh viên thực hiện:** Phạm Nguyễn Lan Anh
- **MSSV:** 5231000069
- **Lớp:** 523100B

---

## 1. Giới Thiệu & Mục Tiêu Đề Tài

Xây dựng ứng dụng di động hỗ trợ người dùng quản lý chế độ ăn uống, theo dõi dinh dưỡng và kiểm soát chi phí thực phẩm hàng ngày. Hệ thống tự động phân tích mục tiêu dinh dưỡng cá nhân kết hợp với hạn mức ngân sách để tính toán và đề xuất các phương án thực đơn tối ưu nhất.

### Tính năng chính:
- **Xác thực:** Đăng ký, đăng nhập bảo mật với JSON Web Token (JWT).
- **Hồ sơ cá nhân & Thể trạng:** Quản lý thông tin thể trạng (chiều cao, cân nặng), tự động tính toán chỉ số BMI và phân loại thể trạng.
- **Mục tiêu dinh dưỡng:** Thiết lập calo mục tiêu hàng ngày, tỷ lệ macronutrients (protein, carb, fat) phù hợp với nhu cầu (Giảm cân, Giữ cân, Tăng cân, Tăng cơ).
- **Thiết lập ngân sách:** Cài đặt định mức chi tiêu cho bữa ăn theo ngày, tuần, tháng.
- **Đề xuất thực đơn tự động:** Thuật toán thông minh tổng hợp từ CSDL món ăn thực tế và đề xuất 3 phương án đa dạng:
  1. *Thực đơn Cân đối tối ưu* (Tối ưu calo và dinh dưỡng).
  2. *Thực đơn Tiết kiệm* (Tối đa hóa chi phí trong ngân sách).
  3. *Thực đơn Giàu đạm / Tăng cơ* (Ưu tiên lượng protein cao).
- **Xem chi tiết thực đơn:** Thống kê khẩu phần, calo, protein, carb, fat và chi phí từng món.
- **Điều chỉnh thực đơn (Đổi món):** Cho phép đổi món ăn trong từng bữa sang các món ăn thay thế phù hợp, hệ thống tự động tính toán lại dinh dưỡng và chi phí.
- **Tổng quan chi phí:** So sánh chi phí thực đơn với ngân sách ngày, tính số tiền dư/vượt, chi phí theo từng bữa ăn.
- **Danh sách nguyên liệu cần mua:** Tự động trích xuất nguyên liệu từ tất cả món ăn trong thực đơn, tự động cộng dồn các nguyên liệu trùng nhau, tính đơn giá và tổng chi phí đi chợ dự kiến.
- **Lịch sử thực đơn:** Tự động lưu vết các thao tác (Tạo mới, Cập nhật, Đổi món) kèm ngày áp dụng và trạng thái.

---

## 2. Công Nghệ Sử Dụng

- **Frontend (Mobile App):**
  - Nền tảng: Android (Native Java)
  - IDE: Android Studio
  - Mạng & REST API Client: Retrofit 2, OkHttp 3, Gson
  - Giao diện: XML Layouts, Material Design Components, Figma
- **Backend (REST API Server):**
  - Framework: Spring Boot 3.x (Java 17)
  - Bảo mật: Spring Security 6, JWT (io.jsonwebtoken)
  - ORM / Persistence: Spring Data JPA, Hibernate
  - Build tool: Apache Maven
- **Cơ sở dữ liệu:**
  - Hệ quản trị CSDL: MySQL 8.x / MariaDB (XAMPP)
  - Tên CSDL: `nutribudget` (gồm 9 bảng liên kết quan hệ khóa ngoại)
- **Thiết kế & Mô hình hóa:**
  - UI/UX: Figma
  - Mô hình UML: StarUML

---

## 3. Cấu Trúc Thư Mục Dự Án

```text
D:\Pham_Nguyen_Lan_Anh\
├── app\                          # Mã nguồn ứng dụng Android Client (Java)
│   ├── src\main\java\com\example\phamnguyenlananh\
│   │   ├── data\                 # Repositories, API interfaces, Data models
│   │   ├── ui\                   # Activities, Fragments, Adapters
│   │   │   ├── auth\             # LoginActivity, RegisterActivity
│   │   │   ├── main\             # MainActivity, Home, Meal, History, Profile
│   │   │   ├── meal\             # MealDetail, AdjustMeal, CostOverview, GroceryList...
│   │   │   └── profile\          # ProfileDetail, NutritionGoals, BudgetSettings...
│   │   └── MainActivity.java
│   └── src\main\res\             # XML layouts, drawables, values, navigation
├── nutribudget-backend\          # Mã nguồn Spring Boot REST API
│   ├── pom.xml                   # Cấu hình dependencies Maven
│   └── src\main\
│       ├── java\com\nutribudget\api\
│       │   ├── controller\       # REST Controllers (Auth, User, Menu, Budget...)
│       │   ├── dto\              # Request & Response Data Transfer Objects
│       │   ├── entity\           # JPA Entities (9 bảng CSDL)
│       │   ├── repository\       # Spring Data JPA Repositories
│       │   ├── security\         # JWT Token Provider, Auth Filter, SecurityConfig
│       │   └── service\          # Xử lý nghiệp vụ & thuật toán đề xuất
│       └── resources\
│           ├── application.properties # Cấu hình cổng, DB, JWT
│           ├── schema.sql        # Script tạo 9 bảng CSDL
│           └── data.sql          # Dữ liệu khởi tạo món ăn, nguyên liệu mẫu
├── build.gradle                  # Root Gradle build script
└── README.md
```

---

## 4. Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### Bước 1: Khởi động Cơ sở dữ liệu MySQL
1. Mở **XAMPP Control Panel** và nhấn **Start** tại mục **MySQL** (hoặc khởi động dịch vụ MySQL Server trên máy tính).
2. Kiểm tra cổng mặc định `3306`.
3. Cơ sở dữ liệu tên: `nutribudget`.
   - Nếu dùng XAMPP mặc định: Username là `root`, Password để **trống**.
   - CSDL và các bảng được tự động khởi tạo và nạp dữ liệu mẫu khi Backend chạy.

---

### Bước 2: Chạy Backend Spring Boot
Mở terminal/command prompt tại thư mục máy chủ và thực thi:

```bash
cd D:\Pham_Nguyen_Lan_Anh\nutribudget-backend
mvn spring-boot:run
```

*(Hoặc mở thư mục `nutribudget-backend` bằng IntelliJ IDEA / Eclipse và nhấn Run class `NutriBudgetApplication.java`)*

- Khi khởi động thành công, server sẽ lắng nghe tại: `http://localhost:8080`.
- Kiểm tra trạng thái API nhanh qua trình duyệt: `http://localhost:8080/api/dishes`.

---

### Bước 3: Cấu hình địa chỉ IP trong Android App
Địa chỉ API được định cấu hình tại file:
`app/src/main/java/com/example/phamnguyenlananh/data/api/ApiConfig.java`

- **Nếu chạy trên Android Emulator (Máy ảo Android Studio):**
  Giữ nguyên mặc định:
  ```java
  public static final String BASE_URL = "http://10.0.2.2:8080/";
  ```
  *(10.0.2.2 là địa chỉ loopback đặc biệt giúp máy ảo Android kết nối tới localhost của máy tính host).*

- **Nếu chạy trên Thiết bị thật (Điện thoại cắm cáp / qua Wi-Fi):**
  1. Đảm bảo điện thoại và máy tính kết nối **chung một mạng Wi-Fi**.
  2. Mở Command Prompt trên máy tính, gõ `ipconfig` để lấy địa chỉ IPv4 (Ví dụ: `192.168.1.15`).
  3. Cập nhật `ApiConfig.java`:
     ```java
     public static final String BASE_URL = "http://192.168.1.15:8080/";
     ```

---

### Bước 4: Khởi chạy Ứng dụng Android

#### Cách 1: Sử dụng Android Studio (Khuyến nghị)
1. Mở **Android Studio**.
2. Chọn **Open** và dẫn tới thư mục gốc của dự án: `D:\Pham_Nguyen_Lan_Anh`.
3. Chờ Android Studio đồng bộ xong Gradle (Sync Project with Gradle Files).
4. Chọn thiết bị chạy (Android Virtual Device - AVD hoặc Điện thoại cắm cáp đã bật USB Debugging).
5. Nhấn nút **Run** (biểu tượng tam giác xanh ▶ hoặc phím tắt `Shift + F10`).

#### Cách 2: Sử dụng dòng lệnh (Terminal / PowerShell)
Tại thư mục gốc `D:\Pham_Nguyen_Lan_Anh`, chạy:
```bash
# Build file APK Debug
.\gradlew.bat assembleDebug

# Cài đặt trực tiếp lên thiết bị đang kết nối
.\gradlew.bat installDebug
```

---

## 5. Tài Khoản Thử Nghiệm (Demo Accounts)

Hệ thống đã có sẵn các tài khoản mẫu trong cơ sở dữ liệu để đăng nhập và trải nghiệm ngay:

| Email | Mật khẩu | Ghi chú |
| :--- | :--- | :--- |
| `lananh@nutribudget.com` | `123456` | Tài khoản chính có đầy đủ mục tiêu, ngân sách và lịch sử thực đơn |
| `nguyenvana@gmail.com` | `123456` | Tài khoản mẫu 2 |

*Người dùng cũng có thể nhấn **"Đăng ký ngay"** trên màn hình để tạo một tài khoản hoàn toàn mới.*

---

## 6. Kịch Bản Chạy Demo Toàn Bộ Chức Năng (Walkthrough)

1. **Đăng nhập / Đăng ký:** Mở ứng dụng, nhập email và mật khẩu hoặc đăng ký tài khoản mới.
2. **Cập nhật thể trạng & BMI:** Vào tab **Cá nhân** $\rightarrow$ **Thông tin cá nhân** $\rightarrow$ Cập nhật Chiều cao, Cân nặng $\rightarrow$ Ứng dụng tự động tính lại BMI và hiển thị trạng thái thể trạng.
3. **Thiết lập mục tiêu dinh dưỡng:** Tab **Cá nhân** $\rightarrow$ **Mục tiêu dinh dưỡng** $\rightarrow$ Chọn mục tiêu (Tăng cân / Giảm cân / Giữ cân), điều chỉnh Calo và tỷ lệ Macronutrients $\rightarrow$ Nhấn **Lưu mục tiêu**.
4. **Thiết lập ngân sách:** Tab **Cá nhân** $\rightarrow$ **Thiết lập ngân sách** $\rightarrow$ Đặt ngân sách ngày, tuần, tháng $\rightarrow$ Nhấn **Lưu ngân sách**.
5. **Nhận đề xuất thực đơn:** Chuyển sang tab **Thực đơn** $\rightarrow$ Hệ thống tự động gọi API lấy 3 phương án gợi ý theo đúng thể trạng và ngân sách vừa cài đặt.
6. **Xem chi tiết & Điều chỉnh thực đơn:** Bấm chọn một phương án $\rightarrow$ Màn hình **Chi tiết thực đơn** hiển thị các món ăn theo bữa $\rightarrow$ Nhấn **"Điều chỉnh thực đơn"** $\rightarrow$ Chọn một món để thay thế bằng món khác trong danh sách $\rightarrow$ Xem calo và giá tiền tự động tính lại.
7. **Xem tổng quan chi phí & Nguyên liệu cần mua:**
   - Nhấn **"Xem tổng chi phí"**: Phân tích chi tiết tiền theo từng bữa, số tiền còn dư hoặc vượt ngân sách.
   - Nhấn **"Nguyên liệu cần mua"**: Xem danh sách các nguyên liệu đi chợ đã được tự động cộng dồn số lượng và tính tổng số tiền.
8. **Lưu thực đơn & Xem lịch sử:** Nhấn **"Lưu thực đơn"** $\rightarrow$ Chuyển sang tab **Lịch sử** để xem danh sách các thực đơn đã áp dụng và nhật ký các thao tác (`TAO_MOI`, `DOI_MON`).
