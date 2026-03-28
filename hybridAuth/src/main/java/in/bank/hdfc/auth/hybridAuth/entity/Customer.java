package in.bank.hdfc.auth.hybridAuth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "customer_id", updatable = false, nullable = false, length = 12)
    private String customerId;

    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(name = "phone_no", nullable = false, length = 10, unique = true)
    private String phoneNo;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "pan", nullable = false, length = 10, unique = true)
    private String pan;

    @Column(name = "whatsapp_enabled")
    private Boolean whatsappEnabled = false;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (customerId == null) {
            customerId = "CUST" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String v) { this.customerId = v; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String v) { this.customerName = v; }
    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String v) { this.phoneNo = v; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate v) { this.dob = v; }
    public String getPan() { return pan; }
    public void setPan(String v) { this.pan = v; }
    public Boolean getWhatsappEnabled() { return whatsappEnabled; }
    public void setWhatsappEnabled(Boolean v) { this.whatsappEnabled = v; }
    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(LocalDateTime v) { this.lastLoginTime = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}
