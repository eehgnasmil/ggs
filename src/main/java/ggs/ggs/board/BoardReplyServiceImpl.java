package ggs.ggs.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Reply;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements ReplyService {

        private final BoardReplyRepository replyRepository;
        private final BoardRepository boardRepository;
        private final MemberRepository memberRepository;

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

        @Override
        public List<ReplyDto> findByBoardId(Long boardId) {
                Board board = boardRepository.findById(boardId)
                                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다. id=" + boardId));

                // 대댓글을 포함한 댓글 리스트를 만듭니다.
                return replyRepository.findByBoardOrderByParentIdxAscIdxAsc(board).stream()
                                .map(reply -> ReplyDto.from(reply))
                                .collect(Collectors.toList());
        }

}