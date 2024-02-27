package ggs.ggs.dto;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsFile;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.MemberFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private MultipartFile file            ;
    private Integer idx;
    private String ofile;
    private String sfile;
    private String path;
    private String status;

    private Goods goods;
    private Member member;


    public FileDto(String ofile, String sfile, String path) {
        this.ofile = ofile;
        this.sfile = sfile;
        this.path = path;
    }

    public FileDto(GoodsFile goodsFile) {
        this.idx = goodsFile.getIdx();
        this.ofile = goodsFile.getOfile();
        this.sfile = goodsFile.getSfile();
        this.path = goodsFile.getPath();
    }
    public FileDto(MemberFile memberFile) {
        this.idx = memberFile.getIdx();
        this.ofile = memberFile.getOfile();
        this.sfile = memberFile.getSfile();
        this.path = memberFile.getPath();
    }

}