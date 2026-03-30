# HybridAuth Complete System Architecture

```mermaid
---
config:
  theme: default
  primaryColor: '#E8F4F8'
  primaryBorderColor: '#2C3E50'
  fontSize: 14px
---
graph TB
    subgraph Client["Client Layer"]
        UI["Net Banking Portal"]
        MB["Mobile Banking App"]
        WA["WhatsApp"]
    end

    subgraph Gateway["API Gateway & Security"]
        LB["Load Balancer"]
        CORS["CORS Configuration"]
        RL["Rate Limiter"]
    end

    subgraph Controllers["API Controllers"]
        UserIdent["User Identify<br/>/user/details/identify"]
        OtpCtrl["OTP<br/>/auth/otp/*"]
        QrCtrl["QR Code<br/>/auth/qr/*"]
        WaCtrl["WhatsApp<br/>/auth/wa/*"]
        SessionCtrl["Session<br/>/auth/session/*"]
    end

    subgraph Services["Business Services"]
        UserSvc["User Service"]
        OtpSvc["OTP Service"]
        QrSvc["QR Service"]
        WaSvc["WhatsApp Service"]
        SessionSvc["Session Service"]
        JwtUtil["JWT Utility"]
    end

    subgraph Security["Security & Infrastructure"]
        RateLim["Rate Limiter"]
        Scheduler["Expiry Scheduler<br/>60s scan interval"]
    end

    subgraph Data["Data Layer"]
        Customer["Customer"]
        OtpTable["OTP Session"]
        QrTable["QR Session"]
        WaTable["WhatsApp Session"]
        Database["PostgreSQL Database"]
    end

    subgraph External["Third Party Services"]
        SMS["SMS Provider"]
        WhatsApp_API["WhatsApp API"]
        HDFC_MB_API["HDFC Mobile API"]
    end

    UI --> LB
    MB --> LB
    WA --> LB

    LB --> CORS
    CORS --> RL
    RL --> UserIdent
    RL --> OtpCtrl
    RL --> QrCtrl
    RL --> WaCtrl
    RL --> SessionCtrl

    UserIdent --> UserSvc
    OtpCtrl --> OtpSvc
    QrCtrl --> QrSvc
    WaCtrl --> WaSvc
    SessionCtrl --> SessionSvc

    OtpSvc --> JwtUtil
    QrSvc --> JwtUtil
    WaSvc --> JwtUtil
    SessionSvc --> JwtUtil

    OtpSvc --> RateLim
    QrSvc --> RateLim
    WaSvc --> RateLim

    UserSvc --> Customer
    OtpSvc --> OtpTable
    QrSvc --> QrTable
    WaSvc --> WaTable

    Customer --> Database
    OtpTable --> Database
    QrTable --> Database
    WaTable --> Database

    Scheduler -.->|Monitor| OtpTable
    Scheduler -.->|Monitor| QrTable
    Scheduler -.->|Monitor| WaTable

    OtpSvc -.-> SMS
    WaSvc -.-> WhatsApp_API
    QrSvc -.-> HDFC_MB_API

    classDef clientStyle fill:#FFF9C4,stroke:#F57F17,stroke-width:2px,color:#000
    classDef gatewayStyle fill:#E3F2FD,stroke:#1565C0,stroke-width:2px,color:#000
    classDef controllerStyle fill:#F3E5F5,stroke:#6A1B9A,stroke-width:2px,color:#000
    classDef serviceStyle fill:#E0F2F1,stroke:#00695C,stroke-width:2px,color:#000
    classDef dataStyle fill:#FCE4EC,stroke:#C2185B,stroke-width:2px,color:#000
    classDef externalStyle fill:#F1F8E9,stroke:#558B2F,stroke-width:2px,color:#000

    class UI,MB,WA clientStyle
    class LB,CORS,RL gatewayStyle
    class UserIdent,OtpCtrl,QrCtrl,WaCtrl,SessionCtrl controllerStyle
    class UserSvc,OtpSvc,QrSvc,WaSvc,SessionSvc,JwtUtil,RateLim,Scheduler serviceStyle
    class Customer,OtpTable,QrTable,WaTable,Database dataStyle
    class SMS,WhatsApp_API,HDFC_MB_API externalStyle
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
