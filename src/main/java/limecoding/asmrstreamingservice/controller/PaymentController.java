package limecoding.asmrstreamingservice.controller;

import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.dto.payment.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@Slf4j
@RequestMapping("/api/v1/payment")
@RestController
public class PaymentController {

    private final RestClient restClient;
    private String tid;

    public PaymentController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://open-api.kakaopay.com").build();
    }

    @PostMapping("/kakao")
    public ApiResponse<Object> kakaoPayment(@RequestBody OrderRequestDTO orderRequestDTO) {

        KakaoPaymentDTO kakaoPaymentDTO = KakaoPaymentDTO.builder()
                .cid("TC0ONETIME")
                .partnerOrderId("partner_order_id")
                .partnerUserId("partner_user_id")
                .itemName("초코파이")
                .quantity(1)
                .totalAmount(2200)
                .taxFreeAmount(0)
                .approvalUrl("https://localhost:8080/api/v1/payment/success?orderId=1")
                .failUrl("https://localhost:8080/api/v1/payment/fail")
                .cancelUrl("https://localhost:8080/api/v1/payment/cancel")
                .build();


        RestClient.RequestBodySpec body = restClient.post()
                .uri("/online/v1/payment/ready")
                .header("Authorization", "SECRET_KEY DEVF362EC0133E8052DC8FBC0525F7FFCDA52DDC")
                .contentType(MediaType.APPLICATION_JSON)
                .body(kakaoPaymentDTO);

        ResponseEntity<KakaoPaymentResponseDTO> response = body.retrieve()
                .toEntity(KakaoPaymentResponseDTO.class);


        tid = response.getBody().getTid();


        return ApiResponse.success(response);
    }

    @GetMapping("/success")
    public void paymentSuccess(
            @RequestParam(name = "orderId") Long orderId,
            @RequestParam(name = "pg_token") String pgToken
    ) {
        log.info("order id : {}", orderId);

        KakaoPaymentApproveDTO approveDTO = KakaoPaymentApproveDTO.builder()
                .cid("TC0ONETIME")
                .tid(tid)
                .partnerOrderId("partner_order_id")
                .partnerUserId("partner_user_id")
                .pgToken(pgToken)
                .build();

        ResponseEntity<KakaoPaymentApproveResponseDTO> entity = restClient.post()
                .uri("/online/v1/payment/approve")
                .header("Authorization", "SECRET_KEY DEVF362EC0133E8052DC8FBC0525F7FFCDA52DDC")
                .contentType(MediaType.APPLICATION_JSON)
                .body(approveDTO)
                .retrieve()
                .toEntity(KakaoPaymentApproveResponseDTO.class);

        log.info("response : {}", entity);
    }
}
