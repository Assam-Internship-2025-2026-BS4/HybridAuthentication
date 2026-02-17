# 🏦 Net Banking Authentication Service (Backend)

This project is the **backend authentication service** for the Net Banking system.
It handles secure login flows using:

* 📲 WhatsApp Push Authentication
* 🔐 OTP-Based Authentication (Fallback)
* 🔁 Session Polling Mechanism

---

## 🚀 Tech Stack

* Spring Boot
* Spring Data JPA
* PostgreSQL
* Lombok

---

## 🏗 Architecture

Layered architecture:

```
Controller → Service → Repository → Database
```

External providers (WhatsApp/SMS) are abstracted via client classes.

---

## ▶️ Run the Project

```bash
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

## 🎯 Key Features

* Backend authentication engine for Net Banking
* Session-based login flow
* WhatsApp-first login for registered users
* OTP fallback mechanism
* Scalable and clean backend design
