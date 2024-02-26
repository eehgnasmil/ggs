package ggs.ggs.goods;

import ggs.ggs.dto.CartDto;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.GoodsOptionDto;
import ggs.ggs.dto.GoodsQnADto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    @Autowired
    @Qualifier("goodsServiceImpl")
    private final GoodsService goodsService;


    @GetMapping("/goodslist")
    public String goodslist(Model model
            , @RequestParam(value = "category", defaultValue = "0") Integer category
            , @RequestParam(value = "sortValue", defaultValue = "0") Integer sortValue
            , @RequestParam(value = "page", defaultValue = "0") int page,
                            Authentication authentication) {

        Page<GoodsDto> goods ;

        if (authentication != null && authentication.isAuthenticated()) {
            goods = goodsService.findAll(sortValue, page, category, authentication.getName());
        } else {
            goods = goodsService.findAll(sortValue, page, category);
        }
        System.out.println(goods.toString());

        model.addAttribute("sortValue", sortValue);
        model.addAttribute("category", category);
        model.addAttribute("goods", goods);
        return "/goods/goodslist";
    }


    // detail
    @GetMapping("/detail/{idx}")
    public String detail(Model model, @PathVariable("idx") Integer idx, Authentication authentication) {
        boolean tf = false;
        // 사용자 id 가져오기(SecurityContextHolder)
        GoodsDto goods;
        if (authentication != null && authentication.isAuthenticated()) {
             goods = this.goodsService.getGoods(idx,authentication.getName());
        } else{
            goods = this.goodsService.getGoods(idx);
        }
        System.out.println(goods);

        List<CartDto> cartDtos = new ArrayList<>();
        model.addAttribute("goods", goods);
        model.addAttribute("carts", cartDtos);
        return "/goods/detail";
    }

    @ResponseBody
    @PostMapping("/selectSizes")
    public List<String> selectSizes(@RequestParam("goodsIdx") Integer goodsIdx, @RequestParam("color") String color) {
        List<String> sizes = goodsService.selectSizes(goodsIdx, color);

        return sizes;
    }

    // 작성
    @GetMapping("/goodsForm")
    public String goodsForm(Model model) {

        GoodsDto goodsDto = new GoodsDto();
        model.addAttribute("goods", new GoodsDto());

        return "/goods/goodsForm";
    }

    @PostMapping("/new/{idx}")
    public String save(@ModelAttribute GoodsDto goods, @PathVariable(name = "idx") String idx) throws IOException {
        System.out.println(goods.getFileDtos());
        if (idx == null || idx.equals("null")) {
            idx = this.goodsService.insert(goods).toString();
        } else {
            this.goodsService.update(goods);
        }
        return "redirect:/goods/detail/" + idx;
    }

    @GetMapping("/update/{idx}")
    public String update(Model model, @PathVariable("idx") Integer idx) {
        GoodsDto goods = this.goodsService.getGoods(idx);
        model.addAttribute("goods", goods);
        return "/goods/goodsForm";
    }

    @GetMapping("/delete/{idx}")
    public String delete(@PathVariable("idx") Integer idx) {

        this.goodsService.delete(idx);
        return "redirect:/goods/goodslist";
    }

}
