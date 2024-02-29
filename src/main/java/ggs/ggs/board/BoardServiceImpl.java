package ggs.ggs.board;

import ggs.ggs.domain.Board;
import ggs.ggs.domain.Hashtag;
import ggs.ggs.domain.Member;
import ggs.ggs.domain.MiddleTag;
import ggs.ggs.domain.Reply;
import ggs.ggs.dto.BoardDto;
import ggs.ggs.dto.ReplyDto;
import ggs.ggs.member.MemberRepository;
import ggs.ggs.service.HashtagService;
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
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final HashtagService hashtagService;
    private final BoardTagRepository tagRepository;
    private final BoardMiddleTagRepository middleTagRepository;
    private final BoardReplyRepository replyRepository;

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

        if (dto.getHashtags() != null) {
            hashtagService.updateHashtags(dto.getHashtags(), board);
        }

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

    @Transactional(readOnly = true)
    public List<BoardDto> getBoardsByCategory(String category) {
        return boardRepository.findByCategory(category).stream()
                .map(BoardDto::new)
                .collect(Collectors.toList());
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

    @Override
    public List<Hashtag> getHashtagsForBoard(Long boardIdx) {
        List<Hashtag> hashtags = boardRepository.findHashtagsByBoardIdx(boardIdx);
        return hashtags != null ? hashtags : Collections.emptyList();
    }

    @Transactional
    public Page<BoardDto> getMyboards(String id, int page) {
        Member member = memberRepository.findByid(id)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with ID: " + id));

        Pageable pageable = PageRequest.of(page, 5);
        Page<Board> boards = boardRepository.findByMember(member, pageable);

        return boards.map(this::convertToBoardDto);
    }

    @Transactional
    public Page<BoardDto> getBoardsByUserReplies(String id, int page) {
        Member member = memberRepository.findByid(id)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with ID: " + id));

        List<Reply> replies = replyRepository.findByMember(member);
        List<Board> boards = replies.stream().map(Reply::getBoard).distinct().collect(Collectors.toList());

        int start = page * 5;
        int end = Math.min((start + 5), boards.size());
        List<Board> pagedBoards = boards.subList(start, end);

        return new PageImpl<>(pagedBoards.stream().map(this::convertToBoardDto).collect(Collectors.toList()),
                PageRequest.of(page, 5),
                boards.size());
    }

    @Transactional
    public Page<BoardDto> getBoardsByHashtag(String hashtag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "board.modifiedDate");
        Hashtag targetHashtag = tagRepository.findByHashtag(hashtag).orElseThrow(
                () -> new IllegalArgumentException("해시태그를 찾을 수 없습니다: " + hashtag));
        Page<MiddleTag> middleTags = middleTagRepository.findByHashtagOrderByBoardModifiedDateDesc(targetHashtag,
                pageable);

        List<BoardDto> boardDtos = middleTags.stream()
                .map(MiddleTag::getBoard)
                .filter(Objects::nonNull)
                .map(this::convertToBoardDto)
                .collect(Collectors.toList());

        return new PageImpl<>(boardDtos, pageable, middleTags.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<BoardDto> getAllBoards(String category, String bsort, Pageable pageable) {
        Page<Board> boards = "all".equals(category) ? boardRepository.findAll(pageable)
                : boardRepository.findByCategory(category, pageable);

        List<BoardDto> boardDtos = boards.stream()
                .sorted(getComparator(bsort))
                .map(this::convertToBoardDto)
                .collect(Collectors.toList());

        return new PageImpl<>(boardDtos, pageable, boards.getTotalElements());
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

        return boards.stream()
                .map(this::convertToBoardDto)
                .collect(Collectors.toList());
    }

    private BoardDto convertToBoardDto(Board board) {
        BoardDto boardDto = new BoardDto(board);

        String html = board.getDetail();
        Document doc = Jsoup.parse(html);
        Elements imgElements = doc.select("img");

        for (Element imgElement : imgElements) {
            String imageUrl = imgElement.attr("src");
            boardDto.getImageUrls().add(imageUrl);
        }

        List<String> hashtagNames = middleTagRepository.findByBoard(board).stream()
                .map(MiddleTag::getHashtagName)
                .collect(Collectors.toList());
        boardDto.setHashtags(hashtagNames);

        return boardDto;
    }

}