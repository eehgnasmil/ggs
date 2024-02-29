package ggs.ggs.goods;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsQnA;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.OrderItem;
import ggs.ggs.dto.CartItemDto;
import ggs.ggs.dto.GoodsQnADto;
import ggs.ggs.dto.ReviewDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Qualifier("goodsCSServiceImpl")
@RequiredArgsConstructor
public class GoodsCSServiceImpl implements GoodsCSService{

    @Autowired
    private final GoodsRepository goodsRepository;

    @Autowired
    private final GoodsQnARepository goodsQnARepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Override
    public void save(GoodsQnADto goodsQnADto) {
        Goods goods = goodsRepository.findById(goodsQnADto.getGoodsIdx()).get();
        Member member = memberRepository.findByid(goodsQnADto.getMemberId()).get();
        GoodsQnA goodsQnA = new GoodsQnA(goodsQnADto,goods,member);
        goodsQnARepository.save(goodsQnA);
    }

    @Override
    public Page<GoodsQnADto> findbyGoodsQnA(String id, int page) {
        Pageable pageable = PageRequest.of(page,20);
        Member member = memberRepository.findByid(id).get();
        Page<GoodsQnA> goodsQnAS = goodsQnARepository.findAllByMemberOrderByIdxDesc(pageable, member) ;
        return goodsQnAS.map(goodsQnA -> {
            GoodsQnADto goodsQnADto = new GoodsQnADto(goodsQnA);
            return goodsQnADto;
        });
    }

    @Override
    public ReviewDto findbyOrderItem(Integer idx) {
        OrderItem orderItem = orderItemRepository.findById(idx).orElseThrow();
        CartItemDto cartItemDto = new CartItemDto(orderItem);
        ReviewDto reviewDto = new ReviewDto(cartItemDto);
        return null;
    }

	@Override
	public Page<GoodsQnADto> findbyGoodsQnA(int page) {
		Pageable pageable = PageRequest.of(page,20);
		Page<GoodsQnA> goodsQnAs = goodsQnARepository.findAll(pageable);
		 return goodsQnAs.map(goodsQnA -> {
	            GoodsQnADto goodsQnADto = new GoodsQnADto(goodsQnA);
	            return goodsQnADto;
	        });
	}
}
