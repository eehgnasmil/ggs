package ggs.ggs.mypage;

import ggs.ggs.board.BoardService;
import ggs.ggs.domain.GoodsLike;
import ggs.ggs.domain.MemberFile;
import ggs.ggs.dto.*;
import ggs.ggs.goods.GoodsCSService;
import ggs.ggs.goods.GoodsLikeServiceImpl;
import ggs.ggs.member.MemberService;
import ggs.ggs.order.OrderService;
import ggs.ggs.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    @Autowired
    @Qualifier("mypageServiceImpl")
    private final MypageService mypageService;
    @Autowired
    @Qualifier("memberServiceImpl")
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    @Qualifier("orderServiceImpl")
    private final OrderService orderService;
    @Autowired
    @Qualifier("goodsCSServiceImpl")
    private final GoodsCSService goodsCSService;

    private final @Qualifier("goodsLikeServiceImpl") GoodsLikeServiceImpl goodsLikeService;

    @Autowired
    @Qualifier("boardLikeServiceImpl")
    private LikeService boardLikeService;
    private final BoardService boardService;

    private String id = null;

    // 마이페이지
    @GetMapping("/{title}")
    public String myModify(@PathVariable("title") String title,
            @RequestParam(value = "page", defaultValue = "0") int page, Model model, Authentication authentication, @ModelAttribute MemberDto memberDto) {

        // 사용자 id 가져오기(SecurityContextHolder)
        authentication = SecurityContextHolder.getContext().getAuthentication();
        id = authentication.getName();
        MemberDto dto = mypageService.MemberList(id);

        switch (title) {

            case "myList":

                break;

            case "myResign":

                break;

            case "point":

                break;

            case "myBoardLike":
                Page<Object> boardlike = boardLikeService.likeList(id, page);
                model.addAttribute("boardlike", boardlike);
                break;

            case "myBoard":
                Page<BoardDto> myBoards = boardService.getMyboards(id, page);
                model.addAttribute("myBoards", myBoards);
                break;

            case "myLikeGoods":
                Page<Object> goodslikes = goodsLikeService.likeList(id, page);
                model.addAttribute("goods", goodslikes);
                break;

            case "myBasket":
                Page<CartItemDto> cartItemDtos = orderService.findbyCartItem(id, page);
                Integer cartNum = orderService.sumCartItem(id);
                model.addAttribute("cartItem", cartItemDtos);
                model.addAttribute("cartNum", cartNum);
                break;

            case "myOrderList":
                Page<OrderlistDto> orderlistDtos = orderService.findbyOrderItem(id, page);
                model.addAttribute("orderItem", orderlistDtos);
                break;

            case "myGoodsQnAList":
                Page<GoodsQnADto> goodsQnADtos = goodsCSService.findbyGoodsQnA(id, page);
                model.addAttribute("goodsQnA", goodsQnADtos);
                break;

            case "myGoodsReviewList":

                break;

            default:
                break;
        }

        // 수정

        // 삭제

        model.addAttribute("left", "myLeft");
        model.addAttribute("title", title);
        model.addAttribute("member", dto);

        return "/mypage/myPage";
    }

    // pwcheck
    @ResponseBody
    @PostMapping("/prevPwCheck")
    public boolean prevPwCheck(@RequestParam Map<String, String> params, Model model) throws Exception {
        Map<String, String> result = new HashMap<>();
        String id = params.get("id");
        String prevPw = params.get("prevPw");
        String chk = mypageService.prevPwCheck(id);
        boolean rs = passwordEncoder.matches(prevPw, chk);
        return rs;
    }

    // modifyNick 닉네임 수정
    @ResponseBody
    @PostMapping("/modifyNick")
    public void modifyNick(@RequestParam Map<String, String> params, Model model) throws Exception {
        int idx = Integer.parseInt(params.get("idx"));
        String nick = params.get("nick");
        mypageService.modifyNick(idx, nick);
    }

    // modifyPw 비밀번호 수정
    @ResponseBody
    @PostMapping("/modifyPw")
    public void modifyPw(@RequestParam Map<String, String> params, Model model) throws Exception {
        System.out.println(params);
        int idx = Integer.parseInt(params.get("idx"));
        String pw = passwordEncoder.encode(params.get("pw"));
        mypageService.modifyPw(idx, pw);
    }

    // resign 탈퇴
    @ResponseBody
    @PostMapping("/resign")
    public void resign(@RequestParam Map<String, String> params, Model model, HttpServletRequest request)
            throws Exception {
        mypageService.resign(params.get("id"), request);

    }

    // 아이디, 닉네임, 이메일 중복체크 서비스 호출
    @ResponseBody
    @PostMapping("/saveAddress")
    public String saveAdress(@RequestParam Map<String, String> address) throws Exception {
        int idx = Integer.parseInt(address.get("idx"));
        String postcode = address.get("postcode");
        String postaddress = address.get("postaddress");
        String detailaddress = address.get("detailaddress");

        mypageService.modifyAddress(idx, postcode, postaddress, detailaddress);

        return "1";
    }

    @ResponseBody
    @PostMapping("/deleteImg")
    public String deleteImg(@RequestParam("fileIdx") Integer fileIdx, Authentication authentication) throws IOException {
        FileDto fileDto = new FileDto();
        fileDto.setIdx(fileIdx);
        mypageService.changeImg(fileDto);
        return "변경되었습니다.";
    }

    @PostMapping("/changeImg")
    public ResponseEntity<String> changeImg(@RequestPart("file") MultipartFile file,
                                            @RequestParam("idx") int idx) throws Exception{
        FileDto fileDto = new FileDto();
        fileDto.setIdx(idx);
        fileDto.setFile(file);

        mypageService.changeImg(fileDto);
        return ResponseEntity.ok("변경되었습니다.");
    }

}
