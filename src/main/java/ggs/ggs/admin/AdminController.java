package ggs.ggs.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ggs.ggs.board.BoardService;
import ggs.ggs.dto.MemberDto;
import ggs.ggs.goods.GoodsService;
import ggs.ggs.member.MemberService;
import ggs.ggs.mypage.MypageService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    @Qualifier("adminServiceImpl")
    private final AdminService adminService;

    @Autowired
    @Qualifier("boardServiceImpl")
    private final BoardService boardService;

    @Autowired
    @Qualifier("goodsServiceImpl")
    private final GoodsService goodsService;
    private String id = null;

    //관리자
    @GetMapping("/{title}")
    public String myModify(@PathVariable("title") String title, Model model, Authentication authentication) {

        // 사용자 id 가져오기(SecurityContextHolder)
        authentication = SecurityContextHolder.getContext().getAuthentication();
        id = authentication.getName();

        switch (title) {

            case "dashBoard":
                //페이지 정보 외 페이지로 보낼 데이터
                model.addAttribute("title", title);

                break;

            case "goodsDetail":

                break;

            case "point":

                break;

            case "myBoard":

                break;

            default:
                break;
        }

        //수정


        //삭제

        //리플레이스 할 페이지 정보
        model.addAttribute("title", title);

        return "/admin/admin";
    }


}
