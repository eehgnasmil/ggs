package ggs.ggs.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ggs.ggs.board.BoardService;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.goods.GoodsService;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
    public String myModify(@PathVariable("title") String title, Model model, Authentication authentication,
    		@RequestParam(value = "category", defaultValue = "0") Integer category
            , @RequestParam(value = "sortValue", defaultValue = "0") Integer sortValue
            , @RequestParam(value = "page", defaultValue = "0") int page) {

        // 사용자 id 가져오기(SecurityContextHolder)
        authentication = SecurityContextHolder.getContext().getAuthentication();
        id = authentication.getName();
       
        switch (title) {

            case "dashBoard":
                //페이지 정보 외 페이지로 보낼 데이터
                model.addAttribute("title", title);

                break;

            case "goodsForm":
                model.addAttribute("goods", new GoodsDto());
                break;

            case "goodsList":
            	Page<GoodsDto> goods = goodsService.findAll(sortValue, page, category);
            	model.addAttribute("goods", goods);
            	model.addAttribute("sortValue", sortValue);
                model.addAttribute("category", category);
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

    @PostMapping("/deleteGoods")
    public ResponseEntity<String> deleteGoods(@RequestBody List<Integer> goodsItems) {
        System.out.println(goodsItems);
        for(Integer goodsItem:goodsItems){
//            goodsService.delete(goodsItem);
        }
        return ResponseEntity.ok(goodsItems.size() + "개가 삭제되었습니다.");
    }

}
