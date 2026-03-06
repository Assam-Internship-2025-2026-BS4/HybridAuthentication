package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.Entity;
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
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private String customerId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String accountNumber;

    private String name;

    private String mobileNumber;

    private String DOB;

    private String panNumber;

    private String email;

    private Boolean whatsAppRegistered;
}
