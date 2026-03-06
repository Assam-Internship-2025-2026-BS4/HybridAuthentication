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
@Table(name = "new_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRSession {

        @Id 
        @GeneratedValue(strategy = GenerationType.UUID) 
        private String qrId;

        private String userAgent;

        private String status;

        private LocalDateTime createdAt;
        
        private LocalDateTime expiresIn;
        
        @Enumerated(EnumType.STRING)
        private AuthType authType;

        private String journeyId; 

        private String journeyName;
}

