package limecoding.asmrstreamingservice.controller;

import limecoding.asmrstreamingservice.common.response.ApiResponse;
import limecoding.asmrstreamingservice.controller.v1.APIV1Controller;
import limecoding.asmrstreamingservice.dto.order.OrderDTO;
import limecoding.asmrstreamingservice.dto.order.OrderRequestDTO;
import limecoding.asmrstreamingservice.dto.payment.KakaoPaymentApproveResponseDTO;
import limecoding.asmrstreamingservice.dto.payment.KakaoPaymentResponseDTO;
import limecoding.asmrstreamingservice.service.order.OrderService;
import limecoding.asmrstreamingservice.service.payment.KakaoPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController implements APIV1Controller {

    private final OrderService orderService;
    private final KakaoPaymentService kakaoPaymentService;

    @PostMapping("/kakao/ready")
    public ResponseEntity<ApiResponse<Object>> addOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO orderDTO = orderService.addOrder(orderRequestDTO);
        KakaoPaymentResponseDTO kakaoPaymentResponseDTO = kakaoPaymentService.requestPay(orderDTO);

        return ResponseEntity.ok(ApiResponse.success(kakaoPaymentResponseDTO));
    }

    @GetMapping("/kakao/approve")
    public ResponseEntity<ApiResponse<Object>> approveOrder(
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name = "pg_token") String pgToken
    ) {

        KakaoPaymentApproveResponseDTO kakaoPaymentApproveResponseDTO = kakaoPaymentService.approvePay(orderId, pgToken);

        return ResponseEntity.ok(ApiResponse.success(kakaoPaymentApproveResponseDTO));
    }
}
