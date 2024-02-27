package ggs.ggs.domain;

import ggs.ggs.dto.GoodsQnADto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder(toBuilder = true)
public class GoodsQnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods", referencedColumnName = "idx")
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member", referencedColumnName = "idx")
    private Member member;

    @Column
    private Integer category; // 1 상품문의 2 주문/배송문의
    @Column
    private String title;
    @Column
    private String question;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime questionDate;

    @Column
    private Integer state; // 0 답x 1 답o

    @Column
    private String answer;
    @Column
    private LocalDateTime answerDate;

    public GoodsQnA(GoodsQnADto goodsQnADto, Goods goods, Member member) {
        this.goods = goods;
        this.member = member;
        this.category = goodsQnADto.getCategory();
        this.title = goodsQnADto.getTitle();
        this.question = goodsQnADto.getQuestion();
        this.state = 0;
    }
}
