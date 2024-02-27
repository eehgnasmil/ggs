package ggs.ggs.goods;

import ggs.ggs.domain.Goods;
import ggs.ggs.domain.GoodsLike;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("goodsLikeServiceImpl")
@RequiredArgsConstructor
public class GoodsLikeServiceImpl implements LikeService {
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;
    private final GoodsLikeRepository likeRepository;
    @Override
    public Boolean like(Integer idx, String sid) {
        Goods goods = this.goodsRepository.findById(idx)
                .orElseThrow(() -> new NoSuchElementException("No Goods with idx " + idx));
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Member member = optionalMember.get();

        GoodsLike goodsLike = GoodsLike.builder()
                .goods(goods)
                .member(member)
                .build();

        Boolean exist = likeRepository.existsByGoodsIdxAndMemberIdx(idx,member.getIdx());
        System.out.println(exist);
        if(exist){
            likeRepository.deleteByGoodsIdxAndMemberIdx(idx,member.getIdx());
        }else {
            likeRepository.save(goodsLike);
        }
        exist = likeRepository.existsByGoodsIdxAndMemberIdx(idx,member.getIdx());

        return exist;
    }

    @Override
    public int likeNum(Integer idx) {
        Goods goods = goodsRepository.findById(idx).get();
        int cnt = likeRepository.countByGoods(goods);
        return cnt;
    }

    @Override
    public Page<Object> likeList(String sid, int page) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Pageable pageable = PageRequest.of(page,8);
        Member member = optionalMember.get();

        Page<GoodsLike> goodsLikes = likeRepository.findGoodsLikesWithGoodsByMember(member,pageable);

      return goodsLikes.map(goodsLike -> {
          GoodsDto goodsDto = new GoodsDto(goodsLike.getGoods());
          goodsDto.setLikeTF(true);
          return goodsDto;
      });
    }
}
