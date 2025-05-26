package limecoding.asmrstreamingservice.entity.payment.kakao.ready;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import limecoding.asmrstreamingservice.dto.order.OrderDTO;
import limecoding.asmrstreamingservice.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class KakaoPaymentReady {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String tid;                     // 결제 고유 번호, 20자

    @JsonProperty("created_at")
    private LocalDateTime createAt;         // 	결제 준비 요청 시간

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
