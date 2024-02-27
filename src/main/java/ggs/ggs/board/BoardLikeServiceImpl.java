package ggs.ggs.board;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.BoardLike;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.BoardDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.LikeService;
import lombok.RequiredArgsConstructor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements LikeService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository likeRepository;

    @Override
    public Boolean like(Integer idx, String sid) {
        Board board = this.boardRepository.findById(Long.valueOf(idx))
                .orElseThrow(() -> new NoSuchElementException("No Board with idx " + idx));
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Member member = optionalMember.get();

        BoardLike boardLike = BoardLike.builder()
                .board(board)
                .member(member)
                .build();

        Boolean exist = likeRepository.existsByBoardIdxAndMemberIdx(idx, member.getIdx());
        System.out.println(exist);
        if (exist) {
            likeRepository.deleteByBoardIdxAndMemberIdx(idx, member.getIdx());
            if (board.getLikesCount() > 0) { // likesCount가 0 이상일 때만 감소
                board.setLikesCount(board.getLikesCount() - 1); // 좋아요 제거: likesCount 감소
            }
        } else {
            likeRepository.save(boardLike);
            board.setLikesCount(board.getLikesCount() + 1); // 좋아요 추가: likesCount 증가
        }
        boardRepository.save(board); // 변경된 likesCount 값을 데이터베이스에 저장

        exist = likeRepository.existsByBoardIdxAndMemberIdx(idx, member.getIdx());

        return exist;
    }

    @Override
    public int likeNum(Integer idx) {
        Board board = boardRepository.findById(Long.valueOf(idx)).get();
        int cnt = likeRepository.countByBoard(board);
        return cnt;
    }

    @Override
    public Page<Object> likeList(String sid, int page) {
        Optional<Member> optionalMember = memberRepository.findByid(sid);

        Pageable pageable = PageRequest.of(page, 5);
        Member member = optionalMember.get();

        Page<BoardLike> boardLikes = likeRepository.findBoardLikesWithBoardByMember(member, pageable);

        return boardLikes.map(boardLike -> {
            BoardDto boardDto = new BoardDto(boardLike.getBoard());

            String html = boardLike.getBoard().getDetail();
            Document doc = Jsoup.parse(html);
            Elements imgElements = doc.select("img");

            for (Element imgElement : imgElements) {
                String imageUrl = imgElement.attr("src");
                boardDto.getImageUrls().add(imageUrl);
            }

            return boardDto;
        });
    }
}
