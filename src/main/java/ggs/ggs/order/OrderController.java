package ggs.ggs.order;

import ggs.ggs.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    @Qualifier("orderServiceImpl")
    private final OrderService orderService;

// 이거 어디에 쓴 지 모르겠어요 혹시 발견하신다면 알려주세요!!!
//    @GetMapping("/items")
//    public List<CartItemDto> cartItemlist(String sid) {
//        return orderService.findbyCartItem(sid);
//    }

    @ResponseBody
    @PostMapping("/cartlist")
    public List<Integer> cartlist(@RequestBody List<CartDto> options
            , @RequestHeader(name = "result", required = false, defaultValue = "true") boolean result
            , Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        System.out.println(result);
        List<Integer> idxs = orderService.addCart(options, sid, result);
        return idxs;
    }

    @ResponseBody
    @PostMapping("/checkCart")
    public boolean checkCart(@RequestBody List<CartDto> options, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        boolean ischeck = orderService.checkCart(options, sid);
        return ischeck;
    }

    @ResponseBody
    @PostMapping("/deleteCart")
    public String deleteCart(@RequestParam("cartItem") Integer cartItem, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        orderService.deleteCart(cartItem, sid);
        return "삭제되었습니다.";
    }

    @PostMapping("/deleteCarts")
    public ResponseEntity<String> deleteCarts(@RequestBody List<Integer> cartItems, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        System.out.println(cartItems);
        for(Integer cartItem:cartItems){
            orderService.deleteCart(cartItem, sid);
        }
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @PostMapping("/orderForm")
    public String orderForm(@RequestParam(name = "orderItem") List<Integer> orderItem, Model model, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        OrderDto orderDto = orderService.orderGoods(orderItem, sid);
        model.addAttribute("order", orderDto);
        System.out.println(orderDto);

        return "/order/orderForm";
    }
    @ResponseBody
    @PostMapping("/payment")
    public Integer processPayment(@RequestBody PaymentDto payment, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        Integer orderIdx = orderService.saveOrder(payment,sid);
        System.out.println("안뇽");
    return orderIdx;

    }

    @GetMapping("/orderdetail/{idx}")
    public String detail(Model model, @PathVariable("idx") Integer idx, Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        OrderlistDto order = orderService.getOrder(idx,sid);
        model.addAttribute("order", order);
        return "/order/orderdetail";
    }

    @ResponseBody
    @PostMapping("/orderCancle")
    public void orderCancle(@RequestParam("orderIdx") Integer orderIdx, Authentication authentication){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        orderService.orderCancle(orderIdx, sid);
    }

    @ResponseBody
    @PostMapping("/orderCheck")
    public void orderCheck(@RequestParam("orderIdx") Integer orderIdx, Authentication authentication){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String sid = authentication.getName();
        // void여도 상관 없음
        orderService.orderCorfirm(orderIdx, sid);
    }

    @ResponseBody
    @PostMapping("/changeCnt")
    public void changeCnt(@RequestParam("ciIdx") Integer ciIdx,@RequestParam("cnt") Integer cnt){
        orderService.changCnt(ciIdx, cnt);
    }


}