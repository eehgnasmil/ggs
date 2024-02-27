package ggs.ggs.domain;

import ggs.ggs.dto.BoardDto;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String detail;

    @Column(columnDefinition = "int default 0", nullable = false)
    private int viewcount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(nullable = false)
    private String category; // 필드명 수정

    @Column
    private String outerwear;
    @Column
    private String top;
    @Column
    private String bottom;
    @Column
    private String shoes;
    @Column
    private String acc;

    private int likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberidx")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardlike = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MiddleTag> middleTags = new ArrayList<>();

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void update(BoardDto dto) {
        this.title = dto.getTitle();
        this.detail = dto.getDetail();
        this.category = dto.getCategory();
        this.outerwear = dto.getOuterwear();
        this.bottom = dto.getBottom();
        this.top = dto.getTop();
        this.acc = dto.getAcc();
        this.shoes = dto.getShoes();
    }

}
