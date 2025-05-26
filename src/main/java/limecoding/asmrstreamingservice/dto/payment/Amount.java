package limecoding.asmrstreamingservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * https://developers.kakaopay.com/docs/payment/online/single-payment
 */

@ToString
@Setter
@Getter
public class Amount {
    private Integer total;          // 전체 결제 금액

    @JsonProperty(value = "tax_free")
    private Integer taxFree;        // 비과세 금액

    private Integer vat;            // 부가세 금액
    private Integer point;          // 사용한 포인트 금액
    private Integer discount;       // 할인 금액

    @JsonProperty(value = "green_deposit")
    private Integer greenDeposit;   // 컵 보증금
}