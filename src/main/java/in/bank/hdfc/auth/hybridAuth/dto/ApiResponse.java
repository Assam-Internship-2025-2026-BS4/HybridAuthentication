package in.bank.hdfc.auth.hybridAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private StatusDTO status;
    private T data;
}
