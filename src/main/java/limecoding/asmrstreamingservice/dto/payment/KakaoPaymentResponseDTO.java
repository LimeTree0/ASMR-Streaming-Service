package limecoding.asmrstreamingservice.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * detail for response, check the link below.
 * https://developers.kakaopay.com/docs/payment/online/single-payment
 */

@ToString
@Setter
@Getter
public class KakaoPaymentResponseDTO {
    private String tid;                     // 결제 고유 번호, 20자

    @JsonProperty("next_redirect_app_url")
    private String nextRedirectAppUrl;      // 모바일 앱 카카오톡 결제 페이지 redirect URL

    @JsonProperty("next_redirect_mobile_url")
    private String nextRedirectMobileUrl;   // 모바일 웹 카카오톡 결제 페이지 Redirect URL

    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;       // PC 카카오톡 결제 페이지 Redirect URL

    @JsonProperty("android_app_scheme")
    private String androidAppScheme;        // 카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme) - 내부 서비스용

    @JsonProperty("ios_app_scheme")
    private String iosAppScheme;            // 카카오페이 결제 화면으로 이동하는 iOS 앱 스킴 - 내부 서비스용

    @JsonProperty("created_at")
    private LocalDateTime createAt;         // 	결제 준비 요청 시간
}
