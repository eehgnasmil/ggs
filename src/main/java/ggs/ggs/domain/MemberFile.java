package ggs.ggs.domain;


import ggs.ggs.dto.FileDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@ToString
public class MemberFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String ofile;
    @Column
    private String sfile;
    @Column
    private String path;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", referencedColumnName = "idx")
    private Member member;

    public MemberFile(FileDto fileDto) {
        this.idx = fileDto.getIdx();
        this.ofile = fileDto.getOfile();
        this.sfile = fileDto.getSfile();
        this.path = fileDto.getPath();
        this.member = fileDto.getMember();
    }


    public MemberFile(MemberFile file) {
        this.idx = file.idx;
        this.path = "/img/nProfile.png";
        this.member = file.member;
    }
}
