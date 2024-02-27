package ggs.ggs.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder(toBuilder = true)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @OneToOne
    @JoinColumn(name = "member", referencedColumnName = "idx" )
    private Member member;

    private Integer count;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_date;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems;

    public static Cart CreateCart(Member member){
        Cart cart = Cart.builder()
                .count(0)
                .member(member)
                .build();
        return cart;
    }

    public static Cart updateCart(Cart cart, Integer count){
        Cart uCart = Cart.builder()
                .idx(cart.getIdx())
                .member(cart.getMember())
                .count(count)
                .build();
        return uCart;
    }

    public void removeItemAndUpdateCount(CartItem cartItem) {
        if (cartItems != null) {
            cartItems.remove(cartItem);
            updateCount();
        }
    }
    public void updateCount() {
        if (cartItems != null) {
            this.count = cartItems.size();
        } else {
            this.count = 0;
        }
    }

}
