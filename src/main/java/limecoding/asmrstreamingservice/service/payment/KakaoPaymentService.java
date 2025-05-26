package limecoding.asmrstreamingservice.service.payment;

import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.order.OrderDTO;
import limecoding.asmrstreamingservice.dto.payment.KakaoPaymentApproveDTO;
import limecoding.asmrstreamingservice.dto.payment.KakaoPaymentApproveResponseDTO;
import limecoding.asmrstreamingservice.dto.payment.KakaoPaymentDTO;
import limecoding.asmrstreamingservice.dto.payment.KakaoPaymentResponseDTO;
import limecoding.asmrstreamingservice.entity.order.Order;
import limecoding.asmrstreamingservice.entity.payment.kakao.ready.KakaoPaymentReady;
import limecoding.asmrstreamingservice.repository.payment.kakao.KakaoPaymentReadyRepository;
import limecoding.asmrstreamingservice.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
@Transactional
public class KakaoPaymentService implements PaymentService {

    private final KakaoPaymentReadyRepository kakaoPaymentReadyRepository;
    private final OrderService orderService;
    private final RestClient restClient = RestClient.builder().baseUrl("https://open-api.kakaopay.com").build();


    public KakaoPaymentResponseDTO requestPay(OrderDTO orderDTO) {

        KakaoPaymentDTO kakaoPaymentDTO = convertOrderDTOtoKakaoPaymentDTO(orderDTO);

        ResponseEntity<KakaoPaymentResponseDTO> response = restClient.post()
                .uri("/online/v1/payment/ready")
                .header("Authorization", "SECRET_KEY DEVF362EC0133E8052DC8FBC0525F7FFCDA52DDC")
                .contentType(MediaType.APPLICATION_JSON)
                .body(kakaoPaymentDTO)
                .retrieve()
                .toEntity(KakaoPaymentResponseDTO.class);

        KakaoPaymentResponseDTO body = response.getBody();

        assert body != null;

        Order order = orderService.findOrderEntityById(orderDTO.getId());

        KakaoPaymentReady kakaoPaymentReady = KakaoPaymentReady.builder()
                .tid(body.getTid())
                .createAt(body.getCreateAt())
                .order(order)
                .build();

        kakaoPaymentReadyRepository.save(kakaoPaymentReady);

        return body;
    }

    public KakaoPaymentApproveResponseDTO approvePay(Long orderId, String pgToken) {
        String tid = findTidByOrderId(orderId);

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

        KakaoPaymentApproveResponseDTO body = entity.getBody();

        return body;

    }

    public String findTidByOrderId(Long orderId) {
        Order order = orderService.findOrderEntityById(orderId);

        return kakaoPaymentReadyRepository.findByOrder(order).getFirst().getTid();
    }

    public KakaoPaymentDTO convertOrderDTOtoKakaoPaymentDTO(OrderDTO orderDTO) {
        return KakaoPaymentDTO.builder()
                .cid("TC0ONETIME")
                .partnerOrderId("partner_order_id")
                .partnerUserId("partner_user_id")
                .itemName(orderDTO.getName())
                .quantity(orderDTO.getAmount())
                .totalAmount(2200)
                .taxFreeAmount(0)
                .approvalUrl("https://localhost:8080/api/v1/order/kakao/approve?order_id=" + orderDTO.getId())
                .failUrl("https://localhost:8080/api/v1/payment/fail")
                .cancelUrl("https://localhost:8080/api/v1/payment/cancel")
                .build();
    }
}
