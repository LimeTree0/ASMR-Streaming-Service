package limecoding.asmrstreamingservice.dto.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class OrderRequestDTO {
    private Long id;            // 상품 아이디
    private Integer quantity;   // 상품 수량
    private String userId;      // 사용자 아이디
}
