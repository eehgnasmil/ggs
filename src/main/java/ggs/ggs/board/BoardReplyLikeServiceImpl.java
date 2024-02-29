package ggs.ggs.board;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.BoardLike;
import ggs.ggs.domain.GoodsLike;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.Reply;
import ggs.ggs.domain.ReplyLike;
import ggs.ggs.dto.BoardDto;
import ggs.ggs.dto.GoodsDto;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.LikeService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardReplyLikeServiceImpl implements LikeService {
    private final BoardReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardReplyLikeRepository likeRepository;

    @Override
    public Boolean like(Integer idx, String sid) {
        Reply reply = this.replyRepository.findById(Long.valueOf(idx))
                .orElseThrow(() -> new NoSuchElementException("No Reply with idx " + idx));
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Member member = optionalMember.get();

        ReplyLike replyLike = ReplyLike.builder()
                .reply(reply)
                .member(member)
                .build();

        Boolean exist = likeRepository.existsByReplyIdxAndMemberIdx(idx, member.getIdx());
        if (exist) {
            likeRepository.deleteByReplyIdxAndMemberIdx(idx, member.getIdx());
            if (reply.getLikesCount() > 0) { // likesCount가 0 이상일 때만 감소
                reply.setLikesCount(reply.getLikesCount() - 1); // 좋아요 제거: likesCount 감소
            }
        } else {
            likeRepository.save(replyLike);
            reply.setLikesCount(reply.getLikesCount() + 1); // 좋아요 추가: likesCount 증가
        }
        replyRepository.save(reply); // 변경된 likesCount 값을 데이터베이스에 저장

        exist = likeRepository.existsByReplyIdxAndMemberIdx(idx, member.getIdx());

        return exist;
    }

    @Override
    public int likeNum(Integer idx) {
        Reply reply = replyRepository.findById(Long.valueOf(idx)).get();
        int cnt = likeRepository.countByReply(reply);
        return cnt;
    }

    @Override
    public Page<Object> likeList(String sid, int page) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Pageable pageable = PageRequest.of(page, 5);
        Member member = optionalMember.get();

        Page<ReplyLike> replyLikes = likeRepository.findReplyLikesWithReplyByMember(member, pageable);

        return replyLikes.map(replyLike -> {
            ReplyDto replyDto = ReplyDto.from(replyLike.getReply());
            return replyDto;
        });
    }

}
