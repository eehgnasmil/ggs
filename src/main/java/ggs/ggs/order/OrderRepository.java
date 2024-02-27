package ggs.ggs.order;

import ggs.ggs.domain.Member;
import ggs.ggs.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByMember(Member member, Pageable pageable);

    // 주문취소
    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.state = :state, o.price = 0, o.usePoint = 0, o.givePoint = 0, o.delivery_price = 0  WHERE o = :order")
    void updateByState(@Param("order")Order order, @Param("state")int state);


}
