package in.bank.hdfc.auth.hybridAuth.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ExpiryScheduler {

    private final SessionLookupService sessionLookupService;

    public ExpiryScheduler(SessionLookupService sessionLookupService) {
        this.sessionLookupService = sessionLookupService;
    }

    @Scheduled(fixedDelayString = "${app.auth.expiryScanMillis:60000}")
    public void scanAndExpire() {
        sessionLookupService.expireAll();
    }
}
