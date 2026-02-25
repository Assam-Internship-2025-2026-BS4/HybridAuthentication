package in.bank.hdfc.auth.hybridAuth.controller.dev;

import in.bank.hdfc.auth.hybridAuth.dto.CreateUserRequest;
import in.bank.hdfc.auth.hybridAuth.entity.User;
import in.bank.hdfc.auth.hybridAuth.repository.UserRepository;
import in.bank.hdfc.auth.hybridAuth.util.DobCryptoUtil;
import in.bank.hdfc.auth.hybridAuth.util.PanHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/dev")
@RequiredArgsConstructor
public class DevUserController {

    private final UserRepository userRepository;

    @PostMapping("/create-user")
    public String createUser(@RequestBody CreateUserRequest req) {

        User user = new User();
        user.setMobile(req.getMobile());
        user.setPanHash(PanHashUtil.hash(req.getPan()));
        user.setDobEncrypted(DobCryptoUtil.encrypt(req.getDob()));
        user.setWhatsappRegistered(req.isWhatsappRegistered());
        user.setActive(req.isActive());

        userRepository.save(user);

        return "USER CREATED";
    }


}