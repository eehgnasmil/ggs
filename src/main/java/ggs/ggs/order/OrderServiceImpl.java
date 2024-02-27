package ggs.ggs.order;

import ggs.ggs.domain.*;
import ggs.ggs.dto.*;
import ggs.ggs.goods.GoodsOptionRepository;
import ggs.ggs.goods.GoodsRepository;
import ggs.ggs.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final GoodsOptionRepository optionRepository;
    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;

    // schedualer
    @Scheduled(fixedRate = 60000)
    @Transactional
@Override
    public void processOrderStatus() {

        List<Order> orders = orderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        System.out.println("확인확인"+now);
        for(Order order: orders){
            switch (order.getState()){
                case 0: break; // 주문 취소
                case 1: // 1 결제완료(취소가능)
                    if(order.getDeliveryDeadline().isBefore(now)){
                        order.setState(2);
                        order.setConfirmationDeadline(now.plusMinutes(2));
                    }
                    break;
                case 2: // 2 배송완료(확정가능)
                    if(order.getConfirmationDeadline().isBefore(now)){
                        order.setState(3);
                        order.setReviewDeadline(now.plusMinutes(3));
                    }
                    break;
                case 3: // 3 구매확정(리뷰가능 // 포인트 지급)
                    if(order.getReviewDeadline().isBefore(now)){
                        order.setState(4);
                        givePoint(order);
                    }
                    break;
                case 4: // 4 구매확정
                    break;

            }
        }
        orderRepository.saveAll(orders);

    }

    // 카트 찾기
    @Override
    public Cart FindbyMember(Member member) {
        Optional<Cart> optionalCart = cartRepository.findByMember(member);
        Cart cart;
        if (optionalCart.isEmpty()) {
//            없으면 장바구니 만들기
            cart = Cart.CreateCart(member);
            member.setCart(cart);
            cartRepository.save(cart);
        } else {
            cart = optionalCart.get();
        }
        return cart;
    }

    // 장바구니
    // 장바구니리스트
    @Override
    public Page<CartItemDto> findbyCartItem(String sid, int page) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();

        Cart cart = FindbyMember(member);
        Sort sort = Sort.by(Sort.Order.desc("idx"));
        Pageable pageable = PageRequest.of(page, 8, sort);

        Page<CartItem> cartItems = cartItemRepository.findAllByCart(cart, pageable);

        return cartItems.map(cartItem -> {
            CartItemDto cartItemDto = new CartItemDto(cartItem);
            return cartItemDto;
        });
    }

    // 중복확인
    @Override
    public boolean checkCart(List<CartDto> options, String sid) {

        boolean ischeck = true;
        // 회원찾기
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();
        // 상품찾기
        Optional<Goods> optionalGoodsgoods = goodsRepository.findById(options.get(0).getGoodsIdx());
        Goods goods = optionalGoodsgoods.get();

        Cart cart = FindbyMember(member);

        for (CartDto cartDto : options) {
            GoodsOption goodsOption = optionRepository.findByGoodsAndColorAndSize(goods, cartDto.getColor(), cartDto.getSize());
            Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndAndGoodsOption(cart, goodsOption);
            if (!optionalCartItem.isEmpty()) {
                ischeck = false;
                break;
            }
        }

        return ischeck;
    }

    // 담기
    @Override
    public List<Integer> addCart(List<CartDto> options, String sid, Boolean result) {

        List<Integer> idxs = new ArrayList<>();

        // 회원찾기
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();
        // 상품찾기
        Optional<Goods> optionalGoodsgoods = goodsRepository.findById(options.get(0).getGoodsIdx());
        Goods goods = optionalGoodsgoods.get();

        Cart cart = FindbyMember(member);


        for (CartDto cartDto : options) {
            GoodsOption goodsOption = optionRepository.findByGoodsAndColorAndSize(goods, cartDto.getColor(), cartDto.getSize());
            Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndAndGoodsOption(cart, goodsOption);
            if (optionalCartItem.isEmpty()) {
                // 중복 X
                CartItem cartItem = new CartItem(cart, goodsOption, cartDto.getCnt());
                CartItem save = cartItemRepository.save(cartItem);
                idxs.add(save.getIdx());
            } else {
                CartItem cartItem = optionalCartItem.get();
                CartItem nCartItem = new CartItem();
                if (result) {
                    nCartItem = CartItem.update(cartItem, cartDto);
                } else {
                    nCartItem = CartItem.directUpdate(cartItem, cartDto);
                }
                CartItem save = cartItemRepository.save(nCartItem);
                idxs.add(save.getIdx());
            }
        }
        // 카트 개수 업데이트
        countCart(cart);

        return idxs;
    }

    //삭제
    @Transactional
    @Override
    public void deleteCart(Integer cartItem, String sid) {
        cartItemRepository.deleteById(cartItem);
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();
        Cart cart = FindbyMember(member);

        cartRepository.save(cart);
        countCart(cart);
    }

    // 개수 변경
    @Override
    public void changCnt(Integer ciIdx, Integer cnt) {
        cartItemRepository.updateByIdx(ciIdx, cnt);
    }

    // 장바구니 개수
    @Override
    public Integer sumCartItem(String sid) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();
        Integer cartCount = cartRepository.findCntbyMember(member);
        return cartCount;
    }

    // 카트 개수 업그레이드
    @Override
    public void countCart(Cart cart) {
        Integer cartCount = cartItemRepository.sumCartItem(cart);
        cart = Cart.updateCart(cart, cartCount);
        cartRepository.save(cart);
    }


    // 주문
    // myorderList
    @Override
    public Page<OrderlistDto> findbyOrderItem(String sid, int page) {
        Sort sort = Sort.by(Sort.Order.desc("idx"));
        Pageable pageable = PageRequest.of(page, 8, sort);

    Optional<Member> optionalMember = memberRepository.findByid(sid);
    Member member = optionalMember.get();

    Page<Order> orders = orderRepository.findAllByMember(member, pageable);
        return orders.map(order -> {
        OrderlistDto orderlistDto = new OrderlistDto(order);
        return orderlistDto;
    });
}

// 주문 전 내역
@Override
    public OrderDto orderGoods(List<Integer> orderItems, String sid) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();

        List<CartItemDto> nCartItemDtos = new ArrayList<>();
        for (Integer orderItem : orderItems) {
            System.out.println(orderItem);
            if (orderItem != null) {
                Optional<CartItem> optionalCartItem = cartItemRepository.findById(orderItem);
                CartItem cartItem = optionalCartItem.get();
                CartItemDto nCartItemDto = new CartItemDto(cartItem);
                nCartItemDtos.add(nCartItemDto);
            }
        }

        OrderDto orderDto = new OrderDto(member, nCartItemDtos);
        return orderDto;
    }

    // 주문
    @Override
    public Integer saveOrder(PaymentDto payment, String sid) {

        // 회원찾기
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();

        // 포인트 차감 -> 주문완료 시 포인트 지급
        Integer newPoint = member.getPoint() - payment.getPoint();

        memberRepository.updateMemberPoint(member, newPoint);
        optionalMember = memberRepository.findByid(sid);
        member = optionalMember.get();

        List<OrderItem> orderItems = new ArrayList<>();

        // 주문 생성
        Order order = new Order(member, payment, orderItems);

        for (Integer product : payment.getProducts()) {
            System.out.println(product);

            Optional<CartItem> optionalCartItem = cartItemRepository.findById(product);
            CartItem cartItem = optionalCartItem.get();

            // 주문 항목 생성 시 주문 정보 설정
            OrderItem orderItem = new OrderItem(order, cartItem);

            orderItems.add(orderItem);
            deleteCart(product, sid);
        }

        // 주문 저장
        Order saveOrder = orderRepository.save(order);
        Integer orderIdx = saveOrder.getIdx();

        return orderIdx;
    }

    // 주문 후 내역
    @Override
    public OrderlistDto getOrder(Integer idx, String sid) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();

        Optional<Order> optionalOrder = orderRepository.findById(idx);
        Order order = optionalOrder.get();

        OrderlistDto orderlistDto = new OrderlistDto(order);

        return orderlistDto;
    }

    // 주문취소
    @Override
    public void orderCancle(Integer orderIdx, String sid) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);
        Member member = optionalMember.get();
        Order order = orderRepository.findById(orderIdx).orElseThrow();

        // 포인트 차감 -> 주문완료 시 포인트 지급
        Integer newPoint = member.getPoint() + order.getUsePoint();

        memberRepository.updateMemberPoint(member, newPoint);

       orderRepository.updateByState(order, 0);

    }

    // 주문확인
    @Override
    public void orderCorfirm(Integer orderIdx, String sid) {
        LocalDateTime now = LocalDateTime.now();

        Order order = orderRepository.findById(orderIdx).orElseThrow();
        // 구매확정 시간
        order.setState(3);
        order.setReviewDeadline(now.plusMinutes(3));
        orderRepository.save(order);
        
        givePoint(order);

    }
    // 포인트 지급
    @Override
    public void givePoint(Order order) {
        int newPoint = order.getGivePoint() + order.getMember().getPoint();
        memberRepository.updateMemberPoint(order.getMember(), newPoint);

    }
    // 리뷰 작성 시
    @Override
    public void orderReview(Order order) {
        order.setState(4);
        orderRepository.save(order);
    }


}
