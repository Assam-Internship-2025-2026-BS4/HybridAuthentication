package in.bank.hdfc.auth.hybridAuth.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "whatsapp_login_sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WhatsappLoginSession {

    @Id
    private String sessionId;

    private String customerId;

    private String deviceToken;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
