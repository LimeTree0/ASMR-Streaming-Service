package limecoding.asmrstreamingservice.service.order;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import limecoding.asmrstreamingservice.dto.order.OrderDTO;
import limecoding.asmrstreamingservice.dto.order.OrderRequestDTO;
import limecoding.asmrstreamingservice.entity.ASMR;
import limecoding.asmrstreamingservice.entity.order.Order;
import limecoding.asmrstreamingservice.repository.order.OrderRepository;
import limecoding.asmrstreamingservice.service.ASMRService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ASMRService asmrService;

    public OrderDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Long asmrId = orderRequestDTO.getAsmrId();
        Integer amount = orderRequestDTO.getAmount();

        ASMR asmrEntityById = asmrService.findASMREntityById(asmrId);

        Order order = Order.builder()
                .amount(amount)
                .asmr(asmrEntityById)
                .paymentCompany(orderRequestDTO.getPaymentCompany())
                .build();

        Order save = orderRepository.save(order);

        return OrderDTO.from(save);
    }

    public Order findOrderEntityById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("해당 주문을 찾을 수 없습니다. id = " + id)
        );
    }
}
