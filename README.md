# TCDD-Train-Availability-Notification
# 🚆 Train Schedules API

This project is a **Spring Boot REST API** application that retrieves TCDD train schedules based on user-defined parameters.  
Additionally, if available seat count is greater than zero, it **automatically sends train details via email in JSON format**.

---

## 📌 **Features**
✅ Fetches train schedules from TCDD API  
✅ Allows querying trains via **Swagger UI**  
✅ **Sends email notifications if available seats are greater than zero**  
✅ Supports **JSON format for email notifications**  
✅ Uses Spring Boot and Spring Mail for email automation  

---

## 🛠 **Setup and Run**

### **1️⃣ Install Dependencies**
This project uses **Maven**. Install dependencies using:

```sh
mvn clean install

2️⃣ Run the Application
```sh
mvn spring-boot:run

Once the application starts successfully, you can test it via Swagger UI:

📌 Swagger UI: http://localhost:8080/swagger-ui/index.html

📧 Email Configuration
To enable email notifications, add the following SMTP configuration in application.properties:
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

⚠️ Note: If you are using Gmail SMTP, enable "Less Secure Apps" or use an "App Password" for authentication.

API Usage
Search Train Schedules (Swagger)
GET /api/trains/search

🔗 Example Response
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


📌 If the available seat count is greater than zero, this JSON response is automatically sent to the provided email address.

📜 License
This project is licensed under the MIT License.
