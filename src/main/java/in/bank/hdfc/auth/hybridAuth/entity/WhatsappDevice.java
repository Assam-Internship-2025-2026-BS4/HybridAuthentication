package in.bank.hdfc.auth.hybridAuth.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "whatsapp_devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WhatsappDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String customerId;

    private String deviceId;

    private String deviceToken;

    private String deviceModel;
}
