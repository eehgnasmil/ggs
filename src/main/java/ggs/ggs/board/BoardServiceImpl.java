package ggs.ggs.board;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Hashtag;
import ggs.ggs.domain.Member;
import ggs.ggs.dto.BoardDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.HashtagService;
// import ggs.ggs.service.MiddleTagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final HashtagService hashtagService;

    @Transactional
    public BoardDto createBoard(BoardDto dto, String id) {
        Member member = memberRepository.findByid(id)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with ID: "
                        + id));

        dto.setMemberidx(member.getIdx());

        Board board = dto.toEntity(member);
        board = boardRepository.save(board);

        if (dto.getHashtags() != null) {
            for (String hashtag : dto.getHashtags()) {
                hashtagService.createHashtag(hashtag, board);
            }
        }
        return new BoardDto(board);
    }

    @Transactional
    public void updateBoard(Long idx, BoardDto dto) {
        Board board = boardRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다. ID: " + idx));
        board.update(dto);
        boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long idx, Integer userId) {
        Board board = boardRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!board.getMember().getIdx().equals(userId)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }
        boardRepository.delete(board);
    }


    @Transactional
    public List<BoardDto> getAllBoard(String category, String bsort) {
        List<Board> boards = new ArrayList<>();
        if ("new".equals(bsort)) {
            boards = boardRepository.findTop10ByOrderByModifiedDateDesc();
        } else if ("view".equals(bsort)) {
            boards = boardRepository.findTop10ByOrderByViewcountDesc();
        } else if ("like".equals(bsort)) {
            boards = boardRepository.findTop10ByOrderByLikesCountDesc();
        }
        // 이제 Board 객체를 BoardDto 객체로 변환하고 반환합니다.
        List<BoardDto> boardDtos = boards.stream()
                .map(board -> {
                    BoardDto boardDto = new BoardDto(board);

                    // HTML 파싱을 통해 이미지 URL 추출
                    String html = board.getDetail();
                    Document doc = Jsoup.parse(html);
                    Elements imgElements = doc.select("img");
                    for (Element imgElement : imgElements) {
                        String imageUrl = imgElement.attr("src");
                        boardDto.getImageUrls().add(imageUrl);
                    }

                    // 해시태그 설정
                    List<Hashtag> hashtags = getHashtagsForBoard(board.getIdx());
                    List<String> hashtagNames = hashtags.stream().map(Hashtag::getHashtag).collect(Collectors.toList());
                    boardDto.setHashtags(hashtagNames);

                    return boardDto;
                }).collect(Collectors.toList());
        return boardDtos;
    }

    @Transactional(readOnly = true)
    public List<BoardDto> getBoardsByCategory(String category) {
        return boardRepository.findByCategory(category).stream()
                .map(BoardDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<BoardDto> getMyboards(String id, int page) {
        Member member = memberRepository.findByid(id)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with ID: " + id));

        Pageable pageable = PageRequest.of(page, 5);
        Page<Board> boards = boardRepository.findByMember(member, pageable);

        return boards.map(board -> {
            BoardDto dto = new BoardDto(board);

            String html = board.getDetail();
            Document doc = Jsoup.parse(html);
            Elements imgElements = doc.select("img");

            for (Element imgElement : imgElements) {
                String imageUrl = imgElement.attr("src");
                dto.getImageUrls().add(imageUrl);
            }

            return dto;
        });
    }

    @Transactional(readOnly = true)
    public Page<BoardDto> getAllBoards(String category, String bsort, Pageable pageable) {
        Page<Board> boards = "all".equals(category) ? boardRepository.findAll(pageable)
                : boardRepository.findByCategory(category, pageable);

        List<BoardDto> boardDtos = boards.stream()
                .sorted(getComparator(bsort))
                .map(board -> {
                    BoardDto boardDto = new BoardDto(board);

                    String html = board.getDetail();
                    Document doc = Jsoup.parse(html);
                    Elements imgElements = doc.select("img");

                    for (Element imgElement : imgElements) {
                        String imageUrl = imgElement.attr("src");
                        boardDto.getImageUrls().add(imageUrl);
                    }

                    // 여기에서 해시태그를 설정합니다.
                    List<Hashtag> hashtags = getHashtagsForBoard(board.getIdx());
                    List<String> hashtagNames = hashtags.stream().map(Hashtag::getHashtag).collect(Collectors.toList());
                    boardDto.setHashtags(hashtagNames);

                    return boardDto;
                }).collect(Collectors.toList());

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
    }

    @Transactional
    public Comparator<Board> getComparator(String bsort) {
        if ("new".equals(bsort)) {
            return Comparator.comparing(Board::getModifiedDate).reversed();
        } else if ("view".equals(bsort)) {
            return Comparator.comparing(Board::getViewcount).reversed();
        } else if ("like".equals(bsort)) {
            // 좋아요 개수에 따라 정렬
            return Comparator.comparing(Board::getLikesCount).reversed();
        }
        return Comparator.comparing(Board::getModifiedDate).reversed();
    }

    @Transactional(readOnly = true)
    public BoardDto getBoardById(Long idx) {
        Board board = boardRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다. ID: " + idx));
        return new BoardDto(board);
    }

    @Transactional
    public void incrementViewCount(Long idx) {
        Board board = boardRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다. ID: " + idx));
        board.setViewcount(board.getViewcount() + 1);
        boardRepository.save(board);
    }

    // @Override
    // public List<Hashtag> getHashtagsForBoard(Long boardIdx) {
    // return boardRepository.findHashtagsByBoardIdx(boardIdx);
    // }

    @Override
    public List<Hashtag> getHashtagsForBoard(Long boardIdx) {
        List<Hashtag> hashtags = boardRepository.findHashtagsByBoardIdx(boardIdx);
        return hashtags != null ? hashtags : Collections.emptyList();
    }

}
