package ggs.ggs.member;

import ggs.ggs.domain.Follow;
import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsFile;
import ggs.ggs.domain.GoodsOption;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.MemberFile;
import ggs.ggs.dto.FileDto;
import ggs.ggs.dto.MemberDto;
import ggs.ggs.goods.GoodsFileServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    @Autowired
    @Qualifier("memberFileServiceImpl")
    private final MemberFileServiceImpl fileService;


    //아이디, 닉네임, 이메일 중복 확인(1=중복 데이터 있음, 0=중복데이터 없음)
    public String check(Map<String, String> data) {
        String key = null;
        String val = null;
        String rs = null; // rs = 1 => 동일아이디 있음
        boolean CheckResult;

        for (Map.Entry<String, String> entry : data.entrySet()) {
            key = (String) entry.getKey();
            val = (String) entry.getValue();
        }

        if (key.equals("id")) {
            CheckResult = memberRepository.existsByid(val);
            if (CheckResult == true) {
                rs = "1";
            } else {
                rs = "0";
            }
        } else if (key.equals("nick")) {
            CheckResult = memberRepository.existsBynick(val);
            if (CheckResult == true) {
                rs = "1";
            } else {
                rs = "0";
            }
        } else if (key.equals("emailForm") || key.equals("customEmail") || key.equals("email")) {
            CheckResult = memberRepository.existsByemail(val);
            if (CheckResult == true) {
                rs = "1";
            } else {
                rs = "0";
            }
        } else if (key.equals("emailCheck")) {
            Optional<Member> member = memberRepository.findByemail(val);
            rs = member.get().getId();
        }
        return rs;
    }

    //체크된 데이터 저장 => memberRepository.save(member);
    public Member create(MemberDto dto) throws IOException {
        Member member = new Member(dto);
        FileDto fileDto = new FileDto();
        if (dto.getFileDto() != null) {
            fileDto = fileService.insert(dto.getFileDto().getFile());

        } else{
            fileDto.setPath("/img/nProfile.png");
        }
        fileDto.setMember(member);
        MemberFile memberFile = new MemberFile(fileDto);
        member.setFile(memberFile);


        memberRepository.save(member);
        return member;
    }

    // 멤버 리스트 받아오기
    public MemberDto memberList(String id) {
        Optional<Member> member = memberRepository.findByid(id);
        MemberDto dto = new MemberDto(member.get());
        return dto;
    }

    // 인트로 작성 및 수정
    public void updateIntro(String id, String intro) {
        memberRepository.modifyIntro(id, intro);
    }

    // 팔로우 여부확인
    public boolean followCheck(String sid, String id) {
        boolean check = followRepository.existsByFromUserIdAndToUserId(sid, id);
        return check;
    }

    // 팔로우
    public void updateFollow(String toUserId, String fromUserId) {
        Optional<Member> fromUser = memberRepository.findByid(fromUserId);
        Follow follow = new Follow(toUserId, fromUser.get());
        followRepository.save(follow);
    }

    // 언팔로우
    public void unFollow(String toUserId, String fromUserId) {
        Optional<Follow> followCheck = followRepository.findByFromUserIdAndToUserId(fromUserId, toUserId);
        followRepository.delete(followCheck.get());
    }

    // 상대 팔로워 목록
    public List<Follow> followerList(String toUserId) {
        System.out.println(toUserId);
        List<Follow> followerList = followRepository.findAllByToUserId(toUserId);
        return followerList;
    }

    // 상대 팔로우 목록
    public List<Follow> followList(String toUserId) {
        System.out.println(toUserId);
        List<Follow> followList = followRepository.findAllByFromUserId(toUserId);
        System.out.println("밍?" + followList);
        return followList;
    }

}
