package in.bank.hdfc.auth.hybridAuth.service;

import in.bank.hdfc.auth.hybridAuth.dto.IdentifyUserRequest;
import in.bank.hdfc.auth.hybridAuth.dto.IdentifyUserResponse;
import in.bank.hdfc.auth.hybridAuth.entity.User;
import in.bank.hdfc.auth.hybridAuth.repository.UserRepository;
import in.bank.hdfc.auth.hybridAuth.util.DobCryptoUtil;
import in.bank.hdfc.auth.hybridAuth.util.PanHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserIdentifyService {

    private final UserRepository userRepository;

    public IdentifyUserResponse identify(IdentifyUserRequest request) {

        Optional<User> userOpt =
                userRepository.findByMobile(request.getMobile());

        if (userOpt.isEmpty()) {
            return new IdentifyUserResponse(
                    "FAILED",
                    "USER_NOT_FOUND",
                    false
            );
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            return new IdentifyUserResponse(
                    "FAILED",
                    "USER_INACTIVE",
                    false
            );
        }

        boolean matched = switch (request.getIdType()) {

            case "PAN" -> {
                String hashedInputPan =
                        PanHashUtil.hash(request.getIdValue());
                System.out.println("INPUT PAN        : " + request.getIdValue());
                System.out.println("HASHED INPUT PAN : " + hashedInputPan);
                System.out.println("DB PAN HASH      : " + user.getPanHash());
                yield hashedInputPan.equals(user.getPanHash());
            }

            case "DOB" -> {
                String decryptedDob =
                        DobCryptoUtil.decrypt(user.getDobEncrypted());
                System.out.println("DECRYPTED DOB : " + decryptedDob);
                System.out.println("INPUT DOB     : " + request.getIdValue());
                yield decryptedDob.equals(request.getIdValue());
            }

            default -> false;
        };

        if (!matched) {
            return new IdentifyUserResponse(
                    "FAILED",
                    "IDENTITY_MISMATCH",
                    false
            );
        }

        return new IdentifyUserResponse(
                "VERIFIED",
                null,
                user.isWhatsappRegistered()
        );
    }
}