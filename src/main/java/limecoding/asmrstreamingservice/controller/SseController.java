
package limecoding.asmrstreamingservice.controller;

import limecoding.asmrstreamingservice.dto.user.UserDto;
import limecoding.asmrstreamingservice.repository.EmitterRepository;
import limecoding.asmrstreamingservice.service.NotificationService;
import limecoding.asmrstreamingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscribe")
public class SseController {

    private final EmitterRepository emitterRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal User user) {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60); // 1시간 타임아웃

        String username = user.getUsername();

        UserDto userDto = userService.findUserByUserId(username);

        emitterRepository.save(userDto.getId(), emitter);

        emitter.onTimeout(() -> emitterRepository.remove(userDto.getId()));
        emitter.onCompletion(() -> emitterRepository.remove(userDto.getId()));

        try {
            emitter.send(SseEmitter.event().name("connect").data("SSE 연결됨"));
            emitter.send(SseEmitter.event().name("unread").data(notificationService.findNotificationUnRead(username)));
        } catch (IOException e) {
            emitterRepository.remove(userDto.getId());
        }

        return emitter;
    }
}