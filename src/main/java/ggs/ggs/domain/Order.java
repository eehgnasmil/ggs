package ggs.ggs.domain;

import ggs.ggs.dto.PaymentDto;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String orderNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member", referencedColumnName = "idx")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;


    private Integer price;
    private Integer delivery_price;
    private Integer usePoint;
    private Integer givePoint;

    @Setter
    private Integer state; // 0 취소 1 결제완료(취소) 2 배송완료(확정) 3 구매확정(리뷰) 4 구매확정

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_date;

    @Setter
    @Column
    private LocalDateTime deliveryDeadline; // state가 1일 때 created_date에서 3분이 지나면 2로 바뀜
    @Setter
    @Column
    private LocalDateTime confirmationDeadline; //  state가 2일 때 deliveryDeadline에서 3분이 지나면 3로 바뀌고 사용자에게 givePoint 지급
    @Setter
    @Column
    private LocalDateTime reviewDeadline;   //  state가 3일 때 confirmationDeadline에서 3분이 지나면 4로 바뀜


    public Order(Member member, PaymentDto payment, List<OrderItem> orderItems) {
        this.orderNum = payment.getOrderNum();
        this.member = member;
        this.orderItems = orderItems;
        this.price = payment.getPrice();
        this.delivery_price = payment.getDelivery();
        this.usePoint = payment.getPoint();
        this.givePoint = (int) (payment.getPrice() * 0.01);
        this.state = 1;
        this.deliveryDeadline = LocalDateTime.now().plusMinutes(2);

    }


}
