package ggs.ggs.board;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.*;
import java.util.stream.Collectors;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Goods;
import ggs.ggs.domain.Reply;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.goods.GoodsRepository;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.member.MemberService;
import ggs.ggs.service.ReplyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements ReplyService {

        private final BoardReplyRepository replyRepository;
        private final BoardRepository boardRepository;
        private final MemberRepository memberRepository;
        private final GoodsRepository goodsRepository;

        @Transactional
        @Override
        public ReplyDto save(ReplyDto replyDto, Integer memberId) {
                Reply reply;

                // 댓글의 idx 값이 있으면 댓글 수정
                if (replyDto.getIdx() != null) {
                        reply = replyRepository.findById(replyDto.getIdx())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "댓글을 찾을 수 없습니다. id=" + replyDto.getIdx()));
                }
                // 댓글의 idx 값이 없으면 댓글 생성
                else {
                        reply = new Reply();
                        if (replyDto.getParentIdx() != null) {
                                Reply parent = replyRepository.findById(replyDto.getParentIdx())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "부모 댓글을 찾을 수 없습니다. id=" + replyDto.getParentIdx()));
                                reply.setParent(parent);
                        }
                }

                Member member = memberRepository.findById(replyDto.getMember())
                                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다. id=" + replyDto.getMember()));
                Board board = boardRepository.findById(replyDto.getBoard())
                                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다. id=" + replyDto.getBoard()));

                // 댓글 내용 업데이트
                reply.updateReply(replyDto, member, board);

                return ReplyDto.from(replyRepository.save(reply));
        }

        @Transactional
        @Override
        public void delete(Long idx) {
                // 댓글 삭제
                Reply reply = replyRepository.findById(idx)
                                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다. id=" + idx));
                replyRepository.delete(reply);
        }

        // @Override
        // public List<ReplyDto> findByBoardId(Long boardId) {
        // Board board = boardRepository.findById(boardId)
        // .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다. id=" + boardId));

        // // 대댓글을 포함한 댓글 리스트를 만듭니다.
        // return replyRepository.findByBoardOrderByParentIdxAscIdxAsc(board).stream()
        // .map(reply -> {
        // ReplyDto dto = ReplyDto.from(reply);
        // Integer memberId = reply.getMember().getIdx(); // 댓글 작성자의 ID
        // Member member = memberRepository.findById(memberId)
        // .orElseThrow(() -> new UsernameNotFoundException(
        // "Member not found with ID: " + memberId));
        // dto.setNickname(member.getNick()); // 닉네임 설정
        // return dto;
        // })
        // .collect(Collectors.toList());
        // }

        @Override
        public List<ReplyDto> findByBoardId(Long boardId) {
                Board board = boardRepository.findById(boardId)
                                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다. id=" + boardId));

                // 대댓글을 포함한 댓글 리스트를 만듭니다.
                return replyRepository.findByBoardOrderByParentIdxAscIdxAsc(board).stream()
                                .map(reply -> ReplyDto.from(reply))
                                .collect(Collectors.toList());
        }

//        @Override
//        public List<ReplyDto> findByGoodsId(Integer goodsId) {
//                Goods goods = goodsRepository.findById(goodsId)
//                                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id=" + goodsId));
//
//                // 대댓글을 포함한 댓글 리스트를 만듭니다.
//                return replyRepository.findByGoodsOrderByParentIdxAscIdxAsc(goods).stream()
//                                .map(reply -> {
//                                        ReplyDto dto = ReplyDto.from(reply);
//                                        Integer memberId = reply.getMember().getIdx(); // 댓글 작성자의 ID
//                                        Member member = memberRepository.findById(memberId)
//                                                        .orElseThrow(() -> new UsernameNotFoundException(
//                                                                        "Member not found with ID: " + memberId));
//                                        dto.setNickname(member.getNick()); // 닉네임 설정
//                                        return dto;
//                                })
//                                .collect(Collectors.toList());
//        }

}