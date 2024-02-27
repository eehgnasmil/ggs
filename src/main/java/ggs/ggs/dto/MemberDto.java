package ggs.ggs.dto;

import java.util.List;
import java.util.stream.Collectors;

import ggs.ggs.domain.Member;
import ggs.ggs.domain.MemberFile;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class MemberDto {

    private Integer idx;
    private String id;
    private String pw;
    private String name;
    private String nick;
    private String phone;
    private String email;
    private String postcode;
    private String postaddress;
    private String detailaddress;
    private String intro;
    private Integer grade;
    private Integer point;
    private FileDto fileDto;

    public MemberDto(String id, String password1, String name, String nick, String email) {
        this.id = id;
        this.pw = password1;
        this.name = name;
        this.nick = nick;
        this.email = email;
    }

    public MemberDto(Member member) {
        this.idx = member.getIdx();
        this.id = member.getId();
        this.pw = member.getPw();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.nick = member.getNick();
        this.email = member.getEmail();
        this.postcode = member.getPostcode();
        this.postaddress = member.getPostaddress();
        this.detailaddress = member.getDetailaddress();
        this.grade = member.getGrade();
        this.point = member.getPoint();
        this.intro = member.getIntro();
        this.fileDto = new FileDto(member.getFile());
    }

}
