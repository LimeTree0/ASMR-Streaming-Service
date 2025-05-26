package limecoding.asmrstreamingservice.dto.order;

import limecoding.asmrstreamingservice.entity.order.Order;
import limecoding.asmrstreamingservice.entity.order.PaymentCompany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class OrderDTO {

    private Long id;
    private Integer amount;
    private Long asmrId;
    private String name;
    private PaymentCompany paymentCompany;

    public static OrderDTO from(Order order) {

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setAmount(order.getAmount());
        orderDTO.setAsmrId(order.getAsmr().getId());
        orderDTO.setName(order.getAsmr().getFileEntity().getFileName());
        orderDTO.setPaymentCompany(order.getPaymentCompany());

        return orderDTO;
    }
}
