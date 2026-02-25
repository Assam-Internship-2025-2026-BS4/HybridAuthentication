package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "otp_sessions")
@Getter
@Setter
public class OtpSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mobile;

    @Column(name = "otp_hash", nullable = false)
    private String otpHash;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private int attempts;

    // 🔗 LINK TO AUTH SESSION (CRITICAL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_session_id", nullable = false)
    private AuthSession authSession;
}