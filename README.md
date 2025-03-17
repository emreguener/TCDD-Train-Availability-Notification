# 🚆 TCDD Train Availability Notification API

This project is a **Spring Boot REST API** that retrieves TCDD train schedules based on user-defined parameters. If available seat count is greater than zero, it **automatically sends train details via email in JSON format**.

---

## 📌 Features
✅ Fetches train schedules from TCDD API  
✅ Allows querying trains via **Swagger UI**  
✅ **Sends email notifications if available seats are greater than zero**  
✅ Supports **JSON format for email notifications**  
✅ Uses **Spring Boot, Spring Mail**, and **Maven**  

---

## 🛠 Setup and Run

### **1️⃣ Install Dependencies**
This project uses **Maven**. Install dependencies using:

```sh
mvn clean install
```

### **2️⃣ Run the Application**
```sh
mvn spring-boot:run
```

Once the application starts successfully, you can test it via Swagger UI:

📌 **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📧 Email Configuration
To enable email notifications, add the following **SMTP configuration** in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

⚠️ **Note**: If you are using Gmail SMTP, enable **App Passwords** for authentication instead of your regular password.

---

## 🔍 API Usage

### **1️⃣ Search Train Schedules (Swagger UI)**
Endpoint:
```sh
GET /api/trains/search
```

### **2️⃣ Example Request (via Swagger UI)**

Fill in the parameters as shown below:

```sh
GET /api/trains/search?gidisTarih=2025-03-18T10:00:00&gidisTarihSon=2025-03-20T10:00:00&binisIstasyonu=ESKİŞEHİR&inisIstasyonu=BALIKESİR&binisIstasyonId=93&inisIstasyonId=1159&koltukTipi=EKONOMİ&email=tcdddenemejava@gmail.com
```

This request will fetch available train schedules for the specified parameters and send an email if available seats are greater than zero.


---

### **3️⃣ Example Response (JSON Format)**
```json
[
  {
    "trenAdi": "72018 EGE EKSPRESİ",
    "kalkisTarihi": "2025-03-19T06:05:00",
    "binisIstasyonu": "ESKİŞEHİR",
    "varisIstasyonu": "BALIKESİR",
    "koltukTipi": "EKONOMİ",
    "bosKoltukSayisi": 206
  },
  {
    "trenAdi": "22006 İZMİR MAVİ",
    "kalkisTarihi": "2025-03-18T17:00:00",
    "binisIstasyonu": "ESKİŞEHİR",
    "varisIstasyonu": "BALIKESİR",
    "koltukTipi": "EKONOMİ",
    "bosKoltukSayisi": 238
  }
]
```

📌 **If the available seat count is greater than zero, this JSON response is automatically sent to the provided email address.**

---

## 📜 License
This project is licensed under the **MIT License**.

