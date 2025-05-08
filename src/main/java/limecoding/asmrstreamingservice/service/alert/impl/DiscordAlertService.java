package limecoding.asmrstreamingservice.service.alert.impl;

import limecoding.asmrstreamingservice.dto.alert.DiscordWebHookMessage;
import limecoding.asmrstreamingservice.service.alert.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Slf4j
@Service
@ConditionalOnProperty(name = "alert.discord.enabled", havingValue = "true")
public class DiscordAlertService implements AlertService {

    @Value("${alert.discord.url}")
    private String discordUrl;
    private final RestTemplate restTemplate;

    @Override
    public void sendAlert(String message) {

        DiscordWebHookMessage payload = new DiscordWebHookMessage(message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DiscordWebHookMessage> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(discordUrl, request, String.class);
        } catch (Exception e) {
            log.error("디스코드 전송 실패 : {}", e.getMessage());
        }
    }
}
