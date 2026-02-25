package in.bank.hdfc.auth.hybridAuth.service;

import in.bank.hdfc.auth.hybridAuth.dto.UserDetailsFetchResponse;
import in.bank.hdfc.auth.hybridAuth.entity.User;
import in.bank.hdfc.auth.hybridAuth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsFetchResponse fetchUserDetails(String mobile) {

        User user = userRepository.findByMobile(mobile)
                .orElseThrow(() ->
                        new RuntimeException("USER_NOT_FOUND"));

        return new UserDetailsFetchResponse(
                user.getMobile(),
                user.isWhatsappRegistered(),
                user.isActive()
        );
    }
}