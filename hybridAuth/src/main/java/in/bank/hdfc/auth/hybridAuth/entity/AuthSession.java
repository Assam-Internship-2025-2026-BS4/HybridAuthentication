package in.bank.hdfc.auth.hybridAuth.entity;

import java.time.LocalDateTime;

import in.bank.hdfc.auth.hybridAuth.enums.AuthType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSession{

        @Id 
        @GeneratedValue(strategy = GenerationType.UUID) 
        private String sessionId;

        private String mobileNumber;

        @Enumerated(EnumType.STRING)
        private AuthType authType;

        private String status;

        private LocalDateTime createdAt;
        
        private LocalDateTime expiresAt;

        private String otpHash; 

        private String qrToken;
}
