package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String mobile;

    @Column(name = "pan_hash", nullable = false)
    private String panHash;

    @Column(name = "dob_encrypted", nullable = false)
    private String dobEncrypted;

    @Column(name = "whatsapp_registered")
    private boolean whatsappRegistered;

    private boolean active;
}