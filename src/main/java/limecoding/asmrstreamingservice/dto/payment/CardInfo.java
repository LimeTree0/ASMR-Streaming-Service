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
public class CardInfo {

    @JsonProperty(value = "kakaopay_purchase_corp")
    private String kakaoPayPurchaseCorp;

    @JsonProperty(value = "kakaopay_purchase_corp_code")
    private String kakaoPayPurchaseCorpCode;

    @JsonProperty(value = "kakaopay_issuer_corp")
    private String kakaoPayIssuerCorp;

    @JsonProperty(value = "kakaopay_issuer_corp_code")
    private String kakaoPayIssuerCorpCode;

    private String bin;

    @JsonProperty(value = "card_type")
    private String cardType;

    @JsonProperty(value = "install_month")
    private String installMonth;

    @JsonProperty(value = "approved_id")
    private String approvedId;

    @JsonProperty(value = "card_mid")
    private String cardMid;

    @JsonProperty(value = "interest_free_install")
    private String interestFreeInstall;

    @JsonProperty(value = "installment_type")
    private String installmentType;

    @JsonProperty(value = "card_item_code")
    private String cardItemCode;

}
