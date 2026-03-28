# HybridAuth Complete System Architecture

```mermaid
---
config:
  theme: redux-color
  look: neo
---
flowchart TB
  subgraph Client[Client and Channel Layer]
    NB[Net Banking Web UI]
    MB[HDFC Mobile App]
    WA[WhatsApp Channel]
  end

  subgraph API[API Layer - Spring Boot Controllers]
    AuthInitCtl[AuthInitController]
    UserIdentifyCtl[UserIdentifyController]
    OtpCtl[OtpController]
    QrCtl[QrController]
    WaCtl[WaAuthController]
    SessionCtl[SessionController]
    UserDetailsCtl[UserDetailsController]
  end

  subgraph Service[Service and Utility Layer]
    OtpSvc[OtpService]
    SessionSvc[SessionLookupService]
    JwtUtil[JwtUtil]
    RateSvc[RateLimiterService]
    ExpirySvc[ExpiryScheduler]
    BrowserUtil[BrowserUtil]
  end

  subgraph Repo[Repository Layer]
    CustomerRepo[CustomerRepository]
    OtpRepo[OtpSessionRepository]
    QrRepo[QrSessionRepository]
    WaRepo[WaSessionRepository]
  end

  DB[(PostgreSQL)]
  SmsProvider[(SMS or OTP Provider)]
  WaProvider[(WhatsApp Provider or Webhook Source)]

  NB -->|POST /api/v1/auth/init| AuthInitCtl
  NB -->|POST /api/v1/user/details/identify| UserIdentifyCtl
  UserIdentifyCtl --> CustomerRepo
  CustomerRepo --> DB

  NB -->|WA eligible: POST /api/v1/auth/wa/init| WaCtl
  NB -->|OTP fallback: POST /api/v1/auth/otp/init| OtpCtl

  NB -->|POST /api/v1/auth/qr/generate| QrCtl
  MB -->|POST /api/v1/auth/qr/validate| QrCtl
  MB -->|POST /api/v1/auth/session/approve or reject| SessionCtl
  NB -->|POST /api/v1/auth/session/fetch poll every 3 sec| SessionCtl

  OtpCtl --> OtpSvc
  OtpCtl --> SessionSvc
  OtpCtl --> RateSvc
  OtpCtl --> JwtUtil
  OtpCtl --> BrowserUtil

  QrCtl --> SessionSvc
  QrCtl --> RateSvc
  QrCtl --> BrowserUtil

  WaCtl --> SessionSvc
  WaCtl --> RateSvc
  WaCtl --> BrowserUtil

  SessionCtl --> SessionSvc
  SessionCtl --> JwtUtil
  SessionCtl --> CustomerRepo

  UserDetailsCtl --> SessionSvc
  UserDetailsCtl --> JwtUtil
  UserDetailsCtl --> CustomerRepo

  OtpSvc --> OtpRepo
  SessionSvc --> OtpRepo
  SessionSvc --> QrRepo
  SessionSvc --> WaRepo

  OtpCtl --> OtpRepo
  QrCtl --> QrRepo
  WaCtl --> WaRepo

  ExpirySvc --> OtpRepo
  ExpirySvc --> QrRepo
  ExpirySvc --> WaRepo

  OtpSvc -->|Send OTP| SmsProvider
  WaCtl -->|Send WA prompt| WaProvider
  WaProvider -->|Webhook YES or NO| WaCtl

  OtpRepo --> DB
  QrRepo --> DB
  WaRepo --> DB
  CustomerRepo --> DB

  NB -->|POST /api/v1/user/details/fetch| UserDetailsCtl
```

## Runtime Behavior Summary

- WhatsApp-first path:
    - Identify user
    - If WhatsApp enabled, initiate WA auth
    - Poll session status
    - On approved, fetch user details and login success

- OTP fallback path:
    - Initiate OTP
    - Validate OTP
    - On valid OTP, fetch user details and login success

- QR path:
    - Generate QR from web
    - Validate QR from mobile app
    - Approve or reject session on mobile
    - Web polls session to finalize login outcome
