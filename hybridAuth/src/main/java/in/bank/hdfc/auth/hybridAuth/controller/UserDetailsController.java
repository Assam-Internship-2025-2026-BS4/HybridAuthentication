package in.bank.hdfc.auth.hybridAuth.controller;

import in.bank.hdfc.auth.hybridAuth.dto.IdentifyUserRequest;
import in.bank.hdfc.auth.hybridAuth.dto.IdentifyUserResponse;
import in.bank.hdfc.auth.hybridAuth.dto.UserDetailsFetchResponse;
import in.bank.hdfc.auth.hybridAuth.service.UserDetailsService;
import in.bank.hdfc.auth.hybridAuth.service.UserIdentifyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/details")
@RequiredArgsConstructor
public class UserDetailsController {

    private final UserIdentifyService identifyService;
    private final UserDetailsService userDetailsService;
    @PostMapping("/identify")
    public ResponseEntity<IdentifyUserResponse> identifyUser(
            @RequestBody @Valid IdentifyUserRequest request
    ) {
        return ResponseEntity.ok(
                identifyService.identify(request)
        );
    }

    @GetMapping("/fetch")
    public ResponseEntity<UserDetailsFetchResponse> fetchUserDetails(
            @RequestParam String mobile
    ) {
        return ResponseEntity.ok(
                userDetailsService.fetchUserDetails(mobile)
        );
    }

}