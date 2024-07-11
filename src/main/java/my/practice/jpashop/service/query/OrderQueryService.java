package my.practice.jpashop.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {
    // OSIV 비활성화시 트랜잭션 내에서 처리해야하는 엔티티 관련 연산
}
