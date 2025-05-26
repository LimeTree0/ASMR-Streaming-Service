package limecoding.asmrstreamingservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * this is Kakao payment ready DTO. use it for requesting payment ready
 * if you want to check detail information for each parament, please check
 * the link below.
 * kakao development document: https://developers.kakaopay.com/docs/payment/online/single-payment
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class KakaoPaymentDTO {
    private String cid;                 // 가맹점 코드, 10자

    @JsonProperty("partner_order_id")
    private String partnerOrderId;      //가맹점 주문 번호, 최대 100 글자

    @JsonProperty("partner_user_id")
    private String partnerUserId;       // 가맹점 회원 id, 최대 100 글자

    @JsonProperty("item_name")
    private String itemName;            // 상품명, 최대 100글자

    private Integer quantity;           // 상품 수량

    @JsonProperty("total_amount")
    private Integer totalAmount;        // 상품 총액

    @JsonProperty("tax_free_amount")
    private Integer taxFreeAmount;      // 상품 비과세 금액

    @JsonProperty("approval_url")
    private String approvalUrl;         // 결제 성공 시 redirect url, 최대 255자

    @JsonProperty("cancel_url")
    private String cancelUrl;           // 결제 취소 시 redirect url, 최대 255자

    @JsonProperty("fail_url")
    private String failUrl;             // 결제 실패 시 redirect url, 최대 255자
}
