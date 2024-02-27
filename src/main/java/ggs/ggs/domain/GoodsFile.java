package ggs.ggs.domain;


import ggs.ggs.dto.FileDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@ToString
public class GoodsFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String ofile;
    @Column
    private String sfile;
    @Column
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods", referencedColumnName = "idx")
    private Goods goods;


    public GoodsFile(FileDto fileDto) {
        this.idx = fileDto.getIdx();
        this.ofile = fileDto.getOfile();
        this.sfile = fileDto.getSfile();
        this.path = fileDto.getPath();
        this.goods = fileDto.getGoods();
    }
}
