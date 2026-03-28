# HybridAuth System Architecture

```mermaid
flowchart LR
    subgraph Client[Client Layer]
      Web[Web Client Browser]
      Mobile[Mobile App]
    end

    subgraph API[API Layer]
      Controllers[Spring Boot Controllers]
    end

    subgraph Service[Service Layer]
      OTP[OTP Service]
      WA[WhatsApp Service]
      QR[QR Service]
      Session[Session Lookup Service]
      JWT[JWT Utility]
      Rate[Rate Limiter Service]
      Scheduler[Expiry Scheduler]
    end

    subgraph Repo[Repository Layer]
      OtpRepo[OTP Repository]
      WaRepo[WA Repository]
      QrRepo[QR Repository]
      UserRepo[User Repository]
    end

    DB[(PostgreSQL)]

    Web -->|HTTP JSON| Controllers
    Mobile -->|Validate scan approve reject| Controllers

    Controllers --> OTP
    Controllers --> WA
    Controllers --> QR
    Controllers --> Session

    OTP --> OtpRepo
    WA --> WaRepo
    QR --> QrRepo
    Session --> OtpRepo
    Session --> WaRepo
    Session --> QrRepo
    Controllers --> UserRepo

    OTP --> JWT
    WA --> JWT
    Session --> JWT

    OTP --> Rate
    WA --> Rate
    QR --> Rate
    Scheduler --> OtpRepo
    Scheduler --> WaRepo
    Scheduler --> QrRepo

    OtpRepo --> DB
    WaRepo --> DB
    QrRepo --> DB
    UserRepo --> DB
```

## Notes

- Controllers expose REST APIs for init, validate, session polling, and user details.
- Session lookup unifies OTP, WA, and QR session state resolution.
- JWT is generated after successful validation or approved status.
