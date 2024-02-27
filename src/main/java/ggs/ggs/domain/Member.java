package ggs.ggs.domain;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ggs.ggs.dto.MemberDto;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class Member {

    // 엔티티 컬럼

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(unique = true)
    private String id;

    @Column()
    private String pw;

    @Column()
    private Integer grade;

    @Column()
    private int point ;


    @Column()
    private String name;

    @Column()
    private String phone;

    @Column(unique = true)
    private String nick;

    @Column(unique = true)
    private String email;

    @Column()
    private String intro;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime create_date;

    @LastModifiedDate
    private LocalDateTime modify_date;

    @Column()
    private String postcode;

    @Column()
    private String postaddress;

    @Column()
    private String detailaddress;

    @OneToOne(mappedBy = "member", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private MemberFile file;

    @OneToMany(mappedBy = "member",orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoodsLike> goodsLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member",orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoodsQnA> goodsQnAs = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Cart cart;

    // 추가 메서드
    public Member() {
    }

    public Member(MemberDto dto) {
        this.id = dto.getId();
        this.pw = dto.getPw();
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.nick = dto.getNick();
        this.email = dto.getEmail();
        this.postcode = dto.getPostcode();
        this.postaddress = dto.getPostaddress();
        this.detailaddress = dto.getDetailaddress();
        this.grade = dto.getGrade();
        this.point = 1000;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setFile(MemberFile memberFile) {
        this.file = memberFile;
    }
}
