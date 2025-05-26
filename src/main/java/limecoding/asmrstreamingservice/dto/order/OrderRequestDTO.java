package limecoding.asmrstreamingservice.dto.order;

import limecoding.asmrstreamingservice.entity.order.PaymentCompany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class OrderRequestDTO {
    private Long asmrId;
    private Integer amount;
    private PaymentCompany paymentCompany;
}
