package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String customerId;

    private String accountNumber;

    private String name;

    private String mobileNumber;

    private String DOB;

    private String panNumber;

    private String email;

    private Boolean whatsAppRegistered;
}