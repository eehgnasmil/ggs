package ggs.ggs.order;

import ggs.ggs.domain.Cart;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.Order;
import ggs.ggs.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    // scheduler
    void processOrderStatus();

    Cart FindbyMember(Member member);

    // 장바구니
    // 리스트
    Page<CartItemDto> findbyCartItem(String sid, int page);

    // 중복확인
    boolean checkCart(List<CartDto> options, String sid);

    // 담기
    List<Integer> addCart(List<CartDto> options, String sid, Boolean result);

    // 삭제
    void deleteCart(Integer cartItem, String sid);

    // 개수변경
    void changCnt(Integer ciIdx, Integer cnt);

    // 장바구니 개수
    Integer sumCartItem(String sid);

    // 카트 개수 업그레이드
    void countCart(Cart cart);


    // 주문
    // 리스트
    Page<OrderlistDto> findbyOrderItem(String sid, int page);

    // 주문 내역 폼
    OrderDto orderGoods(List<Integer> orderItems, String sid);

    // 주문하기
    Integer saveOrder(PaymentDto payment, String sid);

    // 주문 내역 디테일
    OrderlistDto getOrder(Integer idx, String sid);

    // 주문 취소
    void orderCancle(Integer orderIdx, String sid);

    // 주문 확인
    void orderCorfirm(Integer orderIdx, String sid);

    // 포인트 지급
    void givePoint(Order order);

    // 리뷰 작성
    void orderReview(Order order);


}