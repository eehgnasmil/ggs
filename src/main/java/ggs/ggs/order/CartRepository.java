package ggs.ggs.order;

import ggs.ggs.domain.Cart;
import ggs.ggs.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByMember(Member member);
    @Query("select c.count from Cart c where c.member = :member")
    Integer findCntbyMember(@Param("member") Member member);
}
