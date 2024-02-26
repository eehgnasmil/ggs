package ggs.ggs.domain;

import groovy.transform.builder.Builder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="subscribe_uk",
                        columnNames = {"toUserId", "fromUserId"}
                )
        }
)
public class Follow {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column()
    private String toUserId;

    @JoinColumn(name = "fromUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member fromUser;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime create_date;

    public Follow(String toUserId, Member fromUser) {
        this.toUserId = toUserId;
        this.fromUser = fromUser;
    }

}