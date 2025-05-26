package limecoding.asmrstreamingservice.repository.payment.kakao;

import limecoding.asmrstreamingservice.entity.order.Order;
import limecoding.asmrstreamingservice.entity.payment.kakao.ready.KakaoPaymentReady;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KakaoPaymentReadyRepository extends JpaRepository<KakaoPaymentReady, Long> {
    List<KakaoPaymentReady> findByOrder(Order order);
}
