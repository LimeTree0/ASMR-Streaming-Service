package limecoding.asmrstreamingservice.controller;

import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.dto.subscribe.CheckSubscribeDTO;
import limecoding.asmrstreamingservice.dto.subscribe.RequestSubscribeDTO;
import limecoding.asmrstreamingservice.dto.subscribe.SubscribeDTO;
import limecoding.asmrstreamingservice.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> subscribe(
            @AuthenticationPrincipal User user,
            @RequestBody RequestSubscribeDTO requestSubscribeDTO
    ) {
        String username = user.getUsername();

        SubscribeDTO subscribeDTO = subscribeService.subscribe(username, requestSubscribeDTO);

        return ResponseEntity.ok(ApiResponse.success(subscribeDTO));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> cancel(
            @AuthenticationPrincipal User user,
            @RequestBody RequestSubscribeDTO requestSubscribeDTO
    ) {

        log.info("user : {}", user);

        String username = user.getUsername();

        subscribeService.cancel(username, requestSubscribeDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> getSubscribeList(
            @AuthenticationPrincipal User user
    ) {
        String username = user.getUsername();

        List<SubscribeDTO> subscribeList = subscribeService.getSubscribeList(username);

        return ResponseEntity.ok(ApiResponse.success(subscribeList));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<?>> checkSubscribeTargetUser(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "targetUser") String targetUserId
    ) {

        CheckSubscribeDTO checkSubscribeDTO = subscribeService.isSubscribe(user.getUsername(), targetUserId);

        return ResponseEntity.ok(ApiResponse.success(checkSubscribeDTO));

    }
}
