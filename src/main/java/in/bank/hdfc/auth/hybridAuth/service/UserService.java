package in.bank.hdfc.auth.hybridAuth.service;

import java.util.List;
// import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import in.bank.hdfc.auth.hybridAuth.dto.CreateUserRequest;
import in.bank.hdfc.auth.hybridAuth.dto.IdentifyUserResponse;
import in.bank.hdfc.auth.hybridAuth.dto.UserResponse;
import in.bank.hdfc.auth.hybridAuth.entity.AuthSession;
import in.bank.hdfc.auth.hybridAuth.entity.User;
import in.bank.hdfc.auth.hybridAuth.repository.AuthSessionRepository;
import in.bank.hdfc.auth.hybridAuth.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsRepository userRepository;

    private final AuthSessionRepository repository;

    public UserResponse fetchUserDetails(String sessionId) {

        AuthSession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Invalid Session Id"));

        User user = userRepository.findByMobileNumber(session.getMobileNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // if(session.getExpiresAt().isBefore(LocalDateTime.now()))
        // throw new RuntimeException("Session expired");

        return new UserResponse(
                user.getCustomerId(),
                user.getAccountNumber(),
                user.getName(),
                user.getMobileNumber(),
                user.getDOB(),
                user.getPanNumber(),
                user.getEmail(),
                user.getWhatsAppRegistered());
    }

    public List<UserResponse> fetchAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(
                        user.getCustomerId(),
                        user.getAccountNumber(),
                        user.getName(),
                        user.getMobileNumber(),
                        user.getDOB(),
                        user.getPanNumber(),
                        user.getEmail(),
                        user.getWhatsAppRegistered()
                ))
                .toList();
    }

    public IdentifyUserResponse identifyUser(String mobileNumber){
        return userRepository.findByMobileNumber(mobileNumber)
        .map(user -> new IdentifyUserResponse(
                true,
                Boolean.TRUE.equals(user.getWhatsAppRegistered()),
                user.getName()
        ))
        .orElse(new IdentifyUserResponse(
                false,
                false,
                null
        ));
    }

    public UserResponse createUser(CreateUserRequest request) {

        User user = User.builder()
                .customerId(UUID.randomUUID().toString())
                .name(request.getName())
                .mobileNumber(request.getMobileNumber())
                .DOB(request.getDOB())
                .panNumber(request.getPanNumber())
                .email(request.getEmail())
                .whatsAppRegistered(request.getWhatsAppRegistered())
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getCustomerId(),
                savedUser.getAccountNumber(),
                savedUser.getName(),
                savedUser.getMobileNumber(),
                savedUser.getDOB(),
                savedUser.getPanNumber(),
                savedUser.getEmail(),
                savedUser.getWhatsAppRegistered());
    }
}
