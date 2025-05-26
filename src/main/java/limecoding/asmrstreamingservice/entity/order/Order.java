package limecoding.asmrstreamingservice.entity.order;

import jakarta.persistence.*;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentCompany paymentCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asmr_id", nullable = false)
    private ASMR asmr;
}
