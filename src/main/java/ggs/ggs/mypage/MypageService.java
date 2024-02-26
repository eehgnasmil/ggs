package ggs.ggs.mypage;

import ggs.ggs.dto.FileDto;
import ggs.ggs.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface MypageService {
    int SearchIdx(String id);
    
    void modifyPw(int idx, String pw);

    MemberDto MemberList(String id);

    String prevPwCheck(String id);

    void modifyNick(Integer idx, String nick);

    void resign(String id, HttpServletRequest request);

    void modifyAddress(int idx, String postcode, String postaddress, String detailaddress);


    void changeImg(FileDto fileDto) throws IOException;
}
