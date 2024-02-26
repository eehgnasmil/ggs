package ggs.ggs.order;

import ggs.ggs.domain.Cart;
import ggs.ggs.domain.CartItem;
import ggs.ggs.domain.GoodsOption;
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
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndAndGoodsOption(Cart cart, GoodsOption goodsOption);
    @Query("select count(c.cnt) from CartItem c where c.cart = :cart" )
    Integer sumCartItem(@Param("cart") Cart cart);

    Page<CartItem> findAllByCart(Cart cart, Pageable pageable);

    @Modifying
    @Query("UPDATE CartItem cI SET cI.cnt = :cnt WHERE cI.idx = :ciIdx")
    @Transactional
    void updateByIdx(Integer ciIdx, Integer cnt);

}
