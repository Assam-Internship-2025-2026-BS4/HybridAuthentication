package in.bank.hdfc.auth.hybridAuth.entity.dev;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Dev")
@Getter
@Setter
public class DevEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

}
