package ggs.ggs.mypage;

import ggs.ggs.domain.Member;
import ggs.ggs.domain.MemberFile;
import ggs.ggs.dto.FileDto;
import ggs.ggs.dto.MemberDto;
import ggs.ggs.board.BoardRepository;
import ggs.ggs.goods.GoodsRepository;
import ggs.ggs.member.MemberFileRepository;
import ggs.ggs.member.MemberFileServiceImpl;
import ggs.ggs.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {

    private final MemberRepository memberRepository;
    private final MemberFileRepository fileRepository;

    @Qualifier("memberFileServiceImpl")
    private final MemberFileServiceImpl fileService;


    //리스트 가져오기
    public MemberDto MemberList(String id) {
        Optional<Member> list = memberRepository.findByid(id);

        MemberDto dto = new MemberDto(list.get());
        System.out.println(dto);
        return dto;
    }

    
    //리스트 가져오기(+img)
    public MemberDto MemberListImg(String id) {
    	Optional<Member> list = memberRepository.findByid(id);
    	MemberDto dto = new MemberDto(list.get());
    	return dto;
    }

    //탈퇴
    public void resign(String id, HttpServletRequest request) {
        memberRepository.deleteById(id);
        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
    }

    //멤버 수정을 위한 비밀번호 검증
    public String prevPwCheck(String id) {
        Optional<Member> list = memberRepository.findByid(id);
        String chkPw = list.get().getPw();
        return chkPw;
    }

    //닉네임 수정
    public void modifyNick(Integer idx, String nick) {
        memberRepository.updateNickById(idx, nick);

    }

    //비밀번호 수정
    public void modifyPw(int idx, String pw) {
        memberRepository.updatePwById(idx, pw);
    }

    //주소 수정
    public void modifyAddress(int idx, String postcode, String postaddress, String detailaddress) {
        memberRepository.modifyAddress(idx, postcode, postaddress, detailaddress);

    }
    @Override
    public void changeImg(FileDto fileDto) throws IOException {
        System.out.println(fileDto);
        MemberFile file = fileRepository.findById(fileDto.getIdx()).get();
        MemberFile nfile = new MemberFile();
        fileService.delete(file.getSfile());
        if(fileDto.getFile() == null){
            nfile = new MemberFile(file);
        }else {
            System.out.println(fileDto.getFile());
            fileDto = fileService.insert(fileDto.getFile());
            fileDto.setMember(file.getMember());
            fileDto.setIdx(file.getIdx());
            System.out.println(fileDto);
            nfile = new MemberFile(fileDto);
        }
        fileRepository.save(nfile);
    }

    //id로 idx조회
    public int SearchIdx(String id) {
        Optional<Member> member = memberRepository.findByid(id);
        int idx = member.get().getIdx();
        return idx;
    }


}
