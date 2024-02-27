package ggs.ggs.domain;

import ggs.ggs.dto.CartDto;
import ggs.ggs.dto.GoodsOptionDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder(toBuilder = true)
public class GoodsOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    @Column
    private String color;
    @Column
    private String size;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "goods", referencedColumnName = "idx")
    private Goods goods;

    // 굿즈 옵션이 삭제되면 장바구니 삭제
    @OneToMany(mappedBy = "goodsOption", cascade = {CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    public GoodsOption(GoodsOptionDto goodsOptionDto) {
        this.idx = goodsOptionDto.getIdx();
        this.color = goodsOptionDto.getColor();
        this.size = goodsOptionDto.getSize();
        this.goods = goodsOptionDto.getGoods();
    }

}
