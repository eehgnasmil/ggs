package ggs.ggs.domain;

import ggs.ggs.dto.GoodsDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    @Column
    private Integer category;
    @Column
    private String name;
    @Column
    private String summary;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String detail;
    @Column
    private Integer sellingPrice;
    @Column
    private Integer discountPrice;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_date;
    @LastModifiedDate
    private LocalDateTime modified_date;

    @OneToMany(mappedBy = "goods", cascade = {CascadeType.MERGE}, orphanRemoval = true)
    private List<GoodsLike> goodsLikes = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<GoodsOption> goodsOptions = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<GoodsFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = false)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoodsQnA> goodsQnAs = new ArrayList<>();

    public Goods(GoodsDto goodsDto) {
        this.idx = goodsDto.getIdx();
        this.category = goodsDto.getCategory();
        this.name = goodsDto.getName();
        this.summary = goodsDto.getSummary();
        this.detail = goodsDto.getDetail();
        this.sellingPrice = goodsDto.getSellingPrice();
        this.discountPrice = goodsDto.getDiscountPrice();
        this.files = new ArrayList<>();
        this.goodsOptions = new ArrayList<>();
    }
}
