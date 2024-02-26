package ggs.ggs.member;

import ggs.ggs.domain.Follow;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    @Qualifier("memberServiceImpl")
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    //회원가입==============================================================================================================//
    @GetMapping("/join")
    public String joinPage() {

        return "member/join";
    }

    //아이디, 닉네임, 이메일 중복체크 서비스 호출
    @ResponseBody
    @PostMapping("/checking")
    public String checking(@RequestParam("checks") String ch, @RequestParam("value") String val)throws Exception{
        System.out.println(val);
        Map<String, String> checks = new HashMap<String, String>();
        checks.put(ch, val);
        String checkResult = memberService.check(checks);
        return checkResult;
    }

    //체크된 데이터 저장 서비스 호출
    @PostMapping("/join")
    public String join(@ModelAttribute MemberDto memberDto)throws Exception{
    	String encPw = passwordEncoder.encode(memberDto.getPw());
        System.out.println(memberDto);
    	memberDto.setPw(encPw);
        memberService.create(memberDto);
        return "redirect:/";
    }

    //로그인==============================================================================================================//
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    //userpage==============================================================================================================//
    @GetMapping("/userPage/{id}")
    public String userPage(Model model, @PathVariable("id") String id, Authentication authentication) {
        //내 정보 확인
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        String fwid; //팔로워
        String fid; //팔로우
        model.addAttribute("sid", sid);
        //내가 팔로우 한 상태인지 체크
        boolean follow = memberService.followCheck(sid, id);
        model.addAttribute("followCheck", follow);

        //상대 유저 정보
        MemberDto dto = new MemberDto();
        dto = memberService.memberList(id);
        model.addAttribute("member", dto);

        // 상대 팔로워 정보
        List<Follow> follower = memberService.followerList(id);
        List<String> followers = new ArrayList<String>();
        for(Follow f : follower) {
            fwid =String.valueOf(f.getFromUser().getId());
            followers.add(fwid);
        }
        // 상대 팔로우 정보
        List<Follow> followList = memberService.followList(id);
        List<String> follows = new ArrayList<String>();
        for(Follow f : followList) {
            fid =String.valueOf(f.getToUserId());
            follows.add(fid);
        }

        model.addAttribute("followerList", followers);
        model.addAttribute("followList", follows);

        return "member/userPage";
    }

    //체크된 데이터 저장 서비스 호출
    @PostMapping("/intro")
    public String intro(@RequestParam Map<String, String> params)throws Exception{
        String intro = params.get("intro");
        String id =params.get("id");
        memberService.updateIntro(id, intro);
        return "redirect:/";
    }

    //팔로우
    @PostMapping("/follow")
    public String follow(@RequestParam Map<String, String> params)throws Exception{
        String toUserId = params.get("toUserId");
        String fromUserId =params.get("fromUserId");
        memberService.updateFollow(toUserId, fromUserId);
        return "redirect:/";
    }

    //언팔로우
    @PostMapping("/unFollow")
    public String unFollow(@RequestParam Map<String, String> params)throws Exception{
        String toUserId = params.get("toUserId");
        String fromUserId =params.get("fromUserId");
        memberService.unFollow(toUserId, fromUserId);
        return "redirect:/";
    }
//아이디 비밀번호 찾기==========================================================================================================//

    //아이디 찾기페이지
    @GetMapping("/findId")
    public String findId() {
        return "member/findId";
    }

    //PW 찾기페이지
    @GetMapping("/findPw")
    public String findPw() {
        return "member/findPw";
    }

    //아이디, 닉네임, 이메일 중복체크 서비스 호출
    @ResponseBody
    @PostMapping("/email/checking")
    public String checkMail(@RequestParam("checks") String ch, @RequestParam("value") String val)throws Exception{
        System.out.println(val);
        Map<String, String> checks = new HashMap<String, String>();
        checks.put(ch+"Check", val);
        String id = memberService.check(checks);
        return id;
    }

}
