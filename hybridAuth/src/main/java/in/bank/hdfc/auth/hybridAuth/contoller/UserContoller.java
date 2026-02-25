package in.bank.hdfc.auth.hybridAuth.contoller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bank.hdfc.auth.hybridAuth.dto.AuthInitRequest;
import in.bank.hdfc.auth.hybridAuth.dto.CreateUserRequest;
import in.bank.hdfc.auth.hybridAuth.dto.FetchUserRequest;
import in.bank.hdfc.auth.hybridAuth.dto.IdentifyUserResponse;
import in.bank.hdfc.auth.hybridAuth.dto.UserResponse;
import in.bank.hdfc.auth.hybridAuth.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserContoller {

    private final UserService userService;

    @PostMapping("/user/details/fetch")
    public UserResponse fetchUser(@RequestBody FetchUserRequest request) {
        return userService.fetchUserDetails(request.getSessionId());
    }

    @PostMapping("/user/create")
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("users/all")
    public List<UserResponse> fetchAllUsers() {

        List<UserResponse> users = userService.fetchAllUsers();

        return users;
    }

    @PostMapping("/user/details/identify")
    public IdentifyUserResponse identifyUser(
            @RequestBody AuthInitRequest request) {

        return userService.identifyUser(request.getMobileNumber());
    }
}
