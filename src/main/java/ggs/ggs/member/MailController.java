package ggs.ggs.member;

import ggs.ggs.mypage.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {
    @Autowired
    @Qualifier("mailServiceImpl")
    private final MailService mailService;
    @Autowired
    @Qualifier("mypageServiceImpl")
    private final MypageService mypageService;
    @Autowired
    @Qualifier("memberServiceImpl")
    private final MemberService memberService;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(@RequestParam("mail") String mail){

        int number = mailService.sendMail(mail);

        String num = "" + number;

        return num;
    }

    @ResponseBody
    @PostMapping("/mail/id")
    public String MailIdSend(@RequestParam("mail") String mail, @RequestParam("id") String id){
        mailService.sendIdMail(mail, id);

        return "1";
    }

    @ResponseBody
    @PostMapping("/mail/pw")
    public String MailPwSend(@RequestParam("mail") String mail, @RequestParam("id") String id){
        int idx = mypageService.SearchIdx(id);
        String pw = passwordEncoder.encode(mailService.sendPwMail(mail));
        System.out.println(idx + pw);
        mypageService.modifyPw(idx, pw);
        return "1";
    }

}