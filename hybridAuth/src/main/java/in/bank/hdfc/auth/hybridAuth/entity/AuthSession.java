package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "auth_sessions")
@Getter
@Setter
@NoArgsConstructor
public class AuthSession {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sessionToken;

    // 🔹 NEW FIELD FOR QR AUTH
    @Column(unique = true)
    private String qrToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthSessionStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    private Instant approvedAt;

    public AuthSession(String sessionToken, Instant expiresAt) {
        this.sessionToken = sessionToken;
        this.status = AuthSessionStatus.PENDING;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
    }
}