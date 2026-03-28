# 🏦 Net Banking Authentication Service (Backend)

This project is the backend authentication service for the Net Banking system.  
It provides secure, multi-channel authentication journeys with session tracking and token-based access.

Supported journeys:
- 📲 WhatsApp Push Authentication
- 🔐 OTP-Based Authentication
- 📷 QR-Based Authentication
- 🔁 Session Polling for real-time status updates

---

## 🚀 Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JWT (token generation and validation)
- Maven

---

## 🏗 Architecture

Layered backend architecture:

Controller → Service → Repository → Database

Design highlights:
- Clear separation of business logic and persistence
- Session-first authentication model for OTP, WA, and QR journeys
- Rate-limiting and expiry handling for secure flow control
- External providers (WhatsApp/SMS) can be integrated through service adapters

Detailed docs:
- [Documentation/README.md](Documentation/README.md)
- [Documentation/Diagrams/README.md](Documentation/Diagrams/README.md)
- [Documentation/Diagrams/System/System-Architecture.md](Documentation/Diagrams/System/System-Architecture.md)

---

## 📚 Project Documentation

- API reference: [Documentation/API-Docs/HybridAuth-API.md](Documentation/API-Docs/HybridAuth-API.md)
- Postman collection: [Documentation/Postman/Test_APIs.postman_collection.json](Documentation/Postman/Test_APIs.postman_collection.json)
- OTP journey: [Documentation/Diagrams/Auth-Journeys/OTP-Auth-Journey.md](Documentation/Diagrams/Auth-Journeys/OTP-Auth-Journey.md)
- WhatsApp journey: [Documentation/Diagrams/Auth-Journeys/WA-Auth-Journey.md](Documentation/Diagrams/Auth-Journeys/WA-Auth-Journey.md)
- QR journey: [Documentation/Diagrams/Auth-Journeys/QR-Auth-Journey.md](Documentation/Diagrams/Auth-Journeys/QR-Auth-Journey.md)

---

## ▶️ Run the Project

Prerequisites:
- Java 21
- Maven
- PostgreSQL running locally

Run command:
mvn spring-boot:run

Base URL:
http://localhost:8080

---

## 🎯 Key Features

- Backend authentication engine for Net Banking
- Unified session lifecycle across OTP, WA, and QR journeys
- WhatsApp-first login for eligible users
- OTP fallback flow with retry and expiry controls
- QR desktop-to-mobile approval flow
- Polling-based status retrieval and token issuance
- Production-oriented layered design ready for extension

---

## ✅ Submission Notes

This repository includes:
- API documentation in Markdown
- Postman collection for API testing
- System and auth-journey architecture diagrams
- Modular code structure suitable for review and scale