package limecoding.asmrstreamingservice.service.alert.impl;

import limecoding.asmrstreamingservice.service.alert.AlertService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "alert.discord.enabled", havingValue = "false")
public class NoAlertService implements AlertService {
    @Override
    public void sendAlert(String message) {
        // Do not Send Message
    }
}